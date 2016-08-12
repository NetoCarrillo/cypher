package org.netosoft.lab.cypher.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import sun.security.rsa.RSAPublicKeyImpl;

/**
 *
 * @author ernesto
 */
public class ClientApp{
	/** Name of the encryption algorithm. */
	private final String algorithm;
	/** Key to encrypt messages. */
	private final PublicKey publicKey;
	/** Host where the messages will be send. */
	private final String host;
	/** Port on the host to connect. */
	private final int port;

	/**
	 *
	 * @param algorithm
	 * @param publicKeyPath
	 * @param host
	 * @param port
	 * @throws IOException Error on the load of the file containing the public key.
	 * @throws InvalidKeyException In case an erorr ocurres during the load of
	 * the public key
	 */
	public ClientApp(String algorithm, String publicKeyPath, String host, int port) throws IOException, InvalidKeyException{
		this.algorithm = algorithm;
		this.host = host;
		this.port = port;
		try(InputStream input =
				new BufferedInputStream(new FileInputStream(publicKeyPath));
				ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			
			byte[] buffer = new byte[2048];
			for(int b = input.read(buffer); b != -1; b = input.read(buffer)){
				baos.write(buffer, 0, b);
			}
			
			this.publicKey = new RSAPublicKeyImpl(baos.toByteArray());
		}
	}

	/**
	 * Sends a encrypted message to the host.
	 * @param message the message.
	 * @throws IOException Conextion error.
	 * @throws ClassNotFoundException the type of the reciving message is not found.
	 * @throws InvalidKeyException the key is not valid.
	 */
	public void sendMessage(String message) throws IOException, ClassNotFoundException, InvalidKeyException{
		//establish socket connection to server
		try(Socket socket = new Socket(host, port);
			ObjectOutputStream oos =
					new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois =
					new ObjectInputStream(socket.getInputStream());){

			oos.writeObject(encryptMessage(message));
				
			String response = (String)ois.readObject();
			
			//Shows that the sent encrypted message is the same decrypted by the server.
			System.out.printf("Message sent: %s%nMessage recived: %s%nEquals: %b%n%n",
					message, response, message.equals(response));
		}
	}
	
	private String encryptMessage(String text) throws IOException, InvalidKeyException{
		String encrypted;
		try{
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(algorithm);
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] cipherText = cipher.doFinal(text.getBytes());
			encrypted = DatatypeConverter.printBase64Binary(cipherText);
			
		}catch(GeneralSecurityException e){
			encrypted = null;
			e.printStackTrace(System.err);
		}
		
		return encrypted;
	}
}
