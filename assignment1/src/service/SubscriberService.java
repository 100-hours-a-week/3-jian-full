package service;

import domain.account.Subscriber;
import domain.subscription.SubscriptionPlan;
import repository.SubscriberRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SubscriberService {

    private final SubscriberRepository subscriberRepository =  new SubscriberRepository();

    public Subscriber signUp(String name, String email, String password) {
        Optional<Subscriber> subscriber = subscriberRepository.getByEmail(email);
        if (subscriber.isPresent()) {
            System.out.println("회원가입 실패: 이미 존재하는 이메일입니다.\n");
            return subscriber.get();
        }

        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDate subscriptionStartDate = LocalDate.now();
        Subscriber newSubscriber = new Subscriber(
                id,
                name,
                email,
                password,
                createdAt,
                createdAt,
                SubscriptionPlan.TRIAL,
                subscriptionStartDate,
                subscriptionStartDate.plusDays(30) // 상수화 필요
                );

        return subscriberRepository.save(newSubscriber);
    }

    public Subscriber signIn(String email, String password) {
        Subscriber subscriber = subscriberRepository.getByEmail(email)
                .orElseGet(() -> {
                    System.out.println("로그인 실패: 존재하지 않는 이메일입니다.\n");
                    return null;
                });

        if (subscriber != null && !subscriber.getPassword().equals(password)) {
            System.out.println("로그인 실패: 유효하지 않은 비밀번호입니다.\n");
            return null;
        }
        return subscriber;
    }

    public Map<String, String> getSubscriberProfile(Subscriber subscriber) {
        Map<String, String> myProfile = new LinkedHashMap<>();

        myProfile.put("이메일", subscriber.getEmail());
        myProfile.put("이름", subscriber.getName());
        myProfile.put("요금제", subscriber.getSubscriptionPlan().getDescription());
        myProfile.put("만료일", subscriber.getSubscriptionEndDate().toString());

        return myProfile;
    }
}
