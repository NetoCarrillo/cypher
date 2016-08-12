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

/**
 *
 * @author ernesto
 */
public class KeyGenerator{
	/** Name of the encryption algorithm. */
	private final String algorithm;
	/** Key size in bits. */
	private final int keyLegnth;
	/** Directory where the generated keys will be placed. */
	private final Path keyPath;

	/**
	 * 
	 * @param algorithm
	 * @param keyLegnth
	 * @param keyPath
	 * @throws IOException If <code>keyPath</code> does not exists or is not a 
	 * directory.
	 */
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
	
	/**
	 * 
	 * @throws IOException Error on writing the keys.
	 * @throws NoSuchAlgorithmException if the algorithm does not exists.
	 */
	public void genKeys() throws IOException, NoSuchAlgorithmException{
		final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
		keyGen.initialize(keyLegnth);
		final KeyPair key = keyGen.generateKeyPair();

		File keyDir = keyPath.toFile();
		File privateKeyFile = new File(keyDir, "private.key");
		File publicKeyFile = new File(keyDir, "public.key");

		// Saving the Public key in a file
		try(OutputStream out =
				new BufferedOutputStream(new FileOutputStream(publicKeyFile))){
			out.write(key.getPublic().getEncoded());
		}

		// Saving the Private key in a file
		try(OutputStream out =
				new BufferedOutputStream(new FileOutputStream(privateKeyFile))){
			out.write(key.getPrivate().getEncoded());
		}
	}
}
