package controller;

import domain.account.Subscriber;
import service.SubscriberService;

import java.util.Map;
import java.util.Scanner;

public class SubscriberController {

    private final Scanner scanner;

    private final SubscriberService subscriberService;

    public SubscriberController(Scanner scanner, SubscriberService subscriberService) {
        this.scanner = scanner;
        this.subscriberService = subscriberService;
    }

    public Subscriber signUp() {
        System.out.print("이름을 입력해주세요: ");
        String name = scanner.nextLine().trim();

        System.out.print("이메일을 입력해주세요: ");
        String email = scanner.nextLine().trim();

        System.out.print("비밀번호를 입력해주세요: ");
        String password = scanner.nextLine().trim();

        return subscriberService.signUp(name, email, password);
    }

    public Subscriber signIn() {
        System.out.print("이메일을 입력해주세요: ");
        String email = scanner.nextLine().trim();

        System.out.print("비밀번호를 입력해주세요: ");
        String password = scanner.nextLine().trim();

        return subscriberService.signIn(email, password);
    }

    public void printProfile(Subscriber subscriber) {
        subscriberService.getSubscriberProfile(subscriber);
    }
}
