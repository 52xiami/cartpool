package edu.sjsu.cmpe275.cartpool.cartpool.services;

import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.ConflictException;
import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.NotFoundException;
import edu.sjsu.cmpe275.cartpool.cartpool.models.Role;
import edu.sjsu.cmpe275.cartpool.cartpool.models.User;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.CartPoolRepository;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CartPoolRepository cartPoolRepository;
    private final MailService mailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<User> getUsers() {
        mailService.sendHtmlMail("zhaoxna@gmail.com", "Test", "Hello!");
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        if (userRepository.findById(id).isPresent()){
            return userRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerNewUser(User user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            if(!userRepository.existsByScreenName(user.getScreenName())) {
                int atIndex = user.getEmail().indexOf("@");
                String domain = user.getEmail().substring(atIndex+1);
                if(domain.equals("sjsu.edu")){
                    user.setRole(Role.ADMIN);
                }else {
                    user.setRole(Role.USER);
                }
                user.setActive(false);
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                String token = UUID.randomUUID().toString().replace("-", "");
                user.setToken(token);
                userRepository.save(user);
                sendVerificationEmail(user.getEmail(), token);
                return user;
            }else{
                throw new ConflictException("User screen name has been used, please try another one.");
            }
        }else{
            throw new ConflictException("User email has been used, please try another one.");
        }
    }

    @Override
    public User verifyUser(String token) {
        User user = userRepository.findByToken(token);
        if(user != null){
            user.setActive(true);
            user.setToken(null);
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public User joinCartPool(Long id, Long cartPoolId) {
        if(userRepository.findById(id).isPresent()){
            if(cartPoolRepository.findById(cartPoolId).isPresent()){
                User user = userRepository.findById(id).get();
                user.setPoolId(cartPoolId);
                return userRepository.save(user);
            }else{
                throw new NotFoundException("Cart Pool does not exist.");
            }
        }else{
            throw new NotFoundException("Cannot find User.");
        }
    }

    @Override
    public User setContributionScore(Long id, Integer score) {
        if(userRepository.findById(id).isPresent()){
            User user = userRepository.findById(id).get();
            user.setContribution(score);
            return userRepository.save(user);

        }else{
            throw new NotFoundException("Cannot find User.");
        }
    }

    public void sendVerificationEmail(String to, String token){
        String subject = "Registration Confirmation";
        String content = "token is: " + token;
        mailService.sendHtmlMail(to, subject, content);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
