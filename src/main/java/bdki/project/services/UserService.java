package bdki.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bdki.project.entity.EmailRequest;
import bdki.project.entity.User;
import bdki.project.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    public User createUser(User user) {
        try {
            String stanValue = user.getStan();
            String phoneNumber = user.getPhoneNumber();

             // Check if phone number is already registered
        // if (userRepository.findByPhoneNumber(phoneNumber) != null) {
        //     throw new RuntimeException("User with phone number " + phoneNumber + " is already registered");
        // }

            if (redisTemplate.hasKey(stanValue)) {
                System.out.println("Masuk pengecekan ");
                throw new RuntimeException("Duplicate stan detected in Redis: " + stanValue);
            }

            User savedUser = userRepository.save(user);
            // String idFromDatabase = Long.toString(savedUser.getId());

            System.out.println("======================================================");
            System.out.println("user berhasil di simpan : " + " " + savedUser);
            System.out.println("ini stanKeys : " + stanValue);
            System.out.println("ini stanValue: " + stanValue);
            System.out.println("======================================================");

            redisTemplate.opsForValue().set(stanValue, stanValue);

            return savedUser;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("An error occurred while creating user: " + e.getMessage());
            throw new RuntimeException("Failed to create new user: " + e.getMessage(), e);
        }
    }

    public User getUserById(long userId) {
        String redisKey = String.valueOf(userId);
        if (redisTemplate.hasKey(redisKey)) {
            String userJSON = redisTemplate.opsForValue().get(redisKey);
            // System.out.println("ini id dari redis: " + redisKey );
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(userJSON, User.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User userFromDatabase = userOptional.get();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String userJSON = objectMapper.writeValueAsString(userFromDatabase);
                redisTemplate.opsForValue().set(redisKey, userJSON);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return userFromDatabase;
        } else {
            System.out.println("User not found in database.");
            return null;
        }
    }
    

    public User updateUser(long userId, User user) {
        if (userRepository.existsById(userId)) {
            user.setId(userId);
            User updatedUser = userRepository.save(user);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String userJSON = objectMapper.writeValueAsString(updatedUser);
                redisTemplate.opsForValue().set(String.valueOf(userId), userJSON);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return updatedUser;
        } else {
            return null;
        }
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        redisTemplate.delete(String.valueOf(userId));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // public User findByPhoneNumber(String phoneNumber) {
    //     return userRepository.findByPhoneNumber(phoneNumber);
    // }
    
}
