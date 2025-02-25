package practice.adaptor.out;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.adaptor.out.entity.AccountEntity;
import practice.adaptor.out.entity.UserEntity;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

  AccountEntity findByUserAndType(UserEntity userEntity, String type);
}
