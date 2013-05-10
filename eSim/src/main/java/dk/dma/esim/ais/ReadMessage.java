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


public class ReadMessage extends Thread {

	private String aisStream = "file";

	private final String filePath = "stream_example.txt";
	private final String URL = "localhost";
	private final Integer port = 4001;
	
	private CopyOnWriteArrayList<Integer> shipMmsi;
	private HashMap<Integer,AisShip> shipHashMap;
	
	/**
	 * Constructor to initiate List of mmsi, and HashMap<mmsi,AisShip>
	 */
	public ReadMessage() {
		shipMmsi = new CopyOnWriteArrayList <Integer>();
		shipHashMap = new HashMap<Integer, AisShip>();
	}
	
	/**
	 * Initiate threading of the class ReadMessage
	 */
	public void run() {
		if (aisStream.toLowerCase().equals("file")) {
			readText();
		} else if (aisStream.toLowerCase().equals("tcp")) {
			readTCP();
		}
	}
	
	/**
	 * Choose between two types of streaming AisMessage
	 * 	file: reading ais message from a file, usefull for debuggin
	 * 	tcp: reading ais message trough the dma proxy
	 * 
	 * @param arg, type of stream
	 */
	public void chooseStream(String arg){
		aisStream = arg;
	}
	
	/**
	 * 
	 * @return ArrayList of current ship mmsi
	 */
	public ArrayList<Integer> getShipMmsi(){	
		ArrayList<Integer> tempShipMmsi = new ArrayList<Integer>();
		
		for (Integer mmsi : shipMmsi){
			tempShipMmsi.add(mmsi);
		}
		
		return tempShipMmsi;
	}
	
	/**
	 * 
	 * @return Hashmap with mmsi keys, and values as AisShip class.
	 */
	public HashMap<Integer,AisShip> getShipHashMap(){
		return shipHashMap;
	}
	
	/**
	 * Creates the AisShip object, and add it to the HashMap, with the coherence mmsi for the ship
	 * 
	 * @param aisMessage
	 */
	private void createShipHashMap(AisMessage aisMessage) {
		int mmsi = aisMessage.getUserId();
		AisShip ship = null;
		
		// Check if ship is already in the list, if not, creat a new ship object
		if (shipHashMap.containsKey(mmsi)) {
			ship = shipHashMap.get(mmsi);
		} else {
			ship = new AisShip(aisMessage);
		}
		
		// Obtain ship geographical latitude and logitude, if possible
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
		
		// Obtain ship length and width, if possible
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
	    
	    // Add/update the HashMap, add to list
		shipHashMap.put(mmsi, ship);
		if (!shipMmsi.contains(mmsi)) shipMmsi.add(mmsi);
	}
	
	/**
	 * Read the ais message from file
	 */
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
	
	/**
	 * Read the ais message from tcp (dma proxy)
	 */
	private void readTCP() {
		System.out.println("Reading from TCP\n");
		
		AisTcpReader reader = new AisTcpReader(URL, port);
		reader.registerHandler(new Consumer<AisMessage>() {         
		    public void accept(AisMessage aisMessage) {
//		        System.out.println("message id: " + aisMessage.getMsgId());
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
