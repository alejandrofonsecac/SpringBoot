package academy.devdojo.springboot.client;

import academy.devdojo.springboot.dominio.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args){
        ResponseEntity<Anime> entity = new RestTemplate().exchange(
                "http://localhost:8080/animes/2",
                HttpMethod.GET,
                null,
                Anime.class
        );
        log.info(entity.getStatusCode());
        log.info(entity.getBody());

        ResponseEntity<Anime> animes = new RestTemplate().exchange(
                "http://localhost:8080/animes/2",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        log.info(animes.getStatusCode());
        log.info(animes.getBody());
        log.info(animes);
    }
}