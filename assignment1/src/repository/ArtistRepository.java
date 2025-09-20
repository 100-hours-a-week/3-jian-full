package repository;

import domain.account.Artist;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ArtistRepository {

    private static final Set<Artist> artistSet = new HashSet<>();

    public Artist save(Artist artist) {
        if (artistSet.add(artist)) return artist;
        throw new RuntimeException("Artist already exists");
    }

    public Optional<Artist> getByStageName(String stageName) {
        for (Artist artist : artistSet) {
            if (artist.getStageName().equalsIgnoreCase(stageName)) {
                return Optional.of(artist);
            }
        }
        return Optional.empty();
    }
}
