package chunk;

final public class IEND extends Chunk{
    IEND(Chunk c){
	super(c.length,c.type,c.data,c.crc);
    }
    @Override
    public void print(){
	System.out.println("--- IEND Properties ---");
	super.print();	
    }
}
