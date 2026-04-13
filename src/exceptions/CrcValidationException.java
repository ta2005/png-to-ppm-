package exceptions;

public class CrcValidationException extends Exception{
    public CrcValidationException(String mess){
	super(mess);
    }
}
