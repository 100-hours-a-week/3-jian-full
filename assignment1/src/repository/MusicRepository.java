package repository;

import domain.content.Music;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MusicRepository {

    private static final Set<Music> musicSet = new HashSet<>();

    public Music save(Music music) {
        if (musicSet.add(music)) return music;
        throw new RuntimeException("Music already exists");
    }

    public Optional<Music> getByTitle(String title) {
        for (Music music : musicSet) {
            if (music.getTitle().equalsIgnoreCase(title)) {
                return Optional.of(music);
            }
        }
        return Optional.empty();
    }
}
