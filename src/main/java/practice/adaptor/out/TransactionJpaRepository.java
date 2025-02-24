package practice.adaptor.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import practice.adaptor.out.entity.TransactionEntity;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {

  @Modifying
  @Query("UPDATE transaction t SET t.transactionType = ?1 WHERE t.id = ?2")
  void updateTransactionType(String transactionType, Long id);

}
