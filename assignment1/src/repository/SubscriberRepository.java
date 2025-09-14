package repository;

import domain.account.Subscriber;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SubscriberRepository {

    private static Set<Subscriber> subscriberSet = new HashSet<>();

    public Subscriber save(Subscriber subscriber) {
        subscriberSet.add(subscriber);
        return subscriber;
    }

    public Optional<Subscriber> getByEmail(String email) {
        for (Subscriber subscriber : subscriberSet) {
            if (subscriber.getEmail().equals(email)) {
                return Optional.of(subscriber);
            }
        }
        return Optional.empty();
    }
}
