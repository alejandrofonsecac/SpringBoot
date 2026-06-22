package academy.devdojo.springboot.repository;

import academy.devdojo.springboot.dominio.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {
    Optional<DevDojoUser> findByUsername(String username);
}