import image.ImageFile;
//once i parse all the chuncks i will move them to seperate files
enum ChunckType{
    IHDR(0x49484452),IDAT(0x49444154),IEND(0x49454E44);
    int code;
    private ChunckType(int code){
	this.code=code;
    }
}



public class Main{
    public static void main(String [] args){
	var ImageFile = new ImageFile("Screenshot 2026-02-15 at 15-32-31 Status - Codeforces.png");
	ImageFile.show();
    }
}
