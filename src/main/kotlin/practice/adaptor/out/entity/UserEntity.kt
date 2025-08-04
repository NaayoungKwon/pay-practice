package practice.adaptor.out.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class UserEntity(
    var name: String? = null,

    var email: String? = null,

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