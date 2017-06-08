package com.epam.ws.rest;

import java.util.HashMap;

public class UserImageData
{
    private static HashMap<User, String> userImageMap = new HashMap<>();

    private UserImageData() {
    }

    private static class DataHolder {
        private final static UserImageData instance = new UserImageData();
    }

    public static UserImageData getInstance() {
        return UserImageData.DataHolder.instance;
    }

    public HashMap<User, String> getAllUserImages() {
        return userImageMap;
    }

    public String getImageByUser(User user) {
        return userImageMap.get(user);
    }

    public void addUserImage(User user, String filename) {
        userImageMap.put(user, filename);
    }

    public void updateUserImage(User user, String filename) {
        userImageMap.replace(user, filename);
    }

    public void deleteUserImage(User user) {
        userImageMap.remove(user);
    }
}
