package org.netosoft.lab.cypher.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author ernesto
 */
public class ClientApp{
	/** String to hold name of the encryption algorithm. */
	private static final String ALGORITHM = "RSA";
	
	/** String to hold name of the public key file. */
	private static final String PUBLIC_KEY_FILE = "/home/ernesto/NetBeansProjects/lab-edu/output/cypher/public.key";
	

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
		String[] messages = {"¡Hola!", "¿Ernesto?", "¿hermano", "de", "María", "?"};
		//get the localhost IP address, if server is running on some other IP, you need to use that
		InetAddress host = InetAddress.getLocalHost();
		for(String word : messages){
			talk(host, word);
			Thread.sleep(100);
		}
		talk(host, "exit");
	}

	public static void talk(InetAddress host, String words) throws IOException, ClassNotFoundException{
		//establish socket connection to server
		try(Socket socket = new Socket(host.getHostName(), 4040);
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());){

			oos.writeObject(encryptMessage(words));
				
			String response = (String)ois.readObject();
			
			System.out.printf("Message sent: %s%nMessage recived: %s%nEquals: %b%n%n", words, response, words.equals(response));
		}
	}
	
	private static String encryptMessage(String words) throws IOException, ClassNotFoundException{
		String encrypted;
		try(ObjectInputStream pubKeyInput = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE))){

			// Encrypt the string using the public key
			final PublicKey publicKey = (PublicKey)pubKeyInput.readObject();
			final byte[] cipherText = encrypt(words, publicKey);
			
			encrypted = DatatypeConverter.printBase64Binary(cipherText);
		}
		
		return encrypted;
	}
	
	private static byte[] encrypt(String text, PublicKey key){
		byte[] cipherText = null;
		try{
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text.getBytes());
		}catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
			e.printStackTrace(System.err);
		}
		return cipherText;
	}
		
}
