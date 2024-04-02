package bdki.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bdki.project.entity.User;

@Repository
    public interface RegisrationRepository extends JpaRepository<User, String> {
}
