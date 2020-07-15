package zuoyang.o2o.exception;

public class WeChatAuthOperationException extends RuntimeException{
    private static final long serialVersionUID = -1840274670489089405L;
    public WeChatAuthOperationException (String msg) {
        super(msg);
    }
}
