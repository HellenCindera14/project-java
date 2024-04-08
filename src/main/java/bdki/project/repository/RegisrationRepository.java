package bdki.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bdki.project.entity.Regisration;

@Repository
    public interface RegisrationRepository extends JpaRepository<Regisration, String> {

}
