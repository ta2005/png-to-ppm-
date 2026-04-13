package chunk;

public enum ChunkType {
    IHDR(0x49484452), 
    PLTE(0x504C5445), 
    IDAT(0x49444154), 
    IEND(0x49454E44),
    UNKNOWN_CRITICAL(0xFFFFFFFF),
    UNKNOWN_AUXILARY(0x00000000),
    EOF(0x000000001);
    private int code;
    private ChunkType(int code){
	this.code=code;
    }
    public int code(){
	return code;
    }
    public static ChunkType fromInt(int code){
	for(var i: values()){
	    if(i.code==code){
		return i;	
	    }
	}
	int  critBit = (code&32);
	if(critBit!=0){
	    return UNKNOWN_CRITICAL;
	}else{
	    return UNKNOWN_AUXILARY;
	}
    }
}
