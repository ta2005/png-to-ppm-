package image;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import chunk.Chunk;
import chunk.IHDR;
import chunk.IDAT;
import parser.Parser;
import decoder.Decoder;

public class ImageFile{
    //maybe the cursor will become private and it add a metod a get the next n bytes
    public int cursor;
    public byte[] data;
    public String inputFileName;
    public String outputFileName;
    public IHDR h;
    public ImageFile(String inputFileName){
	this.inputFileName=inputFileName;
	try{
	    this.data =  Files.readAllBytes(Paths.get(inputFileName));
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}
    }
    public void show() throws Exception{
	var l = Chunk.chunkTokenizer(this);
	for(var c:l){
	    if(c!=null)
		c.print();
	}
	Parser p = new Parser(l);
	Decoder d=new Decoder(p.getData(), p.getHead());
	// for (int i = 0; i < p.getData().length; i++) {
	//     System.out.printf("%02X", p.getData()[i] & 0xFF);
	// }
	String file = "wa.ppm";
	System.out.println("writing to a file");
	try (var f = new PrintStream(file)) {
	    f.println("P3");
	    f.println(p.getHead().width() + " " + p.getHead().height());
	    f.println(255);

	    byte[] rawData = d.getImage();

	    // We need to know how to navigate the rawData array
	    int bytesPerPixel = switch (p.getHead().colorType()) {
		case 2 -> 3; // RGB
		case 6 -> 4; // RGBA
		default -> throw new UnsupportedOperationException("Only RGB/RGBA supported for this quick test");
	    };

	    // scanSize includes the 1 filter byte + all pixel bytes for the row
	    int scanSize = 1 + (p.getHead().width() * bytesPerPixel);

	    for (int y = 0; y < p.getHead().height(); y++) {
		int rowOffset = y * scanSize;

		// j starts at 1 to SKIP the filter byte at rowOffset + 0
		for (int j = 1; j < scanSize; j += bytesPerPixel) {

		    int r = rawData[rowOffset + j] & 0xFF;
		    int g = rawData[rowOffset + j + 1] & 0xFF;
		    int b = rawData[rowOffset + j + 2] & 0xFF;

		    // If it's RGBA (Color Type 6), there is an alpha byte at j + 3, 
		    // but we just ignore it because PPM doesn't support transparency.

		    f.print(r + " " + g + " " + b + "  ");
		}
		f.println(); // Newline after every row makes the PPM file readable
	    }
	}

    }
}
