package dk.dma.esim.ais;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.ais.reader.AisTcpReader;
import dk.dma.enav.model.geometry.Position;
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

	private String aisStream = "file";

	private final String filePath = "stream_example.txt";
	private final String URL = "localhost";
	private final Integer port = 4001;
	
	private CopyOnWriteArrayList<Integer> shipMmsi;
	private HashMap<Integer,AisShip> shipHashMap;
	
	public ReadMessage() {
		shipMmsi = new CopyOnWriteArrayList <Integer>();
		shipHashMap = new HashMap<Integer, AisShip>();
	}
	
	public void run() {
		if (aisStream.toLowerCase().equals("file")) {
			readText();
		} else if (aisStream.toLowerCase().equals("tcp")) {
			readTCP();
		}
	}
	
	public void chooseStream(String arg){
		aisStream = arg;
	}
	
	public ArrayList<Integer> getShipMmsi(){	
		ArrayList<Integer> tempShipMmsi = new ArrayList<Integer>();
		
		for (Integer mmsi : shipMmsi){
			tempShipMmsi.add(mmsi);
		}
		
		return tempShipMmsi;
	}
	
	public HashMap<Integer,AisShip> getShipHashMap(){
		return shipHashMap;
	}
	
	private void createShipHashMap(AisMessage aisMessage) {
		int mmsi = aisMessage.getUserId();
		AisShip ship = null;
		
		if (shipHashMap.containsKey(mmsi)) {
			ship = shipHashMap.get(mmsi);
		} else {
			ship = new AisShip(aisMessage);
		}
		
		if (aisMessage instanceof AisPositionMessage) {
			AisPositionMessage posMessage = (AisPositionMessage)aisMessage;
			Position position = posMessage.getPos().getGeoLocation();
	        try {
	        	ship.setShipLatitude(position.getLatitude());
	        	ship.setShipLongitude(position.getLongitude());
	        } catch(NullPointerException e) {
	        	// Ship latitude and longitude not found
	        }
		}
		
	    if (aisMessage instanceof AisStaticCommon) {
	        AisStaticCommon staticMessage = (AisStaticCommon)aisMessage;
	        try {
	        	ship.setLength(staticMessage.getDimStern());
	        	ship.setWidth(staticMessage.getDimStarboard());
	        	
	        } catch(NullPointerException e) {
	        	// Ship length and width not found
	        	ship.setLength(0);
	        	ship.setWidth(0);
	        }

	    }
	    
		shipHashMap.put(mmsi, ship);
		
		if (!shipMmsi.contains(mmsi)) shipMmsi.add(mmsi);
	}
	
	private void readText() {
		System.out.print("Reading from textfile\n");

		AisReader reader = null;
		try {
			reader = new AisStreamReader(new FileInputStream(filePath));
		} catch (FileNotFoundException e1) {
			// Cannot locate file
			e1.printStackTrace();
		}
		reader.registerHandler(new Consumer<AisMessage>() {         
		    public void accept(AisMessage aisMessage) {
		    	createShipHashMap(aisMessage);
		    }
		});
		reader.start();
		try {
			reader.join();
		} catch (InterruptedException e0) {
			// Lost stream
			e0.printStackTrace();
		}
	}
	
	private void readTCP() {
		System.out.println("Reading from TCP\n");
		
		AisTcpReader reader = new AisTcpReader(URL, port);
		reader.registerHandler(new Consumer<AisMessage>() {         
		    public void accept(AisMessage aisMessage) {
		        System.out.println("message id: " + aisMessage.getMsgId());
		        createShipHashMap(aisMessage);
		    }
		});
		reader.start();
		try {
			reader.join();
		} catch (InterruptedException e) {
			// Lost stream
			e.printStackTrace();
		}
	}
}
