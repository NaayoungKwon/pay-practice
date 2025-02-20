package practice.adaptor.out;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import practice.adaptor.out.entity.AccountEntity;
import practice.adaptor.out.entity.UserEntity;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  AccountEntity findByUserAndType(UserEntity userEntity, String type);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT a FROM AccountEntity a LEFT JOIN FETCH a.transactions t WHERE a.user.id = :userId AND a.type = :accountType")
  AccountEntity findByUserAndTypeFetch(Long userId, String accountType);
}
