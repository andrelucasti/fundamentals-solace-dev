package io.andrelucas.authserver;

import java.util.Optional;

public interface IUserRepository {
    Optional<AuthorizedUser> findByNameAndPassword(final String name, final String password);
}
