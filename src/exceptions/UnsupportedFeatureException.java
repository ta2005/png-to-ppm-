package exceptions;

public class UnsupportedFeatureException extends RuntimeException{
    public UnsupportedFeatureException(String mess){
	super(mess);
    }
}
