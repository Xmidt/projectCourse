import java.io.FileInputStream;
import java.io.FileNotFoundException;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.enav.util.function.Consumer;


public class main {

	private final static String logFile = "stream_example.txt";

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

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

}
