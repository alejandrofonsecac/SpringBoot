package academy.devdojo.springboot.config;

import academy.devdojo.springboot.service.DevDojoUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;


// ------------------------------------//---------------------------------//

//{7}
// O EnableMethodSecurity permite configurar camadas de seguranças conforme a permissão do usuário. Por exemplo, se temos o usuário querendo realizar um request POST, entretanto ele e um usuário e não admin essa camada bloqueia a permissão do usuário, ele pode apenas realizar o POST se ele tiver o login de admin

//{8}
//securedEnabled = true: Habilita o suporte para a @Securedanotação mais antiga. É uma maneira legada de restringir o acesso a métodos, especificando uma lista de funções obrigatórias (por exemplo, @Secured("ROLE_ADMIN"))

//{9}
//jsr250Enabled = true: Ativa o suporte para anotações padrão do Java EE, principalmente @RolesAllowed. Isso permite proteger métodos usando critérios baseados em padrões (por exemplo, @RolesAllowed("ADMIN")).

//E possível usar mais de uma regra para uma requisição. Por exemplo: **@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")**, dessa maneira temos 2 regras para uma requisição que pode ser tanto POST, tanto GET.

/**
 * {10} No momento removi o .formLogin pois estava dando conflito com os SpringSecurityTest, pois nos testes estavamos verificando a API e o formLogin confundia o algoritmo e ele se perguntava se protegia uma API ou uma página WEB
 */

@Log4j2
@Configuration
@EnableMethodSecurity( //{7}
        securedEnabled = true, //{8}
        jsr250Enabled = true//{9}
)
@RequiredArgsConstructor
public class SecurityConfig{
    private final DevDojoUserService devDojoUserService;

    @Bean
    public CsrfTokenRequestHandler requestHandler() {
        return new CsrfTokenRequestAttributeHandler();
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http,
//            DaoAuthenticationProvider provider)
//            throws Exception {
//
//        http
//                .authenticationProvider(provider)
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth ->
//                        auth.anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider provider)
            throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/animes/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/animes","/animes/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest()
                        .authenticated()
                )
//                .formLogin(form -> form.loginPage("/login")) {10}
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {
        log.info("Password encoder: {}", passwordEncoder);
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(devDojoUserService);

        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

//    @Bean
//    public UserDetailsService userDetailsService(
//            PasswordEncoder passwordEncoder) {
//
//        UserDetails admin = User.withUsername("admin")
//                .password(passwordEncoder().encode("123"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withUsername("Jorge")
//                .password(passwordEncoder.encode("balda"))
//                .roles("USER", "ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println(encoder.encode("123"));
        return encoder;
    }
}