package org.netosoft.lab.cypher.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author ernesto
 */
public class Application{

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException, InvalidKeyException{
		Properties appProperties = getPropiertes();
		
		ClientApp client = new ClientApp(
				appProperties.getProperty("cypher.algorithm"),
				appProperties.getProperty("cypher.key.public.path"),
				appProperties.getProperty("server.host"),
				Integer.parseInt(appProperties.getProperty("server.port")));
		
		String[] messages = {"¡Hola!", "¿Ernesto?", "¿hermano", "de", "María", "?", "exit"};
		
		for(String word : messages){
			client.sendMessage(word);
			Thread.sleep(100);
		}
	}
	
	private static Properties getPropiertes() throws IOException{
		Properties appProperties = new Properties();
		try(Reader input = new BufferedReader(new FileReader(System.getProperty("application.properties")))){
			appProperties.load(input);
		}catch(IOException ex){
			throw new IOException("Check if the property application.properties is set to the path of the client properties", ex);
		}
		
		return appProperties;
	}
}
