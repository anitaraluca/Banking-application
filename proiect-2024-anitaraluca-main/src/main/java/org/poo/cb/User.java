package org.poo.cb;
import java.util.ArrayList;
import java.util.List;

public class User {
    private static User user;

    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private List<User> friends;
    private Portofolio portfolio;
    private boolean premium;

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setPortofolio(Portofolio portfolio) {
        this.portfolio = portfolio;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static User getUser() {
        return user;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public Portofolio getPortfolio() {
        return portfolio;
    }

    public String getEmail() {
        return email;
    }


    public List<User> getFriends() { // Actualizare aici
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User friend) { // Actualizare aici
        if (!friends.contains(friend)) {
            friends.add(friend);
            //System.out.println("Friend added successfully." + friend.getFirstName());
        } else {
            System.out.println("User with email " + friend.getEmail() + " is already a friend.");
        }
    }

    public void listUser() {
        System.out.println("Email: " + email);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Address: " + address);
        System.out.println("Friends: " + friends);
    }
    public User(String email, String firstName, String lastName, String address) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.friends = new ArrayList<>();
        this.portfolio = new Portofolio();
    }

}