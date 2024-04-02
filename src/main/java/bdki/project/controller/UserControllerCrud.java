package bdki.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bdki.project.entity.User;
import bdki.project.services.UserService;

@RestController
@RequestMapping("/api/karyawan")
@EnableCaching
public class UserControllerCrud {

    private final UserService userService;

    @Autowired
    public UserControllerCrud(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        try {
            User newUser = userService.createUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("user", newUser);
            response.put("responseCode", "00");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("duplicate stan")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Duplicate stan");
                errorResponse.put("responseCode", "05");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", e.getMessage());
                errorResponse.put("responseCode", "99");
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") long userId) {
        try {
            User user = userService.getUserById(userId);
            System.out.println("user yang anda cari: " + userId + " " + user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") long userId, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(userId, user);
            System.out.println("User updated in Database and Redis: " + " " + userId + " " + user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") long userId) {
        try{
        userService.deleteUser(userId);
        return "User berhasil di hapus " + userId;
        }catch (Exception e) {
            e.printStackTrace();
            return "user berhasil di hapus: " + userId;
        }
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
