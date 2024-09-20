package io.andrelucas.authserver.auth_user;

import java.util.Optional;

public interface IUserRepository {
    Optional<AuthorizedUser> findByNameAndPassword(final String name, final String password);
}
