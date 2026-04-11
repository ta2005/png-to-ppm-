package image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public void show(){
	for(int i=0;i<8;i++){
	    System.out.printf("%02X ",data[i]&0xFF);
	}
	System.out.println();
	cursor+=8;
	Chunk c = Chunk.chunkParser(this);
	if(c instanceof IHDR){
	    h=(IHDR)c;
	}	
	c.print();
	while(cursor<data.length){
	    c = Chunk.chunkParser(this);
	    c.print();
	    if(c instanceof IDAT){
		((IDAT)c).convert(this);
	    }	
	}

    }
}
