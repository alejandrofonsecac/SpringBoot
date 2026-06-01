package academy.devdojo.springboot.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePostRequestBody {
    //Validações podendo ser feitas com o spring-boot-starter-validation
    @NotNull(message = "The anime name cannot be null")
    @NotEmpty(message = "The anime name cannot be null")
    private String name;
}
