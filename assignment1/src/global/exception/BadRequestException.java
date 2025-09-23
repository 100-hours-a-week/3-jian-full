package global.exception;

import global.constants.ErrorCode;

public class BadRequestException extends CustomRuntimeException {
    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
