package practice.adaptor.out.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import practice.adaptor.out.entity.converter.ProductListConverter
import practice.payment.domain.Product
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "payment")
@EntityListeners(AuditingEntityListener::class)
class PaymentEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null,

    var partnerId: Long? = null,

    var partnerPayKey: String? = null,

    @Convert(converter = ProductListConverter::class)
    var productItems: List<Product>? = null,

    var totalPayAmount: BigDecimal? = null,

    var status: String? = null,

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(updatable = false)
    var created: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)