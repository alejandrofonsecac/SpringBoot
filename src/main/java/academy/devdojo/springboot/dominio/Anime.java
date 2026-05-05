package academy.devdojo.springboot.dominio;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data //Cria getters and setters e outros
@AllArgsConstructor //Gera o construtor com todos os valores
public class Anime {
    private Long id;
    private String name;
}
