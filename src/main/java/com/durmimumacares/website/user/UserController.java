package com.durmimumacares.website.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
//    @Value("${env.GMAIL_USER}") private String sender;
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<List<User>>(userService.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Optional<List<User>>> getUserByName(@PathVariable String name) {
        return new ResponseEntity<Optional<List<User>>>(userService.userByName(name), HttpStatus.OK);
    }

    @GetMapping("/surname/{surname}")
    public ResponseEntity<Optional<List<User>>> getUserBySurname(@PathVariable String surname) {
        return new ResponseEntity<Optional<List<User>>>(userService.userBySurname(surname), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<List<User>>> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<Optional<List<User>>>(userService.userByEmail(email), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody String payload) {
        JSONObject obj = new JSONObject(payload);
        User user = new User(obj.getString("name"), obj.getString("surname"), obj.getString("email"));

        userService.newUser(user);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String content = "Dear <b>[[name]]</b>,<br>"
                + "please click on the link below to activate your account:<br>"
                + "<a href=\"https://www.pornhub.com\">CONFIRM REGISTRATION</a><br><br>"
                + "Thank you,<br>"
                + "<b>Durmimu Macares Team</b><br>"
                + "<img src=[[logo]] height=\"100\"/>";
        content = content.replace("[[name]]",user.getName());
        content = content.replace("[[logo]]", "https://i.ibb.co/YbFk7cX/logo-black.png");
        try {
            helper.setTo(obj.getString("email"));
            helper.setText(content, true);
            helper.setSubject("Confirm Account Registration");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(mimeMessage);

        return new ResponseEntity<String>("User registered", HttpStatus.CREATED);
    }

}
