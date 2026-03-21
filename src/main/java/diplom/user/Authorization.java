package diplom.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Authorization {
    private String email;
    private String password;
    private String name;


    public static Authorization from(UserCreate user) {
        return new Authorization(user.getEmail(), user.getPassword(), user.getName());
    }
}
