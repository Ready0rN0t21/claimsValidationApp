package com.alliance.claimsvalidationapp.service;

import com.alliance.claimsvalidationapp.entity.User;
import com.alliance.claimsvalidationapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User registerUserService(User user){

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalStateException("Email already exist");
        }
        passwordEncoder = new BCryptPasswordEncoder();
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        user.setUserStatus("active");
        return userRepository.save(user);
    }

    public User loginUserService(String email, String pass){
        if(userRepository.findByEmail(email).isPresent()){
            User user = userRepository.findByEmail(email).get();
            passwordEncoder = new BCryptPasswordEncoder();
            if(passwordEncoder.matches(pass, user.getPassword())){
                return user;
            }
        }
        return null;
    }

    public User deleteUserService(Long id){
        User oldUser = null;
        if(userRepository.findById(id).isPresent()){
            oldUser = userRepository.findById(id).get();
            oldUser.setEmail(null);
            oldUser.setPassword(null);
            oldUser.setUsertype(null);
            oldUser.setUserStatus("deleted");
        } else {
            throw new IllegalStateException("User not found");
        }
        return userRepository.save(oldUser);
    }

    public List<User> getAllUserService(){
        return userRepository.findAllByUserStatus("active");
    }

    public User editSessionUserPasswordService(Long id, String password){
        User sessionUser = null;
        if(userRepository.findById(id).isPresent()){
            sessionUser = userRepository.findById(id).get();
            passwordEncoder = new BCryptPasswordEncoder();
            String encodedPass = passwordEncoder.encode(sessionUser.getPassword());
            sessionUser.setPassword(encodedPass);
        } else {
            throw new IllegalStateException("User not found");
        }
        return userRepository.save(sessionUser);
    }

    public String validateSessionUserPasswordService(Long id, String password) {
        User sessionUser = null;
        if(userRepository.findById(id).isPresent()){
            passwordEncoder = new BCryptPasswordEncoder();
            sessionUser = userRepository.findById(id).get();
            if(passwordEncoder.matches(password, sessionUser.getPassword())){
                return "true";
            } else {
                return "false";
            }
        } else {
            throw new IllegalStateException("User not found");
        }
    }

    public User editSessionNameService(Long id, String firstName, String lastName){
        User sessionUser = userRepository.findById(id).get();
        sessionUser.setLastName(lastName);
        sessionUser.setFirstName(firstName);

        return userRepository.save(sessionUser);
    }
}
