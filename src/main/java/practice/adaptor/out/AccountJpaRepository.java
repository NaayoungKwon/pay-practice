package practice.adaptor.out;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.adaptor.out.entity.AccountEntity;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

}
