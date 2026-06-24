package academy.devdojo.springboot.factory;

import academy.devdojo.springboot.dominio.DevDojoUser;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFactory {
    public static DevDojoUser createUser(PasswordEncoder passwordEncoder){
        return DevDojoUser.builder()
                .name("Matheus Zanella")
                .username("matheus")
                .password(
                        passwordEncoder.encode("123")
                )
                .authorities("ROLE_USER")
                .build();
    }

    public static DevDojoUser createAdmin(PasswordEncoder passwordEncoder){
        return DevDojoUser.builder()
                .name("Christofer")
                .username("chris")
                .password(
                        passwordEncoder.encode("123")
                )
                .authorities("ROLE_USER,ROLE_ADMIN")
                .build();
    }
}
