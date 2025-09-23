import controller.ArtistController;
import controller.MusicController;
import controller.SubscriberController;
import domain.account.Artist;
import domain.account.Subscriber;
import domain.content.Music;
import repository.ArtistRepository;
import repository.MusicRepository;
import repository.SubscriberRepository;
import service.ArtistService;
import service.MusicService;
import service.SubscriberService;
import validator.MenuSelectionValidator;
import view.MainViewController;
import view.MusicViewController;

import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class Application {
    
    private static Scanner scanner;

    private static SubscriberService subscriberService;
    private static SubscriberController subscriberController;
    private static ArtistController artistController;
    private static MusicController musicController;

    private static MainViewController mainViewController;
    private static MusicViewController musicViewController;

    private static void init() {
        scanner = new Scanner(System.in);

        ArtistService artistService = new ArtistService(new ArtistRepository());
        MusicService musicService = new MusicService(new MusicRepository());
        subscriberService = new SubscriberService(new SubscriberRepository());

        subscriberController = new SubscriberController(scanner, subscriberService);
        artistController = new ArtistController(scanner, artistService);
        musicController = new MusicController(scanner, musicService);

        mainViewController = new MainViewController(scanner, new MenuSelectionValidator());
        musicViewController = new MusicViewController(scanner, musicController);
    }

    public static void main(String[] args) throws InterruptedException {
        // 생성자 주입
        init();

        // repository 초기화
        Artist artist1 = artistController.createArtist(
                "Lite Saturation",
                "Dance",
                "Hello, this is Lite Saturation.");
        Artist artist2 = artistController.createArtist(
                "Mr Smith",
                "Blues",
                "We play blues music.");

        Music music1 = musicController.createMusic(
                artist1,
                "Dance Party",
                "/Users/gy/Study/3-jian-full/assignment1/resources/music/",
                "Lite Saturation - Dance Party.mp3"
        );
        Music music2 = musicController.createMusic(
                artist2,
                "Moonshine Blues",
                "/Users/gy/Study/3-jian-full/assignment1/resources/music/",
                "Mr Smith - Moonshine Blues.mp3"
        );

        // 시작 화면
        System.out.print("\n === 음악 스트리밍 서비스 앱 시작 === \n\n");

        // 회원가입 및 로그인
        Subscriber subscriber;

        while(true) {
            System.out.print("회원가입을 진행하시겠습니까? (y/n): ");
            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("y")) {
                subscriber = subscriberController.signUp();
                break;

            } else if (answer.equalsIgnoreCase("n")) {
                subscriber = subscriberController.signIn();
                break;

            } else {
                System.out.println("유효하지 않은 입력값입니다. 다시 입력해주세요.");
            }
        }

        if (subscriber == null) {
            System.out.println("사용자 정보가 존재하지 않습니다. 앱을 종료합니다.");
            exit(0);
        }

        // 메인 화면
        while (true) {
            mainViewController.printMenu();
            int answer = mainViewController.selectMenu();

            switch (answer) {
                case 0:
                    System.out.print("\n === 음악 스트리밍 서비스 앱 종료 === \n\n");
                    exit(0);

                case 1:
                    Music music = musicController.searchByTitle();
                    if (music == null) {
                        System.out.println();
                        break;
                    }

                    // 음악 화면
                    musicViewController.printProfile(music);
                    musicViewController.menu(music);

                    break;

                case 2:
                    Map<String, String> artistProfile = artistController.searchArtistProfile();

                    // 아티스트 홈
                    System.out.print("\n === 아티스트 홈으로 이동 === \n\n");
                    printProfile(artistProfile);
                    break;

                case 3:
                    Map<String, String> myProfile = subscriberService.getSubscriberProfile(subscriber);

                    // 마이 홈
                    System.out.print("\n === 마이 홈으로 이동 === \n\n");
                    printProfile(myProfile);
                    break;

                default:
                    System.out.println("유효하지 않은 입력값입니다. 다시 입력해주세요.\n");
            }
        }
    }

    private static void printProfile(Map<String, String> profile) {
        StringBuilder stringBuilder = new StringBuilder();
        for  (Map.Entry<String, String> entry : profile.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        System.out.print(stringBuilder);
    }
}