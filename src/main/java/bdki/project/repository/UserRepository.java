package bdki.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import bdki.project.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);
    List<User> findByCode(String code);
}
