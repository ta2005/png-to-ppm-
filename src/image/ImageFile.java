package image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import chunk.Chunk;
import chunk.IHDR;
import chunk.IDAT;

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
    public void show() throws IllegalArgumentException{
	var l = Chunk.chunkTokenizer(this);
	for(var c:l){
	    c.print();
	}
    }

}
