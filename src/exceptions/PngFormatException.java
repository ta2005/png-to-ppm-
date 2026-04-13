package exceptions;

public class PngFormatException extends RuntimeException{
    public PngFormatException(String mess){
	super(mess);
    }
}
