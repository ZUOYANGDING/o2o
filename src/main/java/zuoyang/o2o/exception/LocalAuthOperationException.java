package zuoyang.o2o.exception;

import java.io.Serializable;

public class LocalAuthOperationException extends RuntimeException {
    public static final long serialVersionUID = 3252947222469072277L;

    public LocalAuthOperationException(String msg) {
        super(msg);
    }
}
