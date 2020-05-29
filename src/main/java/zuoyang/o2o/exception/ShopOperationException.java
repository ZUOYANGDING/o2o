package zuoyang.o2o.exception;

public class ShopOperationException extends RuntimeException{
    private static final long serialVersionUID = -2581088828582909141L;
    public ShopOperationException(String msg) {
        super(msg);
    }
}
