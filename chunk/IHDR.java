package chunk;

import java.util.Arrays;

public final class IHDR extends Chunk{
    int width;
    int height;
    byte bitDepth;
    byte colorType;
    byte compressionMethod;
    byte filterMethod;
    byte interlaceMethod;
    IHDR(Chunk c){
	super(c.length,c.type,c.data,c.crc);
	int cursor=0;
	width=toInt(Arrays.copyOfRange(this.data,cursor,cursor+4));
	cursor+=4;
	height=toInt(Arrays.copyOfRange(this.data,cursor,cursor+4));
	cursor+=4;
	bitDepth=data[cursor++];
	colorType=data[cursor++];
	compressionMethod=data[cursor++];
	filterMethod=data[cursor++];
	interlaceMethod=data[cursor++];
    }
    public int width(){
	return width;
    }
    public int height(){
	return height;
    }
    public byte bitDepth(){
	return bitDepth;
    }
    public byte colorType(){
	return colorType;
    }
    public byte compressionMethod(){
	return compressionMethod;
    }
    public byte filterMethod(){
	return filterMethod;
    }
    public byte interlaceMethod(){
	return interlaceMethod;
    }

    @Override
    public void print(){
	super.print();
	System.out.println("--- IHDR Properties ---");
	System.out.println("Width              : " + width);
	System.out.println("Height             : " + height);
	System.out.println("Bit Depth          : " + (bitDepth & 0xFF));
	System.out.println("Color Type         : " + (colorType & 0xFF));
	System.out.println("Compression Method : " + (compressionMethod & 0xFF));
	System.out.println("Filter Method      : " + (filterMethod & 0xFF));
	System.out.println("Interlace Method   : " + (interlaceMethod & 0xFF));
	System.out.println("----------------------------------------");
    }
}
