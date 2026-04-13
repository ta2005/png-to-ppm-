package chunk;

import exceptions.PngFormatException;

// import exceptions.TokenizationException;

public record PLTE(int length,ChunkType type,byte[]data,int crc) implements Chunk{
    public PLTE{
	if (!(1*3<=length && length<=3*256) || length%3!=0){
	    throw new PngFormatException("inavlid PLTE");
	}
    }
    @Override
    public void print(){
	System.out.println("--- PLTE Properties ---");
	Chunk.super.print();	
    }
}
