package academy.devdojo.springboot.service;

import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.repository.AnimeRepository;
import academy.devdojo.springboot.request.AnimePostRequestBody;
import academy.devdojo.springboot.request.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AnimeService{
    private final AnimeRepository animeRepository;

    public List<Anime> listAll(){
        return animeRepository.findAll();
    }

    public Anime findByIdOrThrowBadRequestExeption(long id){
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        Anime anime = Anime.builder().name(animePostRequestBody.getName()).build();
        return animeRepository.save(anime);
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestExeption(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime animeSaved = findByIdOrThrowBadRequestExeption(animePutRequestBody.getId());
        Anime anime = Anime.builder()
                .id(animeSaved.getId())
                .name(animePutRequestBody.getName())
                .build();
        animeRepository.save(anime);
    }
}
