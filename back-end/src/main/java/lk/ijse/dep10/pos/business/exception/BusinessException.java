package lk.ijse.dep10.pos.business.exception;

public class BusinessException extends RuntimeException {
    private final BusinessExceptionType type;

    public BusinessExceptionType getType() {
        return type;
    }

    public BusinessException(String message) {
        this(BusinessExceptionType.BUSINESS, message);
    }

    public BusinessException(Throwable cause) {
        this(BusinessExceptionType.BUSINESS, cause);
    }

    public BusinessException(BusinessExceptionType type) {
        this(type, type.getMessage());
    }

    public BusinessException(BusinessExceptionType type, String message) {
        super("Code: " + type.getCode() +"; " + message);
        this.type = type;
    }

    public BusinessException(BusinessExceptionType type, Throwable cause) {
        this(type, type.getMessage(), cause);
    }

    public BusinessException(BusinessExceptionType type, String message, Throwable cause) {
        super("Code: " + type.getCode() +"; " + message, cause);
        this.type = type;
    }
}
