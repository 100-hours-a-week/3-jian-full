package domain.subscription;

public enum SubscriptionPlan {

    FREE("FREE", "무료"),
    TRIAL("TRIAL", "체험"),
    PREMIUM("PREMIUM", "유료"),
    EXPIRED("EXPIRED", "만료");

    private final String value;
    private final String description;

    SubscriptionPlan(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static SubscriptionPlan fromValue(String value) {
        for (SubscriptionPlan plan : SubscriptionPlan.values()) {
            if (plan.value.equals(value)) {
                return plan;
            }
        }
        return null; // 예외처리 필요
    }

    public String getDescription() {
        return description;
    }
}
