package controller;

import domain.content.Music;
import service.MusicService;

import java.util.List;
import java.util.Scanner;

public class MusicController {

    private final Scanner scanner;

    private final MusicService musicService;

    public MusicController(Scanner scanner, MusicService musicService) {
        this.scanner = scanner;
        this.musicService = musicService;
    }

    public void playMusic(Music music) throws InterruptedException {
        System.out.println(music.getTitle() + "이(가) 재생 중입니다.");

        List<String> lyrics = music.getLyrics();
        if (lyrics == null || lyrics.isEmpty()) {
            System.out.println(music.getTitle() + "의 가사가 지원되지 않습니다.");
            return;
        }

        for (String lyric : lyrics) {
            System.out.println(lyric);
            Thread.sleep(500); // 줄 사이에 0.5초 대기
        }
    }

    public Music searchByTitle() {
        System.out.print("노래의 제목을 입력하세요: ");
        String title = scanner.nextLine().trim();

        return musicService.searchByTitle(title);
    }
}
