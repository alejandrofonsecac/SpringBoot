package academy.devdojo.springboot.service;

import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.exception.BadRequestExeption;
import academy.devdojo.springboot.mapper.AnimeMapper;
import academy.devdojo.springboot.repository.AnimeRepository;
import academy.devdojo.springboot.request.AnimePostRequestBody;
import academy.devdojo.springboot.request.AnimePutRequestBody;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

//Anotações:

//{1} --- Marca como camada de regra de negócio (bean do Spring).

//{2} --- Lombok cria construtor com os atributos final (injeção de dependência).

//{3} --- Executa o metodo dentro de uma transação; se der erro, desfaz tudo (rollback).

@Service // {1}
@RequiredArgsConstructor// {2}
public class AnimeService{
    private final AnimeRepository animeRepository;

    public List<Anime> listAll(){
        return animeRepository.findAll();
    }

    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }


    public Anime findByIdOrThrowBadRequestExeption(long id){
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestExeption("Anime not found"));
    }

    @Transactional(rollbackOn = Exception.class)//{3}
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestExeption(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime savedAnime = findByIdOrThrowBadRequestExeption(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(savedAnime.getId());
        animeRepository.save(anime);
    }
}
