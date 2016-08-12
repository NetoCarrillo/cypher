package org.netosoft.lab.cypher.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

/**
 *
 * @author ernesto
 */
public class ClientApp{
	/** String to hold name of the encryption algorithm. */
	private final String algorithm;
	
	private final PublicKey publicKey;
//			"/home/ernesto/NetBeansProjects/lab-edu/output/cypher/public_1470867697609.key";
//			"/home/ernesto/tmp/rsa/public_1.key";
			
	private final String host;
	private final int port;

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

	public void sendMessage(String message) throws IOException, ClassNotFoundException, InvalidKeyException{
		//establish socket connection to server
		try(Socket socket = new Socket(host, port);
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());){

			oos.writeObject(encryptMessage(message));
				
			String response = (String)ois.readObject();
			
			System.out.printf("Message sent: %s%nMessage recived: %s%nEquals: %b%n%n", message, response, message.equals(response));
		}
	}
	
	private String encryptMessage(String words) throws IOException, InvalidKeyException{
		// Encrypt the string using the public key
		final byte[] cipherText = encrypt(words, publicKey);

		return DatatypeConverter.printBase64Binary(cipherText);
	}
	
	private byte[] encrypt(String text, PublicKey key){
		byte[] cipherText = null;
		try{
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(algorithm);
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text.getBytes());
		}catch(GeneralSecurityException e){
			e.printStackTrace(System.err);
		}
		return cipherText;
	}
		
}
