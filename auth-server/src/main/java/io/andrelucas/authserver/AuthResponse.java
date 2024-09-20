package io.andrelucas.authserver;

public record AuthResponse(boolean isAuthenticated) {

    public static AuthResponse authorized() {
        return new AuthResponse(true);
    }

    public static AuthResponse unauthorized() {
        return new AuthResponse(false);
    }
}
