package domain.account;

import domain.subscription.SubscriptionPlan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class Subscriber extends User {

    public Subscriber(
            String id, String name, String email, String password,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            SubscriptionPlan subscriptionPlan,
            LocalDate subscriptionStartDate, LocalDate subscriptionEndDate
    ) {
        super(id, name, email, password, createdAt, updatedAt);

        this.subscriptionPlan = subscriptionPlan;
        this.isAutoRenew = false;
        this.subscriptionStartDate = subscriptionStartDate;
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public Boolean getAutoRenew() {
        return isAutoRenew;
    }

    public LocalDate getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    private SubscriptionPlan subscriptionPlan;
    private Boolean isAutoRenew;
    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;


}