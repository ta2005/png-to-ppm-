package chunk;

public record IHDR(int length, int type, byte[] data, int crc,
		int width,
		int height,
		byte bitDepth,
		byte colorType,
		byte compressionMethod,
		byte filterMethod,
		byte interlaceMethod

) implements Chunk {
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
