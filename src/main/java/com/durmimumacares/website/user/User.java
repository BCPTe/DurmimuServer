package com.durmimumacares.website.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Random;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    //private String username;
    private String name;
    private String surname;
    private String email;
    private int verificationCode;
    private boolean active;

    public User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;

        // generate a random number between 100000 and 999999 (of 6 digits) for registration validation
        this.verificationCode = (int) (Math.random() * (999999 - 100000) + 100000);
        this.active = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public boolean isActive() {
        return active;
    }
}
