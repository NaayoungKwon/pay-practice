package practice.adaptor.out;

import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import practice.account.domain.Account;
import practice.adaptor.out.entity.AccountEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

  @Mapping(source = "id", target = "accountId")
  @Mapping(source = "user.id", target = "userId")
  Account toDomain(AccountEntity accountEntity);

  @Mapping(source = "accountEntity.id", target = "accountId")
  @Mapping(source = "accountEntity.user.id", target = "userId")
  Account toDomain(AccountEntity accountEntity, BigDecimal todayWithdraw);

  @Mapping(source = "accountId", target = "id")
  @Mapping(source = "userId", target = "user.id")
  AccountEntity toEntity(Account account);

}
