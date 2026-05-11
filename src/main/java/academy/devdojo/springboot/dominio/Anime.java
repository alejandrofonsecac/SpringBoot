package academy.devdojo.springboot.dominio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity //Marca a classe como tabela no banco de dados
@NoArgsConstructor //Gera construtor vazio
@AllArgsConstructor //Gera construtor com todos os atributos
@Data //Gera getters, setters, equals, hashCode e toString

public class Anime {

    @Id //Define o campo como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID é gerado automaticamente pelo banco
    private Long id;
    private String name;

}