package repository;

import domain.account.Subscriber;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SubscriberRepository {

    private final Set<Subscriber> subscriberSet = ConcurrentHashMap.newKeySet();

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
