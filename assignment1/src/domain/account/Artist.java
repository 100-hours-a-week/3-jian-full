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

    public Artist(
            String description,
            String facebookLink, String instagramLink, String XLink,
            String stageName, String genre
    ) {
        super(description, facebookLink, instagramLink, XLink);
        this.stageName = stageName;
        this.genre = genre;
    }
}