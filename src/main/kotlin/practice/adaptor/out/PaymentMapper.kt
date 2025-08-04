package practice.adaptor.out

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
import practice.adaptor.out.entity.PaymentEntity
import practice.payment.domain.Payment

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PaymentMapper {

    @Mapping(source = "user.id", target = "user.id")
    @Mapping(target = "status", constant = "PREPARE")
    fun toEntityPrepared(payment: Payment): PaymentEntity

    @Mapping(source = "user.id", target = "user.id")
    @Mapping(source = "updatedAt", target = "paymentDateTime")
    @Mapping(source = "id", target = "paymentId")
    fun toDomain(paymentEntity: PaymentEntity): Payment
}
