package org.netosoft.lab.cypher.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author ernesto
 */
public class KeyGenerator{

	private final String algorithm;
	private final int keyLegnth;
	private final Path keyPath;

	public KeyGenerator(String algorithm, int keyLegnth, String keyPath) throws IOException{
		this.algorithm = algorithm;
		this.keyLegnth = keyLegnth;
		this.keyPath = Paths.get(keyPath);
		
		if(Files.notExists(this.keyPath)){
			throw new FileNotFoundException("Path does not exists " + this.keyPath);
		}
		
		if(!Files.isDirectory(this.keyPath)){
			throw new NotDirectoryException(this.keyPath.toString());
		}
	}
	
	

	public void genKeys() throws IOException, NoSuchAlgorithmException{
		final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
		keyGen.initialize(keyLegnth);
		final KeyPair key = keyGen.generateKeyPair();

		File keyDir = keyPath.toFile();
		File privateKeyFile = new File(keyDir, "private.key");
		File publicKeyFile = new File(keyDir, "public.key");

		//debug
		System.out.println("PRIVATE");
		System.out.println("Class: " + key.getPrivate().getClass());
		System.out.println("Algorithm: " + key.getPrivate().getAlgorithm());
		System.out.println("Encoded: " + DatatypeConverter.printBase64Binary(key.getPrivate().getEncoded()));
		System.out.println("Format: " + key.getPrivate().getFormat());
		System.out.println(key.getPrivate().toString());

		System.out.println("\n");
		System.out.println("PUBLIC");
		System.out.println("Class: " + key.getPublic().getClass());
		System.out.println("Algorithm: " + key.getPublic().getAlgorithm());
		System.out.println("Encoded: " + DatatypeConverter.printBase64Binary(key.getPublic().getEncoded()));
		System.out.println("Format: " + key.getPublic().getFormat());
		System.out.println(key.getPublic().toString());
		//debug

		// Saving the Public key in a file
		try(OutputStream out = new BufferedOutputStream(new FileOutputStream(publicKeyFile))){
			out.write(key.getPublic().getEncoded());
		}

		// Saving the Private key in a file
		try(OutputStream out = new BufferedOutputStream(new FileOutputStream(privateKeyFile))){
			out.write(key.getPrivate().getEncoded());
		}
		
//		// Saving the Public key in a file
//		try(ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile))){
//			publicKeyOS.writeObject(key.getPublic());
//		}
//
//		// Saving the Private key in a file
//		try(ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile))){
//			privateKeyOS.writeObject(key.getPrivate());
//		}
		
	}
}
