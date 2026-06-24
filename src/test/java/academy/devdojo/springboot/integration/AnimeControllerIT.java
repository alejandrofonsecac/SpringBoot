package academy.devdojo.springboot.integration;


import academy.devdojo.springboot.factory.UserFactory;
import academy.devdojo.springboot.repository.DevDojoUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnimeControllerIT {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DevDojoUserRepository devDojoUserRepository;

    @BeforeEach
    void setUp(){
        devDojoUserRepository.deleteAll();//Limpar dataBase

        devDojoUserRepository.save(
                UserFactory.user(passwordEncoder)
        );

        devDojoUserRepository.save(
                UserFactory.admin(passwordEncoder)
        );

    }

//    @Test
//    @DisplayName("List returns list of anime inside page object when successful")
//    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
//        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
//        String expectedName = savedAnime.getName();
//
//        ResponseEntity<PageableResponse<Anime>> animePage = testRestTemplate.exchange("/anime", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
//        });
//
//        Assertions.assertThat(animePage.getBody())
//                .isNotNull();
//
//        Assertions.assertThat(animePage.getBody().toList())
//                .isNotEmpty()
//                .hasSize(1);
//
//        Assertions.assertThat(
//                animePage.getBody().toList().get(0).getName()
//        ).isEqualTo(expectedName);
//    }


}
