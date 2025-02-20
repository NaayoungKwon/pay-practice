package practice.adaptor.out;

import org.mapstruct.Mapper;
import practice.account.domain.Account;
import practice.adaptor.out.entity.AccountEntity;
import practice.adaptor.out.entity.UserEntity;
import practice.user.domain.User;

@Mapper
public interface AccountMapper {

  public Account toDomain(AccountEntity accountEntity);

}
