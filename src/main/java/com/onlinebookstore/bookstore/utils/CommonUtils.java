package com.onlinebookstore.bookstore.utils;

import java.util.UUID;

public class CommonUtils {

    public static String generateRandomUUIDString(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
