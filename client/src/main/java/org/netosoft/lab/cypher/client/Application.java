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
	static final Properties APP_PROPERTIES;
	
	static{
		APP_PROPERTIES = new Properties();
		
		try(Reader input = new BufferedReader(new FileReader(System.getProperty("application.properties")))){
			APP_PROPERTIES.load(input);
		}catch(IOException ex){
			ex.printStackTrace(System.err);
		}
		
		//debug
		for(Map.Entry<Object, Object> e : APP_PROPERTIES.entrySet()){
			System.out.printf("%s\t\t%s%n", e.getKey(), e.getValue());
		}
		//debug
	}

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException, InvalidKeyException{
		
		ClientApp client = new ClientApp(
				APP_PROPERTIES.getProperty("cypher.algorithm"),
				APP_PROPERTIES.getProperty("cypher.key.public.path"),
				APP_PROPERTIES.getProperty("server.host"),
				Integer.parseInt(APP_PROPERTIES.getProperty("server.port")));
		
		String[] messages = {"¡Hola!", "¿Ernesto?", "¿hermano", "de", "María", "?"};
		
		for(String word : messages){
			client.sendMessage(word);
			Thread.sleep(100);
		}
		
		client.sendMessage("exit");
	}
	
}
