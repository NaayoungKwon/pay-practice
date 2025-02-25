package practice.adaptor.out;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import practice.adaptor.out.entity.PaymentEntity;
import practice.payment.domain.Payment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

  @Mapping(source = "user.id", target = "user.id")
  @Mapping(target = "status", constant = "PREPARE")
  PaymentEntity toEntityPrepared(Payment payment);

  @Mapping(source = "user.id", target = "user.id")
  @Mapping(source = "updatedAt", target = "paymentDateTime")
  @Mapping(source = "id", target = "paymentId")
  Payment toDomain(PaymentEntity paymentEntity);

}
