package com.example;

public class GlobalContext {
    private static String currentUser;

    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
