package repository;

import domain.account.Artist;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ArtistRepository {

    private final Set<Artist> artistSet = ConcurrentHashMap.newKeySet();

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
