package parser;

import java.util.List;
import chunk.*;
import exceptions.ParserException;

//the parsers job is to ensuere the order of the packages
//it is  a recursive desent parser that  the return the data in IDAT
public class Parser {
	private List<Chunk> l;
	private byte[] imageData = new byte[0];
	private byte[] colorPallete;
	private int current = 0;

	public Parser(List<Chunk> l) throws ParserException {
		this.l = l;
		header();
	}
	public byte[] getData(){
	    return imageData;
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

	private void header() throws ParserException{
	    //TODO:this the header parser  it has three mode depending on the color type
	    IHDR hdr = (IHDR)consume(ChunkType.IHDR);
	    // imageData=new byte[calcScanSize(hdr)*hdr.height()];
	    idat();
	}

	private void pallete() throws ParserException{
	    PLTE plt = (PLTE)consume(ChunkType.PLTE);
	    colorPallete=plt.data();    
	    idat();
	}

	private void idat() throws ParserException{
	    while(match(ChunkType.IDAT)){
		Chunk dat = consume(ChunkType.IDAT);
		byte[] result = new byte[dat.length() + imageData.length];
		System.arraycopy(imageData,0,result,0,imageData.length);
		System.arraycopy(dat.data(),0,result,imageData.length,dat.length());
		//    for (int i = 0; i < dat.length(); i++) {
		// System.out.printf("%02X", dat.data()[i] & 0xFF);
		//    }
		imageData=result;
	    }

	    iend();
	}

	private void iend() throws ParserException{
	    consume(ChunkType.IEND);
	    consume(ChunkType.EOF);
	}

	//this is the data in the IDAT

	private boolean match(ChunkType... t) {
	    for (var i : t) {
		if (check(i)) {
		    return true;
		}
	    }
	    return false;
	}

	private boolean isAtEnd() {
	    return peek().type() == ChunkType.EOF;
	}

	private Chunk peek() {
	    return l.get(current);
	}

	private boolean check(ChunkType type) {
	    if (isAtEnd() && type!=ChunkType.EOF)
		return false;
	    return peek().type() == type;
	}

	Chunk advance() {
	    if (!isAtEnd())
		current++;
	    return previous();
	}

	Chunk previous() {
	    return l.get(current - 1);
	}

	private Chunk consume(ChunkType type) throws ParserException {
	    if (check(type))
		return advance();
	    throw new ParserException("unexpected token "+type);

	}
}
