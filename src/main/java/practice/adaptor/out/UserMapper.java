package practice.adaptor.out;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import practice.adaptor.out.entity.UserEntity;
import practice.user.domain.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  User toDomain(UserEntity userEntity);

}
