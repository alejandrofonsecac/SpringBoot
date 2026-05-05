package academy.devdojo.springboot.service;

import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.repository.AnimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService{
    public static List<Anime> listAll(){
        return  List.of(new Anime(1L,"Re:Zero"), new Anime(2L,"Berserk"));
    }
}
