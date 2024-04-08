package bdki.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bdki.project.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByStan(String stan);
    boolean existsByStan(String stan);
    User findByPhoneNumber(String phoneNumber);

}