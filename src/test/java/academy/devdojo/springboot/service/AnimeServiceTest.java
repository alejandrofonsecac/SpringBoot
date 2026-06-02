package academy.devdojo.springboot.service;

import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.exception.BadRequestExeption;
import academy.devdojo.springboot.repository.AnimeRepository;
import academy.devdojo.springboot.request.AnimePostRequestBody;
import academy.devdojo.springboot.request.AnimePutRequestBody;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import util.AnimeCreator;
import util.AnimePostRequestBodyCreator;
import util.AnimePutRequestBodyCreator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService animeService;
    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        lenient().when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        lenient().when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        lenient().when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        lenient().when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        lenient().when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        lenient().doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("listAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(0, 10));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllNonPagaeable returns list of anime inside page object when successful")
    void listAllNonPagaeablelistAllNonPagaeable_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeService.listAllNonPagaeable();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestExeption returns anime when successful")
    void findByIdOrThrowBadRequestExeption_ReturnsAnime_WhenSuccessful() {
        Long expectedId = AnimeCreator.createValidAnime().getId();
        Anime anime = animeService.findByIdOrThrowBadRequestExeption(1L);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestExeption throws BadRequestException when anime is not found")
    void findByIdOrThrowBadRequestExeption_ThrowsBadRequestException_WhenAnimeNotFound() {
        lenient().when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(BadRequestExeption.class)
                .isThrownBy(() -> animeService.findByIdOrThrowBadRequestExeption(1L));
    }

    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByname_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeService.findByName("anime");

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of anime when anime is not found")
    void findByname_ReturnsEmptyListOfAnime_WhenAnimeNotFound() {
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeService.findByName("anime");

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns list of anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBody());

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdateAnime_WhenSuccessful() {
        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemoveAnime_WhenSuccessful() {
        Assertions.assertThatCode(() -> animeService.delete(1L))
                .doesNotThrowAnyException();
    }
}