import java.io.FileNotFoundException;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.reader.SendException;
import dk.dma.ais.sentence.SentenceException;

public class Main {

	public static void main(String[] args) throws SendException, SentenceException, AisMessageException, SixbitException, InterruptedException {
		ReadMessage read = new ReadMessage();
		SendMessage send = new SendMessage();
		
		send.vdmTest();
		
		//read.readFile();
		//read.readTCP();
		//read.readRawFile();
		//read.readRawTCP();
	}

}