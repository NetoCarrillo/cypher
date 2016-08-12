package org.netosoft.lab.cypher.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 *
 * @author ernesto
 */
public class Application{

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ClassNotFoundException{
		Properties appProperties = getPropiertes();
		
		if(args.length > 0 && "gen".equals(args[0])){
			KeyGenerator generator = new KeyGenerator(
					appProperties.getProperty("cypher.algorithm"),
					Integer.parseInt(appProperties.getProperty("cypher.key.length")),
					appProperties.getProperty("cypher.key.generation.path"));
			generator.genKeys();
		}else{
			ServerApp server = new ServerApp(
					appProperties.getProperty("cypher.algorithm"),
					appProperties.getProperty("cypher.key.private.path"),
					Integer.parseInt(appProperties.getProperty("server.port")));
			
			server.deploy();
		}
	}
	
	private static Properties getPropiertes() throws IOException{
		Properties appProperties = new Properties();
		try(Reader input = new BufferedReader(new FileReader(System.getProperty("application.properties")))){
			appProperties.load(input);
		}catch(IOException ex){
			throw new IOException("Check if the property application.properties is set to the path of the server properties", ex);
		}
		
		return appProperties;
	}
}
