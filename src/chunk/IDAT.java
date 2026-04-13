package chunk;

import java.util.zip.Inflater;
import java.io.PrintStream;
import image.ImageFile;

public record IDAT(int length, ChunkType type, byte[] data, int crc) implements Chunk {
    // i will hard code it the mometn i get and image i will fix it
    private int calcScanSize(ImageFile im) {
	int bitsPerChannel = switch (im.h.colorType()) {
	    case 0 -> im.h.bitDepth();
	    case 2 -> 3 * im.h.bitDepth();
	    case 4 -> 2 * im.h.bitDepth();
	    case 6 -> 4 * im.h.bitDepth();
	    default -> 0;
	};
	return 1 + (bitsPerChannel * im.h.width() + 7) / 8;
    }

    public void convert(ImageFile im) {
	byte[] uncomp = new byte[calcScanSize(im) * im.h.height()];
	try {
	    Inflater decompresser = new Inflater();
	    decompresser.setInput(this.data);
	    decompresser.inflate(uncomp);
	    decompresser.end();
	} catch (java.util.zip.DataFormatException ex) {
	    System.out.println(ex.getMessage());
	}
	// im will then deploy a singlton patter making the image file a gloval object
	// that im put the meta data in
	// or put an array list in it or smth
	String file = "wa.ppm";
	System.out.println("writing to a file");
	int width = im.h.width();
	int height = im.h.height();
	try (var f = new PrintStream(file)) {
	    f.println("P3");
	    f.println(width + " " + height);
	    f.println(255);
	    int cursor = 0;
	    byte[] prevScan = new byte[calcScanSize(im)];
	    for (int i = 0; i < height; i++) {
		int rowFilter = uncomp[cursor++] & 0xFF;
		uncomp[cursor - 1] = 0;
		System.out.println("the filter algo " + i + " " + rowFilter);

		// (If rowFilter > 0, you would normally have to do defiltering math here!)

		// 2. READ THE PIXELS
		//
		for (int j = 0; j < width; j++) {
		    // Defilter and read Red (looks back 4 bytes to the previous pixel's Red)
		    // Note: You must handle the first pixel of the row differently because there is
		    // no previous pixel!
		    if (j > 0) {
			uncomp[cursor] = (byte) (uncomp[cursor] + uncomp[cursor - 4]);
		    }
		    int r = uncomp[cursor++] & 0xFF;

		    // Defilter and read Green
		    if (j > 0)
			uncomp[cursor] = (byte) (uncomp[cursor] + uncomp[cursor - 4]);
		    int g = uncomp[cursor++] & 0xFF;

		    // Defilter and read Blue
		    if (j > 0)
			uncomp[cursor] = (byte) (uncomp[cursor] + uncomp[cursor - 4]);
		    int b = uncomp[cursor++] & 0xFF;

		    // Defilter and read Alpha (we discard it for PPM, but it MUST be defiltered for
		    // the next pixel to use it!)
		    if (j > 0)
			uncomp[cursor] = (byte) (uncomp[cursor] + uncomp[cursor - 4]);
		    int a = uncomp[cursor++] & 0xFF;

		    f.print(r + " " + g + " " + b + "  ");
		}
		f.println(); //
	    }
	} catch (Exception e) {
	}
    }
}
