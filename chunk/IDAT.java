package chunk;

import java.util.zip.Inflater;
import java.io.PrintStream;
import image.ImageFile;

final class IDAT extends Chunk{
    //i will hard code it the mometn i get and image i will fix it
    IDAT(Chunk c){
	super(c.length,c.type,c.data,c.crc);
    }
    void convert(ImageFile im){
	byte[] uncomp =  new byte[10000000];
	try {
	    Inflater decompresser = new Inflater();
	    decompresser.setInput(this.data);
	    decompresser.inflate(uncomp);
	    decompresser.end();
	} catch (java.util.zip.DataFormatException ex) {
	    System.out.println(ex.getMessage());
	}
	//im will then deploy a singlton patter making the image file a gloval object that im put the meta data in
	//or put an array list in it or smth
	String file = "wa.ppm";
	System.out.println("writing to a ifle");
	int width=im.h.width();
	int height=im.h.height();
	try(var f = new PrintStream(file)){
	    f.println("P3");
	    f.println(width+" "+height);
	    f.println(255);
	    int cursor=0;
	    for(int i=0;i<height;i++){
		int rowFilter = uncomp[cursor++] & 0xFF;
		System.out.println("the filter algo "+i+" "+rowFilter);

		// (If rowFilter > 0, you would normally have to do defiltering math here!)

		// 2. READ THE PIXELS
		for (int j = 0; j < width; j++) {
		    // Read the 4 bytes per pixel (Color Type 6)
		    int r = uncomp[cursor++] & 0xFF;
		    int g = uncomp[cursor++] & 0xFF;
		    int b = uncomp[cursor++] & 0xFF;
		    int a = uncomp[cursor++] & 0xFF; // We read the Alpha, but we ignore it for PPM

		    // P3 PPM wants plain text numbers separated by spaces
		    f.print(r + " " + g + " " + b + "  ");
		}
		f.println(); //
	    }
	}catch(Exception e){
	}
    }
}
