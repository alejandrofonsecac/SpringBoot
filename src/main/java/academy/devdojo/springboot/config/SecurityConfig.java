package academy.devdojo.springboot.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
//        Imagine que você esta logado no seu banco e recebe um link que faz você transferir x quantidade de dinheiro. O seu navegador ve que você já está logado e automaticamente executa a URL. Para impedir isso usamos ** CSRF **
//
//    Quando usar CookieCSRFTokenRepository?
//        Em um cenário de aplicação web. Aonde tenha Angular, React ou aplicação parecida. O ideal é não usar quando usamos JWT e API REST. Nesse cenário fazer: http.csrf(AbstractHttpConfigurer::disable);

//    O CookieCSRFTokenRepository é responsável por criar o cookie XSRF-TOKEN. Dessa forma usado com o withHttpOnlyFalse() permite *document.coookie* que é muito usado em Angular e React

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


// ------------------------------------//---------------------------------//

//{7}
// O EnableMethodSecurity permite configurar camadas de seguranças conforme a permissão do usuário. Por exemplo, se temos o usuário querendo realizar um request POST, entretanto ele e um usuário e não admin essa camada bloqueia a permissão do usuário, ele pode apenas realizar o POST se ele tiver o login de admin

//{8}
//securedEnabled = true: Habilita o suporte para a @Securedanotação mais antiga. É uma maneira legada de restringir o acesso a métodos, especificando uma lista de funções obrigatórias (por exemplo, @Secured("ROLE_ADMIN"))

//{9}
//jsr250Enabled = true: Ativa o suporte para anotações padrão do Java EE, principalmente @RolesAllowed. Isso permite proteger métodos usando critérios baseados em padrões (por exemplo, @RolesAllowed("ADMIN")).

//E possível usar mais de uma regra para uma requisição. Por exemplo: **@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")**, dessa maneira temos 2 regras para uma requisição que pode ser tanto POST, tanto GET.

//--- Roles x Authorities

//Roles representam papéis do usuário, já as Authorities representam o que o usuário pode fazer.
// Exemplo: **User.withUsername("admin")
//    .roles("ADMIN")
//    .authorities("anime:delete", "anime:update")
//    .build();**
//  ---> Assim você esta dizendo que o Admin tera tal autoridades

//Verificação? -> Controller
    //Verificação de Roles:
    //**@PreAuthorize("hasRole('ADMIN')")**

    //Verificação de Authorities:
    //**@PreAuthorize("hasAuthority('anime:delete')")**


/**
 * {7} --> Define o nome do atributo onde o token vai ficar na requisição se necessário
 * {8} --> O FormLogin faz com que o Spring crie uma interface basica de login
 *
 * <p>
 *
 * --> BasicAuthenticationFilter ou UsernamePasswordAuthenticationFilter
 *      Eles capturam as credenciais, autenticam o usuário e colocam um objeto de "Autenticação" dentro do contexto de segurança do Spring (chamado SecurityContextHolder).
 *
 * <p>
 *
 * ---BasicAuthenticationFilter vs UsernamePasswordAuthenticationFilter
 *      BasicAuthenticationFilter: É ativado quando você usa .httpBasic(). Ele inspeciona o cabeçalho HTTP da requisição atrás de credenciais codificadas em Base64 (enviadas no formato Authorization: Basic dXN1YXJpbzpzZW5oYQ==). É muito usado em APIs REST puras e testes rápidos (como Postman).
 *
 * <p>
 *
 *      UsernamePasswordAuthenticationFilter: É ativado quando você usa .formLogin(). Ele intercepta uma requisição do tipo POST enviada para a URL de login e lê os dados que vieram do formulário (os campos username e password).
 *
 * <p>
 *      * <p>
 *  * --- FilterSecurityInterceptor
 *  *      Este fica mais ao final. Ele decide se o usuário autenticado tem a Autorização (permissão/roles) necessária para acessar o endpoint solicitado.
 *  * <p>
 *
 *      ex de código:
 * public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 *     http
 *         .csrf(csrf -> csrf.disable()) // Desabilitado apenas para o exemplo ficar limpo
 *         .authorizeHttpRequests(auth -> auth
 *             // [AQUI VOCÊ CONFIGURA O FilterSecurityInterceptor / AuthorizationFilter]
 *             // Você diz a ele quais rotas ele deve vigiar e quais permissões checar.
 *             .requestMatchers("/animes/admin/**").hasRole("ADMIN")
 *             .anyRequest().authenticated()
 *         )
 *         // [AQUI VOCÊ ATIVA O UsernamePasswordAuthenticationFilter]
 *         // Ao colocar essa linha, o Spring injeta o filtro de formulário na cadeia.
 *         .formLogin(withDefaults())
 * <p>
 *         // [AQUI VOCÊ ATIVA O BasicAuthenticationFilter]
 *         // Ao colocar essa linha, o Spring injeta o filtro que lê o cabeçalho "Authorization: Basic".
 *         .httpBasic(withDefaults());
 * <p>
 *     return http.build();
 * }
 */

@Log4j2
@Configuration
@EnableMethodSecurity( //{7}
        securedEnabled = true, //{8}
        jsr250Enabled = true//{9}
)
public class SecurityConfig {

    @Bean
    public CsrfTokenRequestHandler requestHandler() {
        return new CsrfTokenRequestAttributeHandler();
    }

    // Cadeia de filtros do Spring Security.
    // Substitui o antigo WebSecurityConfigurerAdapter.
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf //{2}
//                        .csrfTokenRepository( //{3}
//                                CookieCsrfTokenRepository.withHttpOnlyFalse()
//                        )
//                        .csrfTokenRequestHandler(//{4}
//                                new CsrfTokenRequestAttributeHandler()
//                        )
//                )
//                .authorizeHttpRequests(auth -> //{5}
//                        auth.anyRequest().authenticated()
//                ).formLogin(form -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/home", true)
//                        .permitAll()
//                )
//                .httpBasic(withDefaults());//{6}
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");//{7}

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(requestHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                // Se for usar apenas Postman ou chamadas diretas REST, remova o .formLogin() temporariamente
                // ou use o .formLogin(withDefaults()) para o Spring gerar a tela padrão sem quebrar a rota do seu app.
                .formLogin(withDefaults()) //{8}
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("123"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("Jorge")
                .password(passwordEncoder.encode("balda"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}