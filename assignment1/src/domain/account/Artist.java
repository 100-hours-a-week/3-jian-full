package domain.account;

import java.time.LocalDateTime;

public class Artist extends Creator {

    private String stageName;
    private String genre;

    public String getStageName() {
        return stageName;
    }

    public String getGenre() {
        return genre;
    }

    public Artist(String stageName, String genre, String description) {
        super(description);
        this.stageName = stageName;
        this.genre = genre;
    }
}