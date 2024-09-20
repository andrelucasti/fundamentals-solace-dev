package io.andrelucas.authserver;

public record AuthorizedUser(String name, String role, String password) {
}
