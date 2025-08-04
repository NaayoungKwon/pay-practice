package practice.adaptor.out.entity


import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "external_account")
@DynamicUpdate
@EntityListeners(AuditingEntityListener::class)
class ExternalAccountEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null,

    var bank: String? = null,

    var accountNumber: String? = null,

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)