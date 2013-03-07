import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		ReadMessage read = new ReadMessage();
		
		read.readFile();
	}

}