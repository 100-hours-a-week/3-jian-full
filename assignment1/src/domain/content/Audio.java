package domain.content;

public class Audio extends Media {

    private Integer playTime;

    public Audio(String title, String filePath, String fileName) {
        super(title, filePath, fileName);
    }
}