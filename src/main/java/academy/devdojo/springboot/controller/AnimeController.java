package academy.devdojo.springboot.controller;

import academy.devdojo.springboot.request.AnimePostRequestBody;
import academy.devdojo.springboot.request.AnimePutRequestBody;
import academy.devdojo.springboot.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.util.DateUtil;
import java.util.List;

/**
 *
 * {1} -> O AuthenticationPrincipal
 *      Ela serve para injetar diretamente o usuário que está logado na sessão atual. Ver nome ou dados do usuário (funcionando como um get)
 */

@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
@Log4j2
public class AnimeController {

    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> list(Pageable pageable) {
        return ResponseEntity.ok(animeService.listAll(pageable));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll() {
        return ResponseEntity.ok(animeService.listAllNonPagaeable());
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> finById(@PathVariable("id") long id) {
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestExeption(id));
    }

    @GetMapping(path = "/by-id/{id}")
    public ResponseEntity<Anime> finByIdAuthenticationPrincipal(@PathVariable("id") long id, @AuthenticationPrincipal UserDetails userDetails) {//{1}
        log.info(userDetails.getUsername());
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestExeption(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody anime){
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