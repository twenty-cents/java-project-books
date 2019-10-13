public class BookStatisticsException extends Exception {

    private Integer errorCode;


    public BookStatisticsException(String message) {
        super(message);
    }

    public BookStatisticsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookStatisticsException(String message, Throwable cause, ErrorCodes errorCode) {
        super(message, cause);
        this.errorCode = errorCode.getCode();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
