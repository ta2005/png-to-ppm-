package chunk;

public record IEND(int length,int type,byte[]data,int crc) implements Chunk{
    @Override
    public void print(){
	System.out.println("--- IEND Properties ---");
	Chunk.super.print();	
    }
}
