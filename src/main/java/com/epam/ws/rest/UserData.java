package com.epam.ws.rest;


import java.util.ArrayList;
import java.util.List;

public class UserData {

    private static List<User> userList;
    private static long idCount = 0L;

    static {
        userList = new ArrayList<User>();
        User user1 = new User("Tom", "Cruise", "SuperTom", "TomCruise@example.com");
        User user2 = new User("Sonya", "Blade", "LegendarySonya", "SonyaBlade@example.com");
        User user3 = new User("Kate", "Winnick", "Lagertha", "KateWinnick@example.com");
        User user4 = new User("Jack", "Black", "TenaciousD", "JackBlack@example.com");

        user1.setId(++idCount);
        user2.setId(++idCount);
        user3.setId(++idCount);
        user4.setId(++idCount);

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
    }

    private UserData() {

    }

    private static class DataHolder {
        private final static UserData instance = new UserData();
    }

    public static UserData getInstance() {
        return DataHolder.instance;
    }

    public List<User> getAllUsers() {
        return userList;
    }

    public User getUserById(long id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User addUser(User user) {
        user.setId(++idCount);
        userList.add(user);
        return user;
    }

    public User updateUser(long userId, User user) {
        boolean result = false;
        for (User data: userList) {
            if (data.getId() == userId) {
                data.setFirstName(user.getFirstName());
                data.setLastName(user.getLastName());
                data.setLogin(user.getLogin());
                data.setEmail(user.getEmail());
                return user;
            }
        }
        return null;
    }

    public void deleteUser(long id) {
        boolean result = false;
        for (User data : userList) {
            if (data.getId() == id) {
                result = userList.remove(data);
                return;
            }
        }
    }
}
