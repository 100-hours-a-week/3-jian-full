package global.exception;

import global.constants.ErrorCode;

public class CustomRuntimeException extends RuntimeException {
    private final ErrorCode code;

    public CustomRuntimeException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
