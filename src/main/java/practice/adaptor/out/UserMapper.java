package practice.adaptor.out;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import practice.adaptor.out.entity.UserEntity;
import practice.user.domain.User;

@Mapper
public interface UserMapper {

  public User toDomain(UserEntity userEntity);

}
