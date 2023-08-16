package com.example.demo;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    @UtilityClass
    public static class ResponseCodes{
        public static final Long SUCCESS = 10000L;
    }
    public static class Roles{
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }
}
