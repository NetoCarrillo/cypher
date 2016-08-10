package org.netosoft.lab.cypher.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author ernesto
 */
public class ServerApp{
	/** String to hold name of the encryption algorithm. */
	private static final String ALGORITHM = "RSA";

	/** String to hold the name of the private key file. */
	private static final String PRIVATE_KEY_FILE = "/home/ernesto/NetBeansProjects/lab-edu/output/cypher/private.key";

	//socket server port on which it will listen
	private static final int PORT = 4040;

	public static void main(String args[]) throws IOException, ClassNotFoundException{
		//create the socket server object
		try(ServerSocket server = new ServerSocket(PORT);){
			while(true){
				System.out.println("Waiting for client request");
				
				String message = readMessage(server);
				
				//terminate the server if client sends exit request
				if(message.equalsIgnoreCase("exit")){
					break;
				}
			}
		}finally{
			System.out.println("Shutting down Socket server!!");
		}
	}
	
	private static String readMessage(ServerSocket server) throws IOException, ClassNotFoundException{
		String message;

		try(Socket socket = server.accept();
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());){

			message = (String)ois.readObject();
			oos.writeObject(decryptMessage(message));
		}
		
		return message;
	}
	
	private static String decryptMessage(String message){
		String plainText;
		try(ObjectInputStream prvKeyInput = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));){

			// Decrypt the cipher text using the private key.
			final PrivateKey privateKey = (PrivateKey)prvKeyInput.readObject();
//			 plainText = decrypt(message, privateKey);

			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(ALGORITHM);

			// decrypt the text using the private key
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] text = DatatypeConverter.parseBase64Binary(message);
			byte[] dectyptedText = cipher.doFinal(text);

			plainText = new String(dectyptedText);

		}catch(IOException | ClassNotFoundException | GeneralSecurityException ex){
			ex.printStackTrace(System.err);
			plainText = null;
		}

		return plainText;
	}
}
