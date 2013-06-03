package dk.dma.esim.ais;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.util.concurrent.CopyOnWriteArrayList;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.ais.reader.AisTcpReader;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.util.function.Consumer;
import dk.dma.esim.virtualship.VirtualShip;
import java.util.concurrent.ConcurrentHashMap;


public class ReadMessage extends Thread {

	private String aisStream = "file";

	private final String filePath = "stream_example.txt";
	private final String URL = "localhost";
	private final Integer port = 4001;
	
	private ConcurrentHashMap<Integer,VirtualShip> shipHashMap;
	
	/**
	 * Constructor to initiate List of mmsi, and HashMap<mmsi,AisShip>
	 */
	public ReadMessage() {
		shipHashMap = new ConcurrentHashMap<Integer, VirtualShip>();
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
	 * @return Hashmap with mmsi keys, and values as AisShip class.
	 */
	public ConcurrentHashMap<Integer,VirtualShip> getShipHashMap(){
		return shipHashMap;
	}
	
	/**
	 * Creates the AisShip object, and add it to the HashMap, with the coherence mmsi for the ship
	 * 
	 * @param aisMessage
	 */
	private void createShipHashMap(AisMessage aisMessage) {
		int mmsi = aisMessage.getUserId();
		VirtualShip ship = null;
		
		/**
		 * Check if ship is already in the list, if not, create a new ship object,
		 * and fill out the various variables if possible.
		 */
		if (shipHashMap.containsKey(mmsi)) {
			ship = shipHashMap.get(mmsi);
                        
                        
		} else {
			ship = new VirtualShip();
			ship.setNode(String.valueOf(mmsi));
			
			 /**
		     * GPS Ant. Distance from stern (B) Reference point for reported position. Also indicates the dimension of ship (m)
		     * (see Fig. 42 and § 3.3.3)
		     * 
		     * GPS Ant. Distance from starboard (D): Reference point for reported position. Also indicates the dimension of ship
		     * (m) (see Fig. 42 and § 3.3.3)
		     */
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
		    
		    /**
		     * Ship name: Maximum 20 characters 6 bit ASCII, as defined in Table 44
		     * 
		     * @@@@@@@@@@@@@@@@@@@@ = not available = default. For SAR aircraft, it should be set to "SAR AIRCRAFT NNNNNNN"
		     *                      where NNNNNNN equals the aircraft registration number
		     */
		    if (aisMessage instanceof AisStaticCommon) {
		        AisStaticCommon staticMessage = (AisStaticCommon)aisMessage;
		        try {
		        	ship.setName(staticMessage.getName());
		        	
		        } catch(NullPointerException e) {
		        	// Ship name not found
		        	ship.setName("");
		        }
		    }
		    
		    /**
		     * Type of ship and cargo type: 0 = not available or no ship = default 1-99 = as defined in � 3.3.2 100-199 =
		     * reserved, for regional use 200-255 = reserved, for future use Not applicable to SAR aircraft
		     */
		    if (aisMessage instanceof AisStaticCommon) {
		        AisStaticCommon staticMessage = (AisStaticCommon)aisMessage;
		        try {
		        	ship.setShipType(staticMessage.getShipType());
		        	
		        } catch(NullPointerException e) {
		        	// Ship length and width not found
		        	ship.setShipType(0);
		        }
		    }		    
		}
		
		ship.setShipAisMessage(aisMessage);
		
		// Obtain ship geographical latitude and longitude, if possible
		if (aisMessage instanceof AisPositionMessage) {
			AisPositionMessage posMessage = (AisPositionMessage)aisMessage;
			Position position = posMessage.getPos().getGeoLocation();
	        try {
	        	ship.setShipLatitude(position.getLatitude());
	        	ship.setShipLongitude(position.getLongitude());
	        } catch(NullPointerException e) {
	        	// Ship latitude and longitude not found
	        	ship.setShipLatitude(0);
	        	ship.setShipLongitude(0);
	        }
		}
		
	    /**
	     * Heading Degrees (0-359) (> 359 indicates not available = 0)
	     */
	    if (aisMessage instanceof AisPositionMessage) {
	    	AisPositionMessage positionMessage = (AisPositionMessage)aisMessage;
	    	try {
	    		int heading = positionMessage.getTrueHeading();
	    		if (heading > 359 ) {
	    			heading = 0;
	    		}
	    		ship.setShipHeading(heading);
	    		
	    	} catch(NullPointerException e) {
	    		ship.setShipHeading(0);
	    	}
	    }
            
        /**
         * Speed
         */
        if (aisMessage instanceof AisPositionMessage) {
	    	AisPositionMessage positionMessage = (AisPositionMessage)aisMessage;
	    	try {
                int speedOverGround = positionMessage.getSog();
	    		ship.setForwardSpeed(Math.ceil((double)speedOverGround/100.0));
	    		
	    	} catch(NullPointerException e) {
	    		ship.setForwardSpeed(0);
	    	}
	    }
            
	    // Add/update the HashMap, add to list
		shipHashMap.put(mmsi, ship);
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
