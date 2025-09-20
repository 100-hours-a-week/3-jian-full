package controller;

import domain.account.Artist;
import domain.content.Music;
import infra.Mp3Player;
import javazoom.jl.decoder.JavaLayerException;
import service.MusicService;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class MusicController {

    private final Scanner scanner;

    private final Mp3Player mp3Player;
    private final MusicService musicService;

    public MusicController(Scanner scanner, MusicService musicService) {
        this.scanner = scanner;
        this.musicService = musicService;
        this.mp3Player = new Mp3Player();
    }

    public void playMusic(Music music) {
        String fullPath = music.getFilePath() + music.getFileName();

        try {
            mp3Player.startPlayer(fullPath);

        } catch (JavaLayerException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopMusic() {
        mp3Player.stopPlayer();
    }

    public Music searchByTitle() {
        System.out.print("노래의 제목을 입력하세요: ");
        String title = scanner.nextLine().trim();

        return musicService.searchByTitle(title);
    }

    public Music createMusic(Artist artist, String title, String filePath, String fileName) {
        return musicService.createMusic(artist, title, filePath, fileName);
    }
}
