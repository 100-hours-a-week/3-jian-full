package service;

import domain.account.Artist;
import domain.content.Music;
import repository.MusicRepository;

import java.util.Optional;

public class MusicService {

    private final MusicRepository musicRepository =  new MusicRepository();

    public Music searchByTitle(String title) {
        Optional<Music> music = musicRepository.getByTitle(title);

        if (music.isEmpty()) {
            System.out.println("노래를 찾을 수 없습니다.\n");
            return null;
        }
        return music.get();
    }

    public Music createMusic(Artist artist, String title, String filePath, String fileName) {
        Music music = new Music(artist, title, filePath, fileName);
        return musicRepository.save(music);
    }
}
