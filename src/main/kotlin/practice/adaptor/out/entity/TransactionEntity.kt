package practice.adaptor.out.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(name = "transaction")
@EntityListeners(AuditingEntityListener::class)
class TransactionEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    var account: AccountEntity? = null,

    var transactionType: String? = null,
    var amount: BigDecimal? = null,
    var counterPartyBank: String? = null,
    var counterPartyAccountId: String? = null,
    var paymentId: Long? = null,
    var createdDate: LocalDate? = null,

    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)