package com.alliance.claimsvalidationapp.service;

import com.alliance.claimsvalidationapp.entity.User;
import com.alliance.claimsvalidationapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User registerUserService(User user){

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalStateException("Email already exist");
        }
        user.setUserStatus("active");
        return userRepository.save(user);
    }

    public User loginUserService(String email, String pass){
        User user = userRepository.findByEmailAndPassword(email, pass);
        if(userRepository.existsById(user.getId())) {
            if(!user.getUserStatus().equals("deleted"))
                return user;
        }
        throw new IllegalStateException("User not found!");
    }

    public User deleteUserService(Long id){
        User oldUser = null;
        if(userRepository.findById(id).isPresent()){
            oldUser = userRepository.findById(id).get();
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
            sessionUser.setPassword(password);
        } else {
            throw new IllegalStateException("User not found");
        }
        return userRepository.save(sessionUser);
    }

    public String validateSessionUserPasswordService(Long id, String password) {
        User sessionUser = null;
        if(userRepository.findById(id).isPresent()){
            sessionUser = userRepository.findById(id).get();
            if(sessionUser.getPassword().equals(password)){
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
