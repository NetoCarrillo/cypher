package org.netosoft.lab.cypher.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 *
 * @author ernesto
 */
public class Application{
	static final Properties APP_PROPERTIES;
	
	static{
		APP_PROPERTIES = new Properties();
		
		try(Reader input = new BufferedReader(new FileReader(System.getProperty("application.properties")))){
			APP_PROPERTIES.load(input);
		}catch(IOException ex){
			ex.printStackTrace(System.err);
		}
		
	}
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ClassNotFoundException{
		if(args.length > 0 && "gen".equals(args[0])){
			KeyGenerator generator = new KeyGenerator(
					APP_PROPERTIES.getProperty("cypher.algorithm"),
					Integer.parseInt(APP_PROPERTIES.getProperty("cypher.key.length")),
					APP_PROPERTIES.getProperty("cypher.key.generation.path"));
			generator.genKeys();
//			//debug
//			try{
//				readKeys();
//			}catch(Exception ex){
//				ex.printStackTrace(System.err);
//			}
//			//debug
		}else{
			ServerApp server = new ServerApp(
					APP_PROPERTIES.getProperty("cypher.algorithm"),
					APP_PROPERTIES.getProperty("cypher.key.private.path"),
					Integer.parseInt(APP_PROPERTIES.getProperty("server.port")));
			
			server.deploy();
		}
	}
	
//	public static void readKeys() throws Exception{
//		
//		Path pvtKeyPath = Paths.get(APP_PROPERTIES.getProperty("cypher.key.generation.path"), "private.key");
//		Path pubKeyPath = Paths.get(APP_PROPERTIES.getProperty("cypher.key.generation.path"), "public.key");
//		
//		try(InputStream input =
//				new BufferedInputStream(new FileInputStream(pubKeyPath.toFile()));
//				ByteArrayOutputStream baos = new ByteArrayOutputStream()){
//			
//			byte[] buffer = new byte[2048];
//			for(int b = input.read(buffer); b != -1; b = input.read(buffer)){
//				baos.write(buffer, 0, b);
//			}
//			
//			PublicKey key = new RSAPublicKeyImpl(baos.toByteArray());
//			
//			printTree(key);
//		}
//
//		// Saving the Private key in a file
//		try(InputStream input = 
//				new BufferedInputStream(new FileInputStream(pvtKeyPath.toFile()));
//				ByteArrayOutputStream baos = new ByteArrayOutputStream()){
//			
//			byte[] buffer = new byte[2048];
//			for(int b = input.read(buffer); b != -1; b = input.read(buffer)){
//				baos.write(buffer, 0, b);
//			}
//			
//			PrivateKey key = RSAPrivateCrtKeyImpl.newKey(baos.toByteArray());
//
//			printTree(key);
//		}
//	}
//	
//	private static void printTree(Object obj){
//		Class<?> c = obj.getClass();
//		
//		System.out.println("Clases:");
//		
//		for(Class<?> d = c; d != null; d = d.getSuperclass()){
//			System.out.println("\t" + d);
//			System.out.println("\t\tInterfaces:");
//			for(Class<?> i : d.getInterfaces()){
//				System.out.println("\t\t\t" + i);
//			}
//			System.out.println();
//		}
//	}
}
