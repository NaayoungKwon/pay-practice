package practice.adaptor.out.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "account")
@DynamicUpdate
@EntityListeners(AuditingEntityListener::class)
class AccountEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null,

    var accountNumber: String? = null,

    var type: String? = null,

    var balance: BigDecimal? = null,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = [CascadeType.ALL])
    var transactions: MutableList<TransactionEntity> = mutableListOf(),

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {

    constructor(id: Long) : this() {
        this.id = id
    }
}