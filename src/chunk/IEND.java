package chunk;

import exceptions.TokenizationException;

public record IEND(int length,ChunkType type,byte[]data,int crc) implements Chunk{
    public IEND{
	if(length!=0){
	    throw new IllegalArgumentException("IEND must not have data");
	}
    }
    @Override
    public void print(){
	System.out.println("--- IEND Properties ---");
	Chunk.super.print();	
    }
}
