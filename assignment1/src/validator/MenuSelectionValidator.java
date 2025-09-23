package validator;

import global.constants.ErrorCode;
import global.exception.BadRequestException;

public class MenuSelectionValidator implements InputValidator {

    public void validate(String input) {
        // 양의 정수
        if (!input.matches("^[0-9]+$")) {
            throw new BadRequestException(
                    ErrorCode.BAD_REQUEST,
                    "메뉴 번호를 숫자로 입력해주세요."
            );
        }
    }
}
