package repository;

import domain.account.Artist;

import java.util.Optional;
import java.util.Set;

public class ArtistRepository {

    private static final Set<Artist> artistSet = Set.of(
            new Artist("아카데미, 그래미 수상자이자 솔로 가수인 레이디 가가입니다.",
                    "facebook.com/ladygaga",
                    "instagram.com/ladygaga",
                    "X.com/ladygaga",
                    "Lady Gaga", "Pop"),
            new Artist("그래미 수상자이자 솔로 가수인 브루노 마스입니다.",
                    "facebook.com/brunomars",
                    "instagram.com/brunomars",
                    "X.com/brunomars",
                    "Bruno Mars", "R&B")
    );

    public Optional<Artist> getByStageName(String stageName) {
        for (Artist artist : artistSet) {
            if (artist.getStageName().equalsIgnoreCase(stageName)) {
                return Optional.of(artist);
            }
        }
        return Optional.empty();
    }
}
