package academy.devdojo.springboot.integration;


import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.factory.UserFactory;
import academy.devdojo.springboot.repository.DevDojoUserRepository;
import academy.devdojo.springboot.wrapper.PageableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestRestTemplate
public class AnimeControllerIT {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DevDojoUserRepository devDojoUserRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    private TestRestTemplate userRestTemplate;
    private TestRestTemplate adminRestTemplate;

    @BeforeEach
    void setUp(){
        devDojoUserRepository.deleteAll();//Limpar dataBase

        devDojoUserRepository.save(
                UserFactory.createUser(passwordEncoder)
        );

        devDojoUserRepository.save(
                UserFactory.createAdmin(passwordEncoder)
        );

        TestRestTemplate userRestTemplate =
                testRestTemplate.withBasicAuth("matheus", "123");

        TestRestTemplate adminRestTemplate =
                testRestTemplate.withBasicAuth("chris", "123");
    }

    @Test
    void list_Returns401_WhenUserNotAuthenticated() {
        ResponseEntity<String> response =
                testRestTemplate.getForEntity("/animes", String.class);

        assertEquals(HttpStatus.UNAUTHORIZED,
                response.getStatusCode());
    }

    @Test
    void list_ReturnsList_WhenUserAuthorized(){
        ResponseEntity<PageableResponse<Anime>> response =
                userRestTemplate.exchange(
                        "/animes",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(
                response.getBody().getContent().isEmpty()
        );
    }

}
