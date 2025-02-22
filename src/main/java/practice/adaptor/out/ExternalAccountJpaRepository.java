package practice.adaptor.out;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.account.domain.ExternalAccount;
import practice.adaptor.out.entity.ExternalAccountEntity;
import practice.adaptor.out.entity.UserEntity;

public interface ExternalAccountJpaRepository extends JpaRepository<ExternalAccountEntity, Long> {

ExternalAccountEntity findByUser(UserEntity user);
}
