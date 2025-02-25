package practice.adaptor.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import practice.adaptor.out.entity.PaymentEntity;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

  @Modifying
  @Query("UPDATE PaymentEntity p SET p.status = :status, p.updatedAt = current timestamp WHERE p.id = :id")
  void updateStatus(String status, Long id);

}
