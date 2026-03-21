package diplom.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreate {
    private String email;
    private String password;
    private String name;
}