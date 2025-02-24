package practice.adaptor.out;

import java.math.BigDecimal;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import practice.account.domain.Account;
import practice.account.domain.ExternalAccount;
import practice.account.domain.TransactionHistory;
import practice.account.domain.TransactionHistoryList;
import practice.adaptor.out.entity.AccountEntity;
import practice.adaptor.out.entity.ExternalAccountEntity;
import practice.adaptor.out.entity.TransactionEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

  @Mapping(source = "id", target = "accountId")
  @Mapping(source = "user.id", target = "userId")
  Account toDomain(AccountEntity accountEntity);

  @Mapping(source = "accountEntity.id", target = "accountId")
  @Mapping(source = "accountEntity.user.id", target = "userId")
  Account toDomain(AccountEntity accountEntity, BigDecimal todayWithdraw);

  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "bank", target = "bankName")
  ExternalAccount toDomain(ExternalAccountEntity externalAccountEntity);

  @Mapping(source = "accountId", target = "id")
  @Mapping(source = "userId", target = "user.id")
  @Mapping(source = "transactionHistoryList", target = "transactions")
  AccountEntity toEntityAlone(Account account);

  @Mapping(source = "transactionType", target = "type")
  @Mapping(source="createdAt", target="transactionDateTime")
  TransactionHistory toDomain(TransactionEntity transactionEntity);

  @Mapping(source = "type", target = "transactionType")
  @Mapping(source="transactionDateTime", target="createdAt")
  @Mapping(source="transactionDateTime", target="createdDate")
  TransactionEntity toEntity(TransactionHistory transactionHistory);

  List<TransactionEntity> toEntity(List<TransactionHistory> transactionHistoryList);

  default  AccountEntity toEntity(Account account){
    AccountEntity entity = toEntityAlone(account);
    entity.getTransactions().forEach(t -> t.setAccount(entity));
    return entity;
  }

  default List<TransactionEntity> toEntity(TransactionHistoryList transactionHistoryList) {
    return toEntity(transactionHistoryList.transactionHistoryList());
  }

}
