package diplom.user;

import net.datafaker.Faker;

public class UserGeneratorFaker {

    private static final Faker faker = new Faker();

    public static UserCreate userGeneratorFaker() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 16, true, true, true);
        String name = faker.name().fullName().replace(" ", "");
        return new UserCreate(email, password, name);
    }
    public static UserCreate userGeneratorFakerNonEmail() {
        String email = null;
        String password = faker.internet().password(8, 16, true, true, true);
        String name = faker.name().fullName().replace(" ", "");
        return new UserCreate(email, password, name);
    }
    public static UserCreate userGeneratorFakerNonPassword() {
        String email = faker.internet().emailAddress();
        String password = null;
        String name = faker.name().fullName().replace(" ", "");
        return new UserCreate(email, password, name);
    }
    public static UserCreate userGeneratorFakerNonName() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 16, true, true, true);
        String name = null;
        return new UserCreate(email, password, name);
    }
}

