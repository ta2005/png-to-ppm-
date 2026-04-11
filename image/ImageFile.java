package image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import chunk.Chunk;
import chunk.IHDR;

public class ImageFile{
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
	Chunk c = Chunk.of(this);
	if(Chunk.getName(c.type())!="IHDR"){
	    h=(IHDR)c;
	}	
	while(cursor<data.length){
	    c = Chunk.of(this);
	    c.print();
	}	
    }

}
