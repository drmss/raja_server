package ir.msisoft.Models;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class User {
    private int id;
    private String name;
    private String username;
    private String password;

    public User(int id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = generatePasswordHash(password);
    }

    public boolean checkPassword(String password) {
        String hash = generatePasswordHash(password);
        return this.password.equals(hash);
    }

    private static String generatePasswordHash(String password) {
        try {
            byte[] bytesOfMessage = ("!raja " + password).getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytesOfDigest = md.digest(bytesOfMessage);
            return DatatypeConverter.printHexBinary(bytesOfDigest).toLowerCase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "failed!";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
