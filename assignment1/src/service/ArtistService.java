package service;

import domain.account.Artist;
import repository.ArtistRepository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

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

        Map<String, String> result = new LinkedHashMap<>();

        result.put("활동명", artist.getStageName());
        result.put("대표 장르", artist.getGenre());
        result.put("소개", artist.getDescription());
        result.put("Instagram", artist.getInstagramLink());
        result.put("Facebook", artist.getFacebookLink());
        result.put("X", artist.getXLink());

        return result;
    }

    public Artist createArtist(String stageName, String genre, String description) {
        Artist artist = new Artist(stageName, genre, description);
        return artistRepository.save(artist);
    }
}
