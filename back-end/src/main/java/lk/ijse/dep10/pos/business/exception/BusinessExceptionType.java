package lk.ijse.dep10.pos.business.exception;

public enum BusinessExceptionType {
    BUSINESS(1000, "Business Exception"),
    DUPLICATE_RECORD(1001, "Duplicate Entry Found"),
    INTEGRITY_VIOLATION(1002, "Integrity Violation"),
    RECORD_NOT_FOUND(1003, "Record Not Found");

    private final int code;
    private final String message;

    BusinessExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
