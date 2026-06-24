package academy.devdojo.springboot.factory;

import academy.devdojo.springboot.dominio.DevDojoUser;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFactory {
    public static DevDojoUser user(PasswordEncoder passwordEncoder){
        return DevDojoUser.builder()
                .username("matheus")
                .password(
                        passwordEncoder.encode("123")
                )
                .authorities("ROLE_USER")
                .build();
    }

    public static DevDojoUser admin(PasswordEncoder passwordEncoder){
        return DevDojoUser.builder()
                .username("chris")
                .password(
                        passwordEncoder.encode("123")
                )
                .authorities("ROLE_USER,ROLE_ADMIN")
                .build();
    }
}
