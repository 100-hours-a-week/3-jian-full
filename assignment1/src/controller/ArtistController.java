package controller;

import domain.account.Artist;
import service.ArtistService;

import java.util.Map;
import java.util.Scanner;

public class ArtistController {

    private final Scanner scanner;

    private final ArtistService artistService;

    public ArtistController(Scanner scanner, ArtistService artistService) {
        this.scanner = scanner;
        this.artistService = artistService;
    }

    public Map<String, String> searchArtistProfile() {
        System.out.print("아티스트 이름을 입력하세요: ");
        String stageName = scanner.nextLine().trim();

        Artist artist = artistService.searchByStageName(stageName);
        return artistService.getArtistProfile(artist);
    }

    public Artist createArtist(String stageName, String genre, String description) {
        return artistService.createArtist(stageName, genre, description);
    }
}
