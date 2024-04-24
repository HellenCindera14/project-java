package bdki.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bdki.project.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByStan(String stan);
    boolean existsByStan(String stan);
    Optional<User> findByPhoneNumber(String phoneNumber);

}