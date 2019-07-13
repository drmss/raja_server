package ir.msisoft.Controllers;

import ir.msisoft.Models.User;
import ir.msisoft.Repositories.UserRepository;
import ir.msisoft.Sentry.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthServices {
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/register")
    public String register(@RequestParam(value = "name") String name, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        User old = UserRepository.findByUsername(username);
        if (old != null)
            return "username is exist.";
        UserRepository.add(new User(name, username, password));
        User user = UserRepository.findByUsername(username);
        return Authentication.createToken(user.getId());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        User user = UserRepository.findByUsername(username);
        if (user == null || user.getPassword().isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid login");
        if (!user.checkPassword(password))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid login");
        return ResponseEntity.status(HttpStatus.OK).body(Authentication.createToken(user.getId()));
    }
}
