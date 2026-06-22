package academy.devdojo.springboot.service;

import academy.devdojo.springboot.repository.DevDojoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DevDojoUserService implements UserDetailsService {

    private final DevDojoUserRepository devDojoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Assumindo que findByUsername já retorna um Optional<DevDojoUser>
        return devDojoRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("DevDojo user not found"));
    }
}
