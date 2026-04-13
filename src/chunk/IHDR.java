package chunk;

import exceptions.PngFormatException;
import exceptions.UnsupportedFeatureException;

public record IHDR(int length, ChunkType type, byte[] data, int crc,
		int width,
		int height,
		byte bitDepth,
		byte colorType,
		byte compressionMethod,
		byte filterMethod,
		byte interlaceMethod

) implements Chunk {
	public IHDR {
		if (length != 13) {
			throw new PngFormatException("header length must be 13");
		} else if (compressionMethod != 0) {
			throw new PngFormatException("invalid compression Method");
		} else if (filterMethod != 0) {
			throw new PngFormatException("invalid filter method");
		} else if (!(interlaceMethod == 0 || interlaceMethod == 1)) {
			throw new PngFormatException("invalid interlace method");
		}
		if (interlaceMethod == 1) {
			throw new UnsupportedFeatureException("Interlacing (Adam7) is not supported yet.");
		}
		boolean validColor = switch (colorType) {
			case 0 -> (bitDepth == 1 || bitDepth == 2 || bitDepth == 4 || bitDepth == 8 || bitDepth == 16); // Grayscale
			case 2, 4, 6 -> (bitDepth == 8 || bitDepth == 16); // Truecolor, Grayscale+Alpha, Truecolor+Alpha
			case 3 -> (bitDepth == 1 || bitDepth == 2 || bitDepth == 4 || bitDepth == 8); // Indexed-color (Palette)
			default -> false;
		};

		if (!validColor) {
			throw new PngFormatException(
					"Invalid combination of color type (" + colorType + ") and bit depth (" + bitDepth + ").");
		}
		// to add check colortype and bit depth match
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
