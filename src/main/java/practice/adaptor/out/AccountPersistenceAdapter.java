package practice.adaptor.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import practice.application.account.out.LoadAccountPort;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort {

    private final JpaAccountRepository jpaAccountRepository = null;

}
