package global.constants;

public enum ErrorCode {
    BAD_REQUEST(4000);

    private final int value;

    ErrorCode(int errorCode) {
        this.value = errorCode;
    }
}
