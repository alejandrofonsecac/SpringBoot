package academy.devdojo.springboot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("anime")
@Log4j2
@AllArgsConstructor //Cria os conscrutor automaticamente com todos os argumentos do DateUtil
public class AnimeController {
    private DateUtil dateUtil;

    //localhost:8080/anime/list
    @GetMapping(path = "list")
    public List<Anime> list(){
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return  List.of(new Anime("Re:Zero"), new Anime("Berserk"));
    }

    @GetMapping(path = "list2")
    public List<Anime> list2(){
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return  List.of(new Anime("Re:Zero"), new Anime("Berserk"));
    }
}
