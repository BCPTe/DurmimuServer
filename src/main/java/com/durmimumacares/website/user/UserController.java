package com.durmimumacares.website.user;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Value("${env.LOGO}")
    private String logo;
    @Value("${env.GMAIL_USER}")
    private String sender;
    @Value("${env.SITE_URL}")
    private String siteUrl;
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
    public ResponseEntity<Optional<User>> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<Optional<User>>(userService.userByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Optional<User>> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<Optional<User>>(userService.userByUsername(username), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody String payload) {
        JSONObject obj = new JSONObject(payload);
        String receivedUsernameOrEmail = obj.getString("usernameOrEmail");
        String receivedPasswordEncoded = obj.getString("password");

        if(getUserByEmail(receivedUsernameOrEmail).getBody().isPresent()) {
            User user = getUserByEmail(receivedUsernameOrEmail).getBody().get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String DBPasswordEncoded = encoder.encode(user.getPassword());
            if(receivedPasswordEncoded != DBPasswordEncoded) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            else return new ResponseEntity(HttpStatus.OK);
        }
        else if(getUserByUsername(receivedUsernameOrEmail).getBody().isPresent()) {
            User user = getUserByUsername(receivedUsernameOrEmail).getBody().get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String DBPasswordEncoded = encoder.encode(user.getPassword());
            if(receivedPasswordEncoded != DBPasswordEncoded) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            else return new ResponseEntity(HttpStatus.OK);

        }
        else return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody String payload) {
        JSONObject obj = new JSONObject(payload);
        User user = new User(obj.getString("name"), obj.getString("surname"), obj.getString("date"), obj.getString("username"), obj.getString("email"), obj.getString("password"));


        userService.newUser(user);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String content = "Dear <b>[[name]]</b>,<br>"
                + "please click on the button below to activate your account:<br><br>"
                + "<a style=\"background:blue;color:white;padding:10px;border-radius:5px;text-decoration:none;font-weight:bold;\" href=\"[[siteurl]]\">CONFIRM REGISTRATION</a><br><br>"
                + "Thank you,<br>"
                + "<b><i>Durmimu Macares Team</i></b><br>"
                + "<img src=[[logo]] height=\"100\"/>";
        content = content.replace("[[name]]",user.getName());
        content = content.replace("[[siteurl]]",siteUrl + "/api/v1/users/confirm-registration?code=" + user.getVerificationCode() + "&userid=" + user.getId());
        content = content.replace("[[logo]]", logo);
        try {
            helper.setFrom(new InternetAddress(sender, "Durmimu Macares Team"));
            helper.setTo(obj.getString("email"));
            helper.setText(content, true);
            helper.setSubject("Confirm Account Registration");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);

        return new ResponseEntity<String>("User registered!", HttpStatus.CREATED);
    }

    @GetMapping("/confirm-registration")
    public ResponseEntity<String> confirmRegistration(@RequestParam int code, @RequestParam String userid) {
        try {
            Optional<User> optUser = userService.userById(new ObjectId(userid));
            if(userService.updateStatus(new ObjectId(userid), code))
                return new ResponseEntity<String>("Update successful for user: " + optUser.get(), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<String>("Update failed cause user not found --- \n" + e, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>("Update failed cause user not found", HttpStatus.NOT_FOUND);
    }


    // old version (doesn't catch exceptions)
//    Optional<User> optUser = userService.userById(new ObjectId(userid));
//    if(optUser.isPresent()) {
//        if(userService.updateStatus(new ObjectId(userid), code))
//            return new ResponseEntity<String>("Update successful for user: " + optUser.get(), HttpStatus.OK);
//        else return new ResponseEntity<String>("Update failed cause mismatching verification code", HttpStatus.NOT_FOUND);
//    } else return new ResponseEntity<String>("Update failed cause user not found", HttpStatus.NOT_FOUND);

}
