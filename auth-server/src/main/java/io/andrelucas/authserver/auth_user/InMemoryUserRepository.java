package io.andrelucas.authserver.auth_user;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements IUserRepository{

    private static final List<AuthorizedUser> authorizedUsers = List.of(
            new AuthorizedUser("admin", "admin", "admin"),
            new AuthorizedUser("user", "user", "user"),
            new AuthorizedUser("solace", "solace", "solace")
    );

    @Override
    public Optional<AuthorizedUser> findByNameAndPassword(final String name, final String password) {
        return authorizedUsers.stream().filter(user -> user.name().equals(name) && user.password().equals(name))
                .findFirst();
    }
}
