package domain.content;

import java.time.LocalDate;

public class Media {

    String title;
    String filePath;
    String fileName;
    LocalDate createdAt;
    LocalDate updatedAt;

    public Media(String title, String filePath, String fileName) {
        this.title = title;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }
    public String getFilePath() { return filePath; }
    public String getFileName() { return fileName; }
}