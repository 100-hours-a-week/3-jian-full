package service;

import domain.account.Artist;
import repository.ArtistRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArtistService {

    private final ArtistRepository artistRepository =  new ArtistRepository();

    public Artist searchByStageName(String stageName) {
        Optional<Artist> artist = artistRepository.getByStageName(stageName);

        if (artist.isEmpty()) {
            System.out.println("아티스트를 찾을 수 없습니다.\n");
            return null;
        }
        return artist.get();
    }

    public Map<String, String> getArtistProfile(Artist artist) {
        if (artist == null) {
            return Map.of("error", "아티스트의 정보가 존재하지 않습니다.");
        }

        Map<String, String> result = new HashMap<>();

        result.put("stageName", artist.getStageName());
        result.put("genre", artist.getGenre());
        result.put("description", artist.getDescription());
        result.put("instagram", artist.getInstagramLink());
        result.put("facebook", artist.getFacebookLink());
        result.put("X", artist.getXLink());

        return result;
    }
}
