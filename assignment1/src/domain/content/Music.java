package domain.content;

import domain.account.Artist;

import java.time.LocalDate;
import java.util.List;

public class Music extends Audio {

    private Artist artist;
    private String composer;
    private String publisher;
    private String genre;
    private LocalDate releaseDate;
    private List<String> lyrics;

    public Music(Artist artist, String title, String filePath, String fileName) {
        super(title, filePath, fileName);
        this.artist = artist;
    }

    public List<String> getLyrics() {
        return lyrics;
    }
}