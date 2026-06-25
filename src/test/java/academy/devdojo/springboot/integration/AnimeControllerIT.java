package academy.devdojo.springboot.integration;

import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.factory.UserFactory;
import academy.devdojo.springboot.repository.AnimeRepository;
import academy.devdojo.springboot.repository.DevDojoUserRepository;
import academy.devdojo.springboot.request.AnimePostRequestBody;
import academy.devdojo.springboot.request.AnimePutRequestBody;
import academy.devdojo.springboot.wrapper.PageableResponse;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @Autowired
    private AnimeRepository animeRepository;

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

        animeRepository.deleteAll();
        Anime anime = Anime.builder()
                .name("AnimeTest")
                .build();
        animeRepository.save(anime);

        userRestTemplate =
                testRestTemplate.withBasicAuth("matheus", "123");

        adminRestTemplate =
                testRestTemplate.withBasicAuth("chris", "123");
    }

    @Test
    void debug() {
        ResponseEntity<String> response =
                userRestTemplate.exchange(
                        "/animes",
                        HttpMethod.GET,
                        null,
                        String.class
                );

        System.out.println("STATUS = " + response.getStatusCode());
        System.out.println("BODY = " + response.getBody());
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
        assertThat(response.getBody())
                .isNotNull()
                .extracting(anime -> anime != null ? anime.getContent() : null, InstanceOfAssertFactories.list(Anime.class))
                .isNotEmpty()
                .hasSize(1)
                .first()
                .extracting(Anime::getName)
                .isEqualTo("AnimeTest");
    }


    @Test
    void list_ReturnsListInsidePageObject_WhenUserAuthorized() {
        ResponseEntity<PageableResponse<Anime>> response =
                adminRestTemplate.exchange(
                        "/animes",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody())
                .isNotNull()
                .extracting(anime -> anime != null ? anime.getContent() : null, InstanceOfAssertFactories.list(Anime.class))
                .isNotEmpty()
                .hasSize(1)
                .first()
                .extracting(Anime::getName)
                .isEqualTo("AnimeTest");
    }

    //Expected error (401)
    @Test
    void save_CreateAnime401_WhenUserNotAuthenticated() {

        AnimePostRequestBody animeTest = AnimePostRequestBody.builder()
                .name("AnimeTest")
                .build();

        ResponseEntity<Anime> response =
                userRestTemplate.postForEntity(
                        "/animes/admin",
                        animeTest,
                        Anime.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    //Expected error (403)
    @Test
    void save_CreateAnime201_WhenUserAuthenticated() {

        AnimePostRequestBody animeTest = AnimePostRequestBody.builder()
                .name("AnimeTest")
                .build();

        ResponseEntity<Anime> response =
                userRestTemplate.postForEntity(
                        "/animes/admin",
                        animeTest,
                        Anime.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().getName())
                .isEqualTo("AnimeTest");
    }

    @Test
    void save_CreateAnime201_WhenAdminAuthenticated() {

        AnimePostRequestBody animeTest = AnimePostRequestBody.builder()
                .name("AnimeTest")
                .build();

        ResponseEntity<Anime> response =
                adminRestTemplate.postForEntity(
                        "/animes/admin",
                        animeTest,
                        Anime.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().getName())
                .isEqualTo("AnimeTest");
    }

    //Expected error (401)
    @Test
    void delete_RemovesAnime401_WhenUserNotAuthenticated() {

        Anime animeSaved = animeRepository.save(
                Anime.builder()
                        .name("AnimeTest")
                        .build()
        );

        ResponseEntity<Void> response =
                userRestTemplate.exchange(
                        "/animes/admin",
                        HttpMethod.DELETE,
                        null,
                        Void.class,
                        animeSaved
                        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    //Error Expected 403
    @Test
    void delete_RemovesAnime204_WhenUserAuthenticated() {
        Anime animeSaved = animeRepository.save(
                Anime.builder()
                        .name("Naruto")
                        .build()
        );

        ResponseEntity<Void> response =
                userRestTemplate.exchange(
                        "/animes/admin/{id}",
                        HttpMethod.DELETE,
                        null,
                        Void.class,
                        animeSaved.getId()
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(animeRepository.findById(animeSaved.getId()))
                .isEmpty();
    }

    @Test
    void delete_RemovesAnime204_WhenAdminAuthenticated() {
        Anime animeSaved = animeRepository.save(
                Anime.builder()
                        .name("Naruto")
                        .build()
        );

        ResponseEntity<Void> response =
                adminRestTemplate.exchange(
                        "/animes/admin/{id}",
                        HttpMethod.DELETE,
                        null,
                        Void.class,
                        animeSaved.getId()
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(animeRepository.findById(animeSaved.getId()))
                .isEmpty();
    }

    @Test
    void put_ReplaceAnime204_WhenAdminAuthenticated() {

        Anime animeSaved = animeRepository.save(
                Anime.builder()
                        .name("AnimeGenerics")
                        .build()
        );

        AnimePutRequestBody request = AnimePutRequestBody.builder()
                .id(animeSaved.getId())
                .name("AnimeTestReplace")
                .build();

        HttpEntity<AnimePutRequestBody> requestEntity =
                new HttpEntity<>(request);

        ResponseEntity<Void> response =
                adminRestTemplate.exchange(
                        "/admin/replace",
                        HttpMethod.PUT,
                        requestEntity,
                        Void.class
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

        Anime updated = animeRepository.findById(animeSaved.getId()).orElseThrow();

        assertThat(updated.getName())
                .isEqualTo("AnimeTestReplace");
    }
}