package practice.adaptor.out;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import practice.adaptor.out.entity.UserEntity;

public interface UserJpaRepository  extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByNameAndEmail(String name, String email);

}
