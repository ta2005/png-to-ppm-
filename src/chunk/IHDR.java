package chunk;

import exceptions.TokenizationException;

public record IHDR(int length, ChunkType type, byte[] data, int crc,
		int width,
		int height,
		byte bitDepth,
		byte colorType,
		byte compressionMethod,
		byte filterMethod,
		byte interlaceMethod

) implements Chunk {
	public IHDR{
	    if (length != 13){
		throw new IllegalArgumentException("header length must be 13");
	    }else if(compressionMethod != 0){
		throw new IllegalArgumentException("invalid compression Method");
	    }else if(filterMethod!=0){
		throw new IllegalArgumentException("invalid filter method");
	    }else if(!(interlaceMethod==0 || interlaceMethod==1)){
		throw new IllegalArgumentException("invalid interlace method");
	    }
	    //to add check colortype and bit depth match
	}
	@Override
	public void print() {
		Chunk.super.print();
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
