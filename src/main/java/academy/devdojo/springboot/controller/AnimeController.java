package academy.devdojo.springboot.controller;

import academy.devdojo.springboot.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
@Log4j2
public class AnimeController {

    private final DateUtil dateUtil; // 👈 TEM QUE SER FINAL
    private final AnimeService animeService;

    @GetMapping
    public List<Anime> list() {
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return animeService.listAll();
    }
}