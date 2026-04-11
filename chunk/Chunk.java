package chunk;

import java.util.Arrays;
import image.ImageFile;

sealed public class Chunk permits IHDR, IEND, IDAT {
	// this is the length of the data of the chuck
	int length;
	int type;
	byte data[];
	int crc;
	private static IHDR h = null;

	protected Chunk(ImageFile i) {
		// i know this is inefficent but will handel it later
		this.length = toInt(Arrays.copyOfRange(i.data, i.cursor, i.cursor + 4));
		i.cursor += 4;
		this.type = toInt(Arrays.copyOfRange(i.data, i.cursor, i.cursor + 4));
		i.cursor += 4;
		this.data = Arrays.copyOfRange(i.data, i.cursor, i.cursor + this.length);
		i.cursor += this.length;
		this.crc = toInt(Arrays.copyOfRange(i.data, i.cursor, i.cursor + 4));
		i.cursor += 4;
	}

	protected Chunk(int length, int type, byte data[], int crc) {
		this.length = length;
		this.type = type;
		this.data = data;
		this.crc = crc;
	}

	static public Chunk of(ImageFile i) {
		Chunk res = new Chunk(i);
		return switch (Chunk.getName(res.type)) {
			case "IHDR" -> {
				h = new IHDR(res);
				yield h;
			}
			case "IDAT" -> new IDAT(res,h);
			default -> res;
		};
	}

	protected int toInt(byte[] i) {
		// System.out.println(Arrays.toString(i));
		int res = i[3] & 0xFF | (i[2] & 0xFF) << 8 | (i[1] & 0xFF) << 16 | (i[0] & 0xFF) << 24;
		return res;
	}
	public int type(){
	    return type;
	}
	static public String getName(int type) {
		return "" + (char) ((type >> 24) & 0xFF) +
				(char) ((type >> 16) & 0xFF) +
				(char) ((type >> 8) & 0xFF) +
				(char) (type & 0xFF);
	}

	public void print() {
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
