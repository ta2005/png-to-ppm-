package chunk;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import image.ImageFile;

public sealed interface Chunk permits IHDR, IEND, IDAT, PLTE {
	// this is the length of the data of the chuck
	int length();

	ChunkType type();

	byte[] data();

	int crc();

	public static List<Chunk> chunkTokenizer(ImageFile i) throws IllegalArgumentException {
		// i know this is inefficent but will handel it later
		List<Chunk> l = new ArrayList<>();
		for (int j = 0; j < 8; j++) {
			System.out.printf("%02X ", i.data[j] & 0xFF);
		}
		i.cursor += 8;
		while (i.cursor < i.data.length) {
			int length = toInt(Arrays.copyOfRange(i.data, i.cursor, i.cursor + 4));
			i.cursor += 4;
			int type = toInt(Arrays.copyOfRange(i.data, i.cursor, i.cursor + 4));
			i.cursor += 4;
			byte[] data = Arrays.copyOfRange(i.data, i.cursor, i.cursor + length);
			i.cursor += length;
			int crc = toInt(Arrays.copyOfRange(i.data, i.cursor, i.cursor + 4));
			i.cursor += 4;
			l.add(switch (ChunkType.fromInt(type)) {
				case IHDR -> {
					int cursor = 0;
					int width = toInt(Arrays.copyOfRange(data, cursor, cursor + 4));
					cursor += 4;
					int height = toInt(Arrays.copyOfRange(data, cursor, cursor + 4));
					cursor += 4;
					byte bitDepth = data[cursor++];
					byte colorType = data[cursor++];
					byte compressionMethod = data[cursor++];
					byte filterMethod = data[cursor++];
					byte interlaceMethod = data[cursor++];
					yield new IHDR(length, ChunkType.fromInt(type), data, crc, width, height, bitDepth, colorType,
							compressionMethod,
							filterMethod, interlaceMethod);
				}
				case IDAT -> new IDAT(length, ChunkType.fromInt(type), data, crc);
				case IEND -> new IEND(length, ChunkType.fromInt(type), data, crc);
				case PLTE -> new PLTE(length, ChunkType.fromInt(type), data, crc);
				case UNKNOWN_CRITICAL -> {
					System.err.println("Unkown Critical chunk " + getName(type));
					throw new RuntimeException("Unknown Critical chunk: " + getName(type));
				}
				default -> null;
			});
		}
		//this is just a hack that i may or may not chagne
		l.add(new IEND(0,ChunkType.fromInt(0x00000001),new byte[0],0));
		return l;
	}

	public static int toInt(byte[] i) {
		// System.out.println(Arrays.toString(i));
		int res = i[3] & 0xFF | (i[2] & 0xFF) << 8 | (i[1] & 0xFF) << 16 | (i[0] & 0xFF) << 24;
		return res;
	}

	public static String getName(int type) {
		return "" + (char) ((type >> 24) & 0xFF) +
				(char) ((type >> 16) & 0xFF) +
				(char) ((type >> 8) & 0xFF) +
				(char) (type & 0xFF);
	}

	default void print() {
		int type = this.type().code();
		int length = this.length();
		byte[] data = this.data();
		int crc = this.crc();
		String typeName = "" +
				(char) ((type >> 24) & 0xFF) +
				(char) ((type >> 16) & 0xFF) +
				(char) ((type >> 8) & 0xFF) +
				(char) (type & 0xFF);

		System.out.println("----------------------------------------");
		System.out.println("Chunk Type : " + typeName);
		System.out.println("Length     : " + length + " bytes");

		System.out.print("Data (Hex) : ");
		int displayLimit = Math.min(data.length, 8);
		for (int i = 0; i < displayLimit; i++) {
			System.out.printf("%02X ", data[i] & 0xFF);
		}
		if (data.length > 8) {
			System.out.print("... (" + (data.length - 8) + " more bytes)");
		}

		System.out.printf("\nCRC        : %08X \n", crc);
	}
}
