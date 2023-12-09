package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Login {

    private String username; // логин
    private String password; // пароль

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Login (){

    }
}
