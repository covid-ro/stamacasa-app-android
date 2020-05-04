package ro.gov.stamacasa.retrofit.model.request;

public class Profile {
    HealthStatus health_status;
    int user_age;

    public Profile(int user_age, HealthStatus health_status) {
        this.user_age = user_age;
        this.health_status = health_status;
    }

    public int getUser_age() {
        return user_age;
    }

    public HealthStatus getHealth_status() {
        return health_status;
    }
}
