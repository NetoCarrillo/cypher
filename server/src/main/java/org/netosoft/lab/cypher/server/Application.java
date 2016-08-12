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
		}else{
			ServerApp server = new ServerApp(
					APP_PROPERTIES.getProperty("cypher.algorithm"),
					APP_PROPERTIES.getProperty("cypher.key.private.path"),
					Integer.parseInt(APP_PROPERTIES.getProperty("server.port")));
			
			server.deploy();
		}
	}
}
