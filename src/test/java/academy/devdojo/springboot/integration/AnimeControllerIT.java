package academy.devdojo.springboot.integration;

import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.repository.AnimeRepository;
import academy.devdojo.springboot.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import util.AnimeCreator;
import org.springframework.boot.resttestclient.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnimeControllerIT {
    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();

        ResponseEntity<PageableResponse<Anime>> animePage = testRestTemplate.exchange("/anime", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
        });

        Assertions.assertThat(animePage.getBody())
                .isNotNull();

        Assertions.assertThat(animePage.getBody().toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(
                animePage.getBody().toList().get(0).getName()
        ).isEqualTo(expectedName);
    }
}
