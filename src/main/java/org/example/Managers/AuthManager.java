package org.example.Managers;

public class AuthManager {

    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    private AuthManager() {}

    public static void setToken(String token) {
        tokenHolder.set(token);
    }

    public static String getToken() {
        return tokenHolder.get();
    }

    public static void clear() {
        tokenHolder.remove();
    }
}