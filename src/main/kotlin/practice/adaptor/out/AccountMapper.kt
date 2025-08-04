package practice.adaptor.out

import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
import practice.account.domain.Account
import practice.account.domain.ExternalAccount
import practice.account.domain.TransactionHistory
import practice.adaptor.out.entity.AccountEntity
import practice.adaptor.out.entity.ExternalAccountEntity
import practice.adaptor.out.entity.TransactionEntity
import java.math.BigDecimal

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface AccountMapper {
    @Mapping(source = "id", target = "accountId")
    @Mapping(source = "user.id", target = "userId")
    fun toDomain(accountEntity: AccountEntity): Account

    @Mapping(source = "accountEntity.id", target = "accountId")
    @Mapping(source = "accountEntity.user.id", target = "userId")
    fun toDomain(accountEntity: AccountEntity, todayWithdraw: BigDecimal): Account

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "bank", target = "bankName")
    fun toDomain(externalAccountEntity: ExternalAccountEntity): ExternalAccount

    @Mapping(source = "accountId", target = "id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "transactionHistoryList.items", target = "transactions")
    fun toEntityAlone(account: Account): AccountEntity

    @Mapping(source = "transactionType", target = "type")
    @Mapping(source = "createdAt", target = "transactionDateTime")
    fun toDomain(transactionEntity: TransactionEntity): TransactionHistory

    @Mapping(source = "type", target = "transactionType")
    @Mapping(source = "transactionDateTime", target = "createdAt")
    @Mapping(source = "transactionDateTime", target = "createdDate")
    fun toEntity(transactionHistory: TransactionHistory): TransactionEntity

    fun toEntity(transactionHistoryList: MutableList<TransactionHistory>): MutableList<TransactionEntity>

//    fun toEntity1(account: Account): AccountEntity {
//        val entity = toEntityAlone(account)
//        entity.transactions.forEach { t: TransactionEntity -> t.account = entity }
//        return entity
//    }

//    @Mapping(source = "transactionHistoryList", target = ".")
//    fun toEntityList(historyList: TransactionHistoryList): List<TransactionEntity>

    //    @Named("mapTransactions")
//    fun toEntity(transactionHistoryList: TransactionHistoryList): List<TransactionEntity> {
//        return transactionHistoryList.transactionHistoryList.let { toEntity(it) }
//    }
}
