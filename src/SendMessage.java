import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.*;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisTcpReader;
import dk.dma.ais.reader.SendException;
import dk.dma.ais.sentence.Abk;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;

/*
 * http://www.navcen.uscg.gov/?pageName=AISMessages
 * http://www.maritec.co.za/aisvdmvdovalidmsgs.html
 * http://web.arundale.co.uk/docs/ais/ais_decoder.html
 */


public class SendMessage {
	
	private final String host = "localhost";
	private final Integer port = 4001; 
	
	private final String vdmMessage = "!BSVDM,1,1,,A,33u=cDPOh6PmO<HQ1MT1<QC>00oQ,0*71";
	
	private Vdm vdm;

	public void vdmTest() throws SentenceException, AisMessageException, SixbitException, SendException, InterruptedException {
		
		vdm = new Vdm();
		vdm.parse(vdmMessage);
		
		AisMessage3 message = new AisMessage3(vdm);
		AisPosition position = new AisPosition(556945,124922);
		
		message.setPos(position);
		//message.getPos().setRawLatitude(556945);
		//message.getPos().setRawLongitude(124922);
		
		System.out.println("message id: " + message.getMsgId());
		
		ReadMessage readMessage = new ReadMessage();
		readMessage.accept(message);
//		AisReader aisReader = new AisTcpReader(host, port);
//		aisReader.start();
//		
//		Abk abk = aisReader.send(message,1,1);
	}


}
