package decoder;

import chunk.IHDR;
import chunk.PLTE;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.zip.Inflater;

public class Decoder {
    private byte[] imageData;
    private IHDR h;
    // this one can be null
    final int bpp;
    public Decoder(byte[] compressedData, IHDR h) {
        this.h = h;
        this.imageData = new byte[calcScanSize(h) * h.height()];

        try {
            Inflater decompresser = new Inflater();
            decompresser.setInput(compressedData);
            decompresser.inflate(imageData);
            decompresser.end();
        } catch (java.util.zip.DataFormatException ex) {
            System.out.println(ex.getMessage());
        }
        int channels = switch (h.colorType()) {
            case 0 -> 1; // Grayscale
            case 2 -> 3; // Truecolor (RGB)
            case 3 -> 1; // Indexed (Palette)
            case 4 -> 2; // Grayscale + Alpha
            case 6 -> 4; // Truecolor + Alpha
            default -> throw new IllegalStateException("Invalid color type");
        };
        bpp = Math.max(1, channels * h.bitDepth() / 8);

        // ya men3ash
        // interlace();
        filter();
    }

    // this will be passed to a final image reader
    public byte[] getImage() { return imageData; }

    private void filterLine(byte[] curr, byte[] prev,
                            BiConsumer<byte[], byte[]> filterMeth) {
        // the first byte is for the type of the filter and i can't do curr++ in
        // java well their loss
        for (int i = 1; i < curr.length; i++) {
            // filterMeth.apply(curr,
        }
    }
    private void filter() {
	int scanSize = calcScanSize(h);
        byte[] prevScanLine = new byte[scanSize];
        byte[] currScanLine = new byte[scanSize];
        List<BiConsumer<byte[], byte[]>> filterMethods = List.of(
            (curr, prev)
                -> {},
            (curr, prev)
                -> {
                for (int i = 1; i < curr.length; i++) {
                    int b = (i > bpp) ? (curr[i - bpp] & 0xFF) : 0;
                    curr[i] = (byte)(curr[i] + b);
                }
            },
            (curr, prev)
                -> {
                for (int i = 1; i < curr.length; i++) {
                    int a = (i > bpp) ? (prev[i] & 0xFF) : 0;
                    curr[i] = (byte)(curr[i] + a);
                }
            },
            (curr, prev)
                -> {
                for (int i = 1; i < curr.length; i++) {
                    int a = (i > bpp) ? (curr[i - bpp] & 0xFF) : 0;
                    int b = (prev[i] & 0xFF);
                    curr[i] = (byte)(curr[i] + (a+b)/2);
                }
            },
            (curr, prev) -> {
                // this is the code of the spec

                for (int i = 1; i < curr.length; i++) {
                    int a = (i > bpp) ? (curr[i - bpp] & 0xFF) : 0;
                    int b = (prev[i] & 0xFF);
                    int c = (i > bpp) ? (prev[i - bpp] & 0xFF) : 0;
                    int p = a + b - c;
                    int pa = Math.abs(p - a);
                    int pb = Math.abs(p - b);
                    int pc = Math.abs(p - c);
		    int pred;
                    if (pa <= pb && pa <= pc) {
			pred=a;
                    } else if (pb <= pc) {
			pred=b;
                    } else {
			pred=c;
                    }
		    curr[i]=(byte)(pred+curr[i]);
                }
            });
        for (int i = 0; i < h.height(); i++) {
	    int offset=scanSize*i;
	    System.arraycopy(imageData, offset, currScanLine, 0, scanSize);
            // then i will either have an array of 5 element and the
            // method to use is simply arr[currScanLine[0]]
	    filterMethods.get(currScanLine[0]&0xFF).accept(currScanLine,prevScanLine);
	    System.arraycopy(currScanLine, 0, imageData, offset, scanSize);
	    System.arraycopy(currScanLine,0,prevScanLine,0,scanSize);
        }
    }

    private int calcScanSize(IHDR h) {
        int bitsPerChannel = switch (h.colorType()) {
            case 0 -> h.bitDepth();
            case 2 -> 3 * h.bitDepth();
            case 4 -> 2 * h.bitDepth();
            case 6 -> 4 * h.bitDepth();
            default -> 0;
        };
        return 1 + (bitsPerChannel * h.width() + 7) / 8;
    }
}
