package academy.devdojo.springboot.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// @Component -> Genérico: usado para qualquer classe gerenciada pelo Spring
// @Service -> Especialização de @Component: usado para regras de negócio (camada de serviço)
// @Repository -> Especialização de @Compoent: usado para acesso a dados (banco) e trata exceções de persistência

@Component
public class DateUtil {
    public String formatLocalDateTimeToDataBaseStyle(LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }
}
