package org.netosoft.lab.cypher.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import sun.security.rsa.RSAPrivateCrtKeyImpl;

/**
 *
 * @author ernesto
 */
public class ServerApp{
	/** String to hold name of the encryption algorithm. */
	private final String algorithm;

	/** String to hold the name of the private key file. */
	private final PrivateKey privateKey;

	//socket server port on which it will listen
	private final int port;
	
	public ServerApp(String algorithm, String privateKeyPath, int port) throws IOException, InvalidKeyException{
		this.algorithm = algorithm;
		this.port = port;
		try(InputStream input =
				new BufferedInputStream(new FileInputStream(privateKeyPath));
				ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			
			byte[] buffer = new byte[2048];
			for(int b = input.read(buffer); b != -1; b = input.read(buffer)){
				baos.write(buffer, 0, b);
			}
			
			privateKey = RSAPrivateCrtKeyImpl.newKey(baos.toByteArray());
		}
	}
	
	public void deploy() throws IOException, ClassNotFoundException{
		//create the socket server object
		try(ServerSocket server = new ServerSocket(port);){
			while(true){
				System.out.println("Waiting for client request");
				
				String message = readMessage(server);
				
				System.out.printf("Readed message: %s%n", message);
				//terminate the server if client sends exit request
				if(message.equalsIgnoreCase("exit")){
					break;
				}
			}
		}finally{
			System.out.println("Shutting down Socket server!!");
		}
	}
	
	private String readMessage(ServerSocket server) throws IOException, ClassNotFoundException{
		String message;

		try(Socket socket = server.accept();
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());){

			String encrypted = (String)ois.readObject();
			message = decryptMessage(encrypted);
			oos.writeObject(message);
		}
		
		return message;
	}
	
	private String decryptMessage(String message){
		String plainText;
		try{
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(algorithm);

			// decrypt the text using the private key
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] text = DatatypeConverter.parseBase64Binary(message);
			byte[] dectyptedText = cipher.doFinal(text);

			plainText = new String(dectyptedText);

		}catch(GeneralSecurityException ex){
			System.err.println("Decryption error");
			ex.printStackTrace(System.err);
			plainText = null;
		}

		return plainText;
	}
}
