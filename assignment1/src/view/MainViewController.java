package view;

import global.exception.BadRequestException;
import validator.MenuSelectionValidator;

import java.util.Scanner;

public class MainViewController {

    private final Scanner scanner;
    private final MenuSelectionValidator menuSelectionValidator;

    public MainViewController(Scanner scanner, MenuSelectionValidator menuSelectionValidator) {
        this.scanner = scanner;
        this.menuSelectionValidator = menuSelectionValidator;
    }

    public void printMenu() {
        System.out.print("\n === 메인 화면으로 이동 === \n\n");

        System.out.println("""
                    0. 앱 종료
                    1. 노래 검색
                    2. 아티스트 검색
                    3. 내 정보
                    """);
    }

    public int selectMenu() {
        while(true) {
            System.out.print("원하는 작업의 번호를 입력하세요: ");
            String input = scanner.nextLine().trim();

            try {
                menuSelectionValidator.validate(input);
                return Integer.parseInt(input);

            } catch (BadRequestException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
