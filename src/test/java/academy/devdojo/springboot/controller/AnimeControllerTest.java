package academy.devdojo.springboot.controller;

import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.request.AnimePostRequestBody;
import academy.devdojo.springboot.request.AnimePutRequestBody;
import academy.devdojo.springboot.service.AnimeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import util.AnimeCreator;
import util.AnimePostRequestBodyCreator;
import util.AnimePutRequestBodyCreator;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

//SpringExtension não inicializa itens do Mockito automaticamente... Para inicializar de forma automática deve-se usar Mockito class

@ExtendWith(MockitoExtension.class)
class AnimeControllerTest {

    @InjectMocks
    // Quando você quer testar a classe em si
    private AnimeController animeController;

    @Mock
    //Você utiliza para todas as classes que estão a ser utilizadas em AnimeController
    private AnimeService animeServiceMock;


    //lenient pódemos usar quando temos vários métodos.
    //Podemos quando temos poucos testes por classe usar cada mockito especificamente em cada teste para melhor visualização, mas como contra o código fica um pouco mais "sujo"
    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage =
                new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        lenient().when(animeServiceMock.listAll(any()))
                .thenReturn(animePage);

        lenient().when(animeServiceMock.listAllNonPagaeable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        lenient().when(animeServiceMock.findByIdOrThrowBadRequestExeption(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        lenient().when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        lenient().when(animeServiceMock.save(any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        lenient().doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        lenient().doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

//    @Test
//    @DisplayName("List returns list of anime inside page object when successful")
//    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
//        String expectedName = AnimeCreator.createValidAnime().getName();
//        Page<Anime> animePage = animeController.listAll(null).getBody();
//
//        Assertions.assertThat(animePage).isNotNull();
//        Assertions.assertThat(animePage.toList())
//                .isNotEmpty()
//                .hasSize(1);
//
//        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
//    }

    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeController.listAll().getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {

        Anime animeExpected = AnimeCreator.createValidAnime();

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestExeption(1L))
                .thenReturn(animeExpected);

        Anime anime = animeController.finById(1L).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(animeExpected.getId());
    }

    @Test
    @DisplayName("FindByName returns list of anime when successful")
    void findByname_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeController.findByName("anime").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of anime when anime is not found")
    void findByname_ReturnsEmptyListOfAnime_WhenAnimeNotFound() {
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeController.findByName("anime").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns list of anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdateAnime_WhenSuccessful() {
        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemoveAnime_WhenSuccessful() {
        Assertions.assertThatCode(() -> animeController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}