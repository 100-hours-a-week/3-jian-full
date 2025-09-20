package view;

import controller.MusicController;
import domain.content.Music;

import java.util.Scanner;

public class MusicViewController {

    private final Scanner scanner;

    private final MusicController musicController;

    public MusicViewController(Scanner scanner,  MusicController musicController) {
        this.scanner = scanner;
        this.musicController = musicController;
    }

    public void printProfile(Music music) {
        System.out.print("\n === 음악 화면으로 이동 === \n\n");
    }

    public void menu(Music music) throws InterruptedException {
        while (true) {
            System.out.println("""
                    0. 메인 화면으로 나가기
                    1. 음악 재생
                    2. 음악 중지
                    3. 플레이리스트에 음악 추가
                    """);

            System.out.print("원하는 작업의 번호를 입력하세요: ");
            int answer = Integer.parseInt(scanner.nextLine().trim());

            switch (answer) {
                case 0 -> {
                    return;
                }
                case 1 -> musicController.playMusic(music);
                case 2 -> musicController.stopMusic();
                case 3 -> System.out.println("현재 사용할 수 없는 기능입니다."); //playListController.addMusic(music);
                default -> System.out.println("유효하지 않은 입력값입니다. 다시 입력해주세요.");
            }
        }
    }
}
