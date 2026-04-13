package exceptions;

public class ParserException extends Exception{
    public ParserException(String mess){
	super(mess);
    }
}
// TODO:PngSignatureException: The very first 8 bytes of a PNG are a magic number (89 50 4E 47 0D 0A 1A 0A). If a user passes a JPEG renamed to .png, it will fail this check. Throw this before you even start reading chunks.
//
// CrcValidationException: You are reading the CRC right now, but not checking it. When you implement the math, you'll want to throw this if calculatedCrc != readCrc. It means the file was corrupted during download.
//
// PngFormatException: Replace all your IllegalArgumentException calls with this. If IHDR has a length of 14, or an invalid color type, throw this.
//
// UnsupportedFeatureException: You mentioned you aren't doing interlacing yet. Instead of letting the code break later, inside your IHDR constructor you can do: if(interlaceMethod == 1) throw new UnsupportedFeatureException("Interlacing not supported yet");.
