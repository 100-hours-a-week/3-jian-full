package repository;

import domain.content.Music;

import java.util.Optional;
import java.util.Set;

public class MusicRepository {

    private static Set<Music> musicSet = Set.of(
            new Music("Abracadabra"),
            new Music("APT.")

    );

    public Optional<Music> getByTitle(String title) {
        for (Music music : musicSet) {
            if (music.getTitle().equalsIgnoreCase(title)) {
                return Optional.of(music);
            }
        }
        return Optional.empty();
    }
}
