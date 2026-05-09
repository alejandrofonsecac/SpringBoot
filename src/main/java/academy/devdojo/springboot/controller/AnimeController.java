package academy.devdojo.springboot.controller;

import academy.devdojo.springboot.request.AnimePostRequestBody;
import academy.devdojo.springboot.request.AnimePutRequestBody;
import academy.devdojo.springboot.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.util.DateUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
@Log4j2
public class AnimeController {

    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<Anime>> list() {
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> finById(@PathVariable("id") long id) {
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestExeption(id));
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody AnimePostRequestBody anime){
        return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);
    }

    //Delete e id Potent -> Ver mais sobre isso
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody){
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}