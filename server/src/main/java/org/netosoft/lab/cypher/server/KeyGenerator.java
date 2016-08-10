package org.netosoft.lab.cypher.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author ernesto
 */
public class KeyGenerator{
	public static void main(String[] args) throws Exception{
		try{
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			final KeyPair key = keyGen.generateKeyPair();

			File keyDir = new File("output/cypher/");
			long l = System.currentTimeMillis();
			File privateKeyFile = new File(keyDir, "private_" + l + ".key");
			File publicKeyFile = new  File(keyDir, "public_" + l + ".key");
			
			System.out.println("PRIVATE");
			System.out.println("Algorithm: " + key.getPrivate().getAlgorithm());
			System.out.println("Encoded: " + DatatypeConverter.printBase64Binary(key.getPrivate().getEncoded()));
			System.out.println("Format: " + key.getPrivate().getFormat());
			System.out.println(key.getPrivate().toString());
			
			System.out.println("\n");
			System.out.println("PUBLIC");
			System.out.println("Algorithm: " + key.getPublic().getAlgorithm());
			System.out.println("Encoded: " + DatatypeConverter.printBase64Binary(key.getPublic().getEncoded()));
			System.out.println("Format: " + key.getPublic().getFormat());
			System.out.println(key.getPublic().toString());

			// Saving the Public key in a file
			try(ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile))){
				publicKeyOS.writeObject(key.getPublic());
			}

			// Saving the Private key in a file
			try(ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile))){
				privateKeyOS.writeObject(key.getPrivate());
			}
		}catch(NoSuchAlgorithmException | IOException e){
			e.printStackTrace(System.err);
		}
	}
}
