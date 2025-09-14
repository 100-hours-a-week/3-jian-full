package domain.content;

import java.time.LocalDate;

public class Media {

    String title;
    Integer size;

    LocalDate createdAt;
    LocalDate updatedAt;

    public Media(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}