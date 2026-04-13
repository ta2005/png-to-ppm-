package chunk;

// import exceptions.TokenizationException;

public record PLTE(int length,ChunkType type,byte[]data,int crc) implements Chunk{
    public PLTE{
	if (!(1<=length && length<=256) || length%3!=0){
	    throw new IllegalArgumentException("inavlid PLTE");
	}
    }
    @Override
    public void print(){
	System.out.println("--- PLTE Properties ---");
	Chunk.super.print();	
    }
}
