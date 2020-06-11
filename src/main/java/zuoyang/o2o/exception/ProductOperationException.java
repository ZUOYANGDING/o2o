package zuoyang.o2o.exception;

public class ProductOperationException extends RuntimeException{
    public static final long serialVersionUID = 7121336879786713556L;
    public ProductOperationException(String msg) {
        super(msg);
    }
}
