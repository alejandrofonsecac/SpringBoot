package academy.devdojo.springboot.service;

import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.exception.BadRequestExeption;
import academy.devdojo.springboot.mapper.AnimeMapper;
import academy.devdojo.springboot.repository.AnimeRepository;
import academy.devdojo.springboot.request.AnimePostRequestBody;
import academy.devdojo.springboot.request.AnimePutRequestBody;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

//Anotações:

//{1} --- Marca como camada de regra de negócio (bean do Spring).

//{2} --- Lombok cria construtor com os atributos final (injeção de dependência).

//{3} --- Executa o metodo dentro de uma transação; se der erro, desfaz tudo (rollback). Usando como exemplo o save dos dados no banco de dados, caso   o save der errado o Transactional nao permite que com as inform,ações ja enviadas ele faça um cadastro  no banco de dados

@Service // {1}
@RequiredArgsConstructor// {2}
public class AnimeService{
    private final AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable){
        return animeRepository.findAll(pageable);
    }

    public List<Anime> listAllNonPagaeable() {
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
