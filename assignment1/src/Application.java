import domain.account.Artist;
import domain.account.Subscriber;
import domain.content.Music;
import service.ArtistService;
import service.MusicService;
import service.SubscriberService;

import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class Application {

    private static SubscriberService subscriberService;
    private static MusicService musicService;
    private static ArtistService artistService;
    
    private static Scanner scanner;

    public static void main(String[] args) {
        subscriberService = new SubscriberService();
        musicService = new MusicService();
        artistService = new ArtistService();
        scanner = new Scanner(System.in);

        // 첫 화면
        System.out.print(" === 음악 스트리밍 서비스 앱 시작 === \n\n");

        // 가입
        Subscriber subscriber;

        while(true) {
            System.out.print("회원가입을 진행하시겠습니까? (y/n): ");
            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("y")) {
                System.out.print("이름을 입력해주세요: ");
                String name = scanner.nextLine().trim();

                System.out.print("이메일을 입력해주세요: ");
                String email = scanner.nextLine().trim();

                System.out.print("비밀번호를 입력해주세요: ");
                String password = scanner.nextLine().trim();

                subscriber = subscriberService.signUp(name, email, password);
                break;

            } else if (answer.equalsIgnoreCase("n")) {
                System.out.print("이메일을 입력해주세요: ");
                String email = scanner.nextLine().trim();

                System.out.print("비밀번호를 입력해주세요: ");
                String password = scanner.nextLine().trim();

                subscriber = subscriberService.signIn(email, password);
                break;

            } else {
                System.out.println("유효하지 않은 입력값입니다. 다시 입력해주세요.");
            }
        }

        if (subscriber == null) {
            System.out.println("사용자 정보가 존재하지 않습니다. 앱을 종료합니다.");
            exit(0);
        }

        while (true) {
            System.out.print(" === 메인 페이지로 이동 === \n\n");

            System.out.println("""
                    0. 앱 종료
                    1. 노래 검색
                    2. 아티스트 검색
                    3. 내 정보
                    """);
            System.out.print("원하는 작업의 번호를 입력하세요: ");
            int answer = Integer.parseInt(scanner.nextLine().trim());

            switch (answer) {
                case 0:
                    System.out.println("앱을 종료합니다.");
                    exit(0);

                case 1:
                    System.out.print("노래의 제목을 입력하세요: ");
                    String title = scanner.nextLine().trim();

                    Music music = musicService.searchByTitle(title);
                    if (music == null) {
                        System.out.println();
                        break;
                    }

                    System.out.print("노래를 재생하시겠습니까? (y/n): ");
                    String answer2 = scanner.nextLine().trim();

                    if (answer2.equalsIgnoreCase("y")) {
                        System.out.println(music.getTitle() + "이(가) 재생 중입니다.\n");

                    } else if (answer2.equalsIgnoreCase("n")) {
                        System.out.println("작업 선택 화면으로 돌아갑니다.\n");

                    } else {
                        System.out.println("유효하지 않은 입력값입니다. 다시 입력해주세요.\n");
                    }

                    break;

                case 2:
                    System.out.print("아티스트 이름을 입력하세요: ");
                    String stageName = scanner.nextLine().trim();

                    Artist artist = artistService.searchByStageName(stageName);
                    if (artist == null) {
                        break;
                    }

                    Map<String, String> artistProfile = artistService.getArtistProfile(artist);

                    // 프로필 출력
                    System.out.print(" === 아티스트 홈 페이지로 이동 === \n\n");
                    printProfile(artistProfile);
                    break;

                case 3:
                    Map<String, String> myProfile = subscriberService.getMyProfile(subscriber);

                    // 프로필 출력
                    System.out.print(" === 마이 홈 페이지로 이동 === \n\n");
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
        System.out.println(stringBuilder);
    }
}