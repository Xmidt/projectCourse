package ais.reader;

import java.util.ArrayList;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.reader.AisTcpReader;
import dk.dma.enav.util.function.Consumer;

/*
 * The following libraries are needed for the Ais TCP reader:
 * 
 * 	ais-communication.jar		// AisLib
 * 	ais-messages.jar			// AisLib
 * 	commons-lang.jar
 * 	dma-commons-util.jar
 * 	enav-model.jar				// e-Navigation
 * 	enav-util.jar				// e-Navigation
 * 	slf4j-api.jar
 */

public class ReadMessage extends Thread {

	private final String URL = "localhost";
	private final Integer port = 4001;
	
	private ArrayList<AisMessage> aisMessageList;
	
	public void run() {
		
		aisMessageList = new ArrayList<>();
		
		try{
			AisTcpReader reader = new AisTcpReader(URL, port);
	
			reader.registerHandler(new Consumer<AisMessage>() {         
			    @Override
			    public void accept(AisMessage aisMessage) {
			    	aisMessageList.add(aisMessage);
			    }
			});
			reader.start();
			reader.join();
		} catch(InterruptedException e) {
			System.out.print("No connecting to proxy");
		}

	}
	
	public ArrayList<AisMessage> getAisMessage(){
		ArrayList<AisMessage> tempAisMessageList = aisMessageList;
		aisMessageList.clear();
		return tempAisMessageList;
	}
}
