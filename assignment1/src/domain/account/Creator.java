package domain.account;

import java.time.LocalDateTime;

public class Creator extends User {

    private String description;
    private String facebookLink;
    private String instagramLink;
    private String XLink;
    private Boolean isVerified;

    public String getDescription() {
        return description;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public String getXLink() {
        return XLink;
    }

    private Creator() {}

    public Creator(String description) {
        this.description = description;
        this.isVerified = false;
    }
}