import java.io.FileInputStream;
import java.io.FileNotFoundException;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage21;			// accept
import dk.dma.ais.message.AisPositionMessage;	// accept
import dk.dma.ais.message.AisStaticCommon;		// accept
import dk.dma.ais.packet.AisPacket;				// readRawTCP
import dk.dma.ais.reader.AisReader;				// readFile
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.ais.reader.AisTcpReader;			// readTCP
import dk.dma.enav.util.function.Consumer;

/*
 * jar's needed to compile and execute this script:
 * 
 * 	~/workspace/AisLib/ais-messages/target/ais-messages-0.1-SNAPSHOT.jar
 *	~/workspace/AisLib/ais-communication/target/ais-communication-0.1-SNAPSHOT.jar
 *	~/workspace/e-Navigation/enav-util/target/enav-util-0.1-SNAPSHOT.jar
 *	~/.m2/repository/dk/dma/commons/dma-commons-util/0.1-SNAPSHOT/dma-commons-util-0.1-SNAPSHOT.jar
 *	~/.m2/repository/org/slf4j/slf4j-api/1.6.6/slf4j-api-1.6.6.jar
 *	~/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.jar
 *	~/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar
 *	~/.m2/repository/dk/dma/enav/enav-model/0.1-SNAPSHOT/enav-model-0.1-SNAPSHOT.jar
 *	~/.m2/repository/org/slf4j/slf4j-log4j12/1.6.6/slf4j-log4j12-1.6.6.jar
 */

public class ReadMessage {

	private final String logFile = "stream_example.txt";
	
	private final String URL = "localhost";
	private final Integer port = 10001; 

	/*
	 * A simple example about how to retrieve the message id
	 */
	public void readFile() throws FileNotFoundException, InterruptedException{
		AisReader reader = new AisStreamReader(new FileInputStream(logFile));
		
		reader.registerHandler(new Consumer<AisMessage>() {         
		    @Override
		    public void accept(AisMessage aisMessage) {
		        System.out.println("message id: " + aisMessage.getMsgId());
		    }
		});
		
		reader.start();
		reader.join();
	}
	
	public void readTCP() throws InterruptedException{
		AisTcpReader reader = new AisTcpReader(URL, port);
		
		reader.registerHandler(new Consumer<AisMessage>() {         
		    @Override
		    public void accept(AisMessage aisMessage) {
		        System.out.println("message id: " + aisMessage.getMsgId());     
		    }
		});
		reader.start();
		reader.join();
	}
	
	/*
	 * This is how to handle raw unparsed messages
	 */
	public void readRawFile() throws FileNotFoundException, InterruptedException{
		AisReader reader = new AisStreamReader(new FileInputStream(logFile));
		
		reader.registerPacketHandler(new Consumer<AisPacket>() {            
		    @Override
		    public void accept(AisPacket aisPacket) {

		    }
		});
		reader.start();
		reader.join();
	}

	public void readRawTCP() throws InterruptedException{
		AisTcpReader reader = new AisTcpReader(URL, port);
		
		reader.registerPacketHandler(new Consumer<AisPacket>() {            
		    @Override
		    public void accept(AisPacket aisPacket) {

		    }
		});
		reader.start();
		reader.join();
	}
	
	/*
	 * How to work with the AisMessage object, and retrieve further information
	 */
	public void accept(AisMessage aisMessage) {
	    // Handle AtoN message
	    if (aisMessage instanceof AisMessage21) {
	        AisMessage21 msg21 = (AisMessage21) aisMessage;
	        System.out.println("AtoN name: " + msg21.getName());
	    }
	    // Handle position messages 1,2 and 3 (class A) by using their shared parent
	    if (aisMessage instanceof AisPositionMessage) {                 
	        AisPositionMessage posMessage = (AisPositionMessage)aisMessage;
	        System.out.println("speed over ground: " + posMessage.getSog());
	    }
	    // Handle position messages 1,2,3 and 18 (class A and B)  
	    if (aisMessage instanceof IGeneralPositionMessage) {
	        IGeneralPositionMessage posMessage = (IGeneralPositionMessage)aisMessage;
	        System.out.println("course over ground: " + posMessage.getCog());
	    }
	    // Handle static reports for both class A and B vessels (msg 5 + 24)
	    if (aisMessage instanceof AisStaticCommon) {
	        AisStaticCommon staticMessage = (AisStaticCommon)aisMessage;
	        System.out.println("vessel name: " + staticMessage.getName());
	    }                   
	}
}
