package practice.adaptor.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import practice.adaptor.out.entity.UserEntity;
import practice.user.application.out.LoadUserPort;
import practice.user.application.out.RegisterUserPort;
import practice.user.domain.User;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort, RegisterUserPort {

    private final UserMapper userMapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    public boolean hasUser(String name, String email) {
        return userJpaRepository.findByNameAndEmail(name, email).isPresent();
    }

    @Override
    public User register(String name, String email) {
        UserEntity userEntity = UserEntity.builder().name(name).email(email).build();
        return userMapper.toDomain(userJpaRepository.save(userEntity));
    }
}
