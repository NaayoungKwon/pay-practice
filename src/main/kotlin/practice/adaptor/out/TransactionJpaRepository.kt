package practice.adaptor.out

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import practice.adaptor.out.entity.TransactionEntity

interface TransactionJpaRepository : JpaRepository<TransactionEntity, Long> {
    @Modifying
    @Query("UPDATE transaction t SET t.transactionType = :transactionType WHERE t.id = :id")
    fun updateTransactionType(transactionType: String, id: Long)
}
