package academy.devdojo.springboot.client;

import academy.devdojo.springboot.dominio.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        //Temos tambem o RestTemplate().(method)forEntity... Ex: GetForEntity

        ResponseEntity<Anime> entity = new RestTemplate().exchange(
                "http://localhost:8080/animes/2",
                HttpMethod.GET,
                null,
                Anime.class
        );
        log.info(entity.getStatusCode());
        log.info(entity.getBody());

        ResponseEntity<Anime> exchange = new RestTemplate().exchange(
                "http://localhost:8080/animes/2",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        log.info(exchange.getStatusCode());
        log.info(exchange.getBody());
        log.info(exchange);

//        Anime request =
//                Anime.builder()
//                        .name("Naruto")
//                        .build();
//        Anime animeSaved = new RestTemplate().postForObject("http://localhost:8080/animes", request, Anime.class);
//        log.info("Anime saved: {}", animeSaved);


        //O exchange pode ajudar no envio do ResponseEntity que pode ajudar a visualizar e manipular regras Http
        Anime request =
                Anime.builder()
                        .name("Naruto")
                        .build();
        ResponseEntity<Anime> animeSaved = new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.POST,
                new HttpEntity<>(request),
                Anime.class);
        log.info("Anime saved: {}", animeSaved);

        //-------------------------------------------

//        ParameterizedTypeReference serve para preservar informações de tipos genéricos em tempo de execução.


//        ResponseEntity<Void> animeUpdate = new RestTemplate().exchange(
//                "http://localhost:8080/animes",
//                HttpMethod.PUT,
//                new HttpEntity<>(animeSaved, createJSONHeaders()),
//                Void.class
//        );
//        log.info(animeUpdate);

        //-------------------------

        assert animeSaved.getBody() != null;
        ResponseEntity<Void> animeUpdate = new RestTemplate().exchange(
                "http://localhost:8080/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                animeSaved.getBody().getId()
        );
        log.info(animeUpdate);
    }

    private static HttpHeaders createJSONHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}