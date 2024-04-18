package org.poo.cb;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
public class UserDatabase {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {// Adăugăm un utilizator în baza de date
        users.put(user.getEmail(), user);
    }

    public static User getUserByEmail(String email) { // Obținem un utilizator după adresa de email
        return users.get(email);
    }

    public static void deleteAllUsers() {
        users.clear();
    }
}
