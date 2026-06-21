package academy.devdojo.springboot.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;

import static org.springframework.security.config.Customizer.withDefaults;


//*
//    O que é CSRF?
//        Imagine que você esta logado no seu banco e recebe um link que faz você transferir x              quantidade de dinheiro. O seu navegador ve que você ja esta logado e automaticamente             executa a URL. Para impedir isso usamos ** CSRF **
//
//    Quando usar CookieCSRFTokenRepository?
//        Em um cenário de aplicação web. Aonde tenha Angular, React ou aplicação parecida. O               ideal é não usar quando usamos JWT e API REST. Nesse cenário fazer: http.csrf                    (AbstractHttpConfigurer::disable);

//    O CookieCSRFTokenRepository é responsável por criar o cookie XSRF-TOKEN. Dessa forma usado        com o withHttpOnlyFalse() permite *document.coookie* que é muito usado em Angular e React

//    O CsrfTokenRequestAttributeHandler é responsável por ler o X-XSRF-TOKEN

//*

//-------------------------------------------------------------

//*
    //{1}
//    .csrf()
//    .csrfTokenRepository(
//        CookieCsrfTokenRepository.withHttpOnlyFalse()
//    ) -> Isso da errado hoje em dia, funcionava apenas no SpringBoot5

//    Agora no Spring6 temos o **CsrfTokenRequestHandler** ele diz:
//      onde procurar o token;
//      qual atributo da request usar;
//      como disponibilizar o token.
//*


//{2}
// Proteção CSRF habilitada.
// Necessária quando usamos sessão e cookies.

//{3}
// Armazena o token CSRF em um cookie.

//{4}
// Security 6: responsável por ler o token enviado.

//{5}
// Todas as requisições precisam estar autenticadas.

//{6}
// Autenticação Basic.
@Log4j2
@Configuration
public class SecurityConfig {

    @Bean
    public CsrfTokenRequestHandler requestHandler() {
        return new CsrfTokenRequestAttributeHandler();
    }

    // Cadeia de filtros do Spring Security.
    // Substitui o antigo WebSecurityConfigurerAdapter.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf //{2}
                        .csrfTokenRepository( //{3}
                                CookieCsrfTokenRepository.withHttpOnlyFalse()
                        )
                        .csrfTokenRequestHandler(//{4}
                                new CsrfTokenRequestAttributeHandler()
                        )
                )

                .authorizeHttpRequests(auth -> //{5}
                        auth.anyRequest().authenticated()
                )

                .httpBasic(withDefaults());//{6}
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails user = User.withUsername("Jorge")
                .password(passwordEncoder.encode("balda"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}