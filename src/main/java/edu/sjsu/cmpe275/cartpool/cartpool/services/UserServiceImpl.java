package edu.sjsu.cmpe275.cartpool.cartpool.services;

import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.ConflictException;
import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.NotFoundException;
import edu.sjsu.cmpe275.cartpool.cartpool.models.CartPool;
import edu.sjsu.cmpe275.cartpool.cartpool.models.OrderStatus;
import edu.sjsu.cmpe275.cartpool.cartpool.models.Role;
import edu.sjsu.cmpe275.cartpool.cartpool.models.User;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.CartPoolRepository;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.OrderRepository;
import edu.sjsu.cmpe275.cartpool.cartpool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EnableAsync
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CartPoolRepository cartPoolRepository;
    private final OrderRepository orderRepository;
    private final MailService mailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<User> getUsers() {
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
                user.setContribution(0);
                user.setPoolStatus("INIT");
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
            return user;
        }else{
            throw new NotFoundException("Cannot find token");
        }
    }

    @Override
    public User joinCartPool(Long id, Long cartPoolId) {
        if(userRepository.findById(id).isPresent()){
            if(cartPoolRepository.findById(cartPoolId).isPresent()){
                User user = userRepository.findById(id).get();
                user.setPoolId(cartPoolId);
                user.setPoolStatus("ACTIVE");
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

    @Override
    public User firebaseLogin(User user) {

        if(userRepository.existsByEmail(user.getEmail())){
            return userRepository.findByEmail(user.getEmail());
        }else{
            user.setPassword(bCryptPasswordEncoder.encode(user.getEmail()));
            user.setActive(true);
            user.setContribution(0);
            int atIndex = user.getEmail().indexOf("@");
            String domain = user.getEmail().substring(atIndex+1);
            if(domain.equals("sjsu.edu")){
                user.setRole(Role.ADMIN);
            }else {
                user.setRole(Role.USER);
            }
            user.setCreationTime(LocalDateTime.now());
            return userRepository.save(user);
        }
    }

    @Override
    public User joinPoolRef(Long id, Long poolId, String refScreenName) {
        if(userRepository.findById(id).isPresent()){
            if(cartPoolRepository.findById(poolId).isPresent()){
                if(userRepository.existsByScreenName(refScreenName)){
                    User refMember = userRepository.findByScreenName(refScreenName);
                    if(refMember.getPoolId()!= null && refMember.getPoolId().equals(poolId) && refMember.getPoolStatus().equals("ACTIVE")){
                        User user = userRepository.findById(id).get();
                        user.setPoolId(poolId);
                        user.setPoolStatus("REF");
                        sendJoinPoolEmailRef(refMember.getEmail(), user.getScreenName(), user.getId(), poolId);
                        return userRepository.save(user);
                    }else{
                        throw new NotFoundException("Reference user is not member of this pool");
                    }
                }else{
                    throw new NotFoundException("Reference member does not exist.");
                }
            }else{
                throw new NotFoundException("Cart Pool does not exist.");
            }
        }else{
            throw new NotFoundException("Cannot find User.");
        }
    }

    @Override
    public User joinPoolLeader(Long id, Long poolId) {
        if(userRepository.findById(id).isPresent()){
            if(cartPoolRepository.findById(poolId).isPresent()){
                CartPool cartPool = cartPoolRepository.findById(poolId).get();
                if(userRepository.findById(cartPool.getLeader()).isPresent()){
                    User leader = userRepository.findById(cartPool.getLeader()).get();
                    User user = userRepository.findById(id).get();
                    user.setPoolId(poolId);
                    user.setPoolStatus("LEADER");
                    sendJoinPoolEmailLeader(leader.getEmail(), user.getScreenName(), user.getId(), poolId);
                    return userRepository.save(user);
                }else{
                    throw new NotFoundException("Cannot find pool leader");
                }
            }else{
                throw new NotFoundException("Cart Pool does not exist.");
            }
        }else{
            throw new NotFoundException("Cannot find User.");
        }
    }

    @Override
    public User verifyJoinPoolRef(Long userId, Long poolId, boolean join) {
        if(userRepository.findById(userId).isPresent()){
            if(cartPoolRepository.findById(poolId).isPresent()){
                CartPool cartPool = cartPoolRepository.findById(poolId).get();
                if(userRepository.findById(cartPool.getLeader()).isPresent()){
                    if(join){
                        User leader = userRepository.findById(cartPool.getLeader()).get();
                        User user = userRepository.findById(userId).get();
                        user.setPoolId(poolId);
                        user.setPoolStatus("LEADER");
                        sendJoinPoolEmailLeader(leader.getEmail(), user.getScreenName(), user.getId(), poolId);
                        return userRepository.save(user);
                    }else{
                        User user = userRepository.findById(userId).get();
                        user.setPoolId(null);
                        user.setPoolStatus("INIT");
                        return userRepository.save(user);
                    }
                }else{
                    throw new NotFoundException("Cannot find pool leader");
                }
            }else{
                throw new NotFoundException("Cart Pool does not exist.");
            }
        }else{
            throw new NotFoundException("Cannot find User.");
        }
    }

    @Override
    public User verifyJoinPoolLeader(Long userId, Long poolId, boolean join) {
        if(userRepository.findById(userId).isPresent()){
            if(cartPoolRepository.findById(poolId).isPresent()){
                if(join){
                    User user = userRepository.findById(userId).get();
                    user.setPoolId(poolId);
                    user.setPoolStatus("ACTIVE");
                    return userRepository.save(user);
                }else{
                    User user = userRepository.findById(userId).get();
                    user.setPoolId(null);
                    user.setPoolStatus("INIT");
                    return userRepository.save(user);
                }
            }else{
                throw new NotFoundException("Cart Pool does not exist.");
            }
        }else{
            throw new NotFoundException("Cannot find User.");
        }
    }

    @Override
    public User leavePool(Long userId, Long poolId) {
        if(userRepository.findById(userId).isPresent()){
            if(orderRepository.findAllByUserIdAndPoolIdAndStatusNot(userId, poolId, OrderStatus.DELIVERED).size()==0){
                User user = userRepository.findById(userId).get();
                user.setPoolId(null);
                user.setPoolStatus("INIT");
                return userRepository.save(user);
            }else{
                throw new ConflictException("Cannot leave pool, since you have unfinished order in this pool");
            }
        }else{
            throw new NotFoundException("Cannot find User.");
        }
    }

    @Override
    public User sendEmail(Long userId, String toUserScreenName, String subject, String content) {
        if(userRepository.findById(userId).isPresent()){
            User user = userRepository.findById(userId).get();
            if(userRepository.existsByScreenName(toUserScreenName)){
                User toUser = userRepository.findByScreenName(toUserScreenName);
                mailService.sendHtmlMail(toUser.getEmail(), subject, content+"<p>Sent from CartPool user: "+ user.getScreenName() +"</p>");
                return user;
            }else{
                throw new NotFoundException("Cannot find user "+toUserScreenName);
            }
        }else {
            throw new NotFoundException("Cannot find user");
        }
    }

    public void sendVerificationEmail(String to, String token){
        String subject = "Registration Confirmation";
        String content = "<p>Thanks for registering to CartPool!</p>" +
                "<p>Please verify your email at: <a href='"+ UtilService.WEB_URL +"/verification?token=" + token + "'>Link</a></p>";
        mailService.sendHtmlMail(to, subject, content);
    }

    public void sendJoinPoolEmailRef(String to, String userScreenName, Long userId, Long poolId){
        String subject = "Joining Pool Request";
        String content = "<p>User "+ userScreenName + " requests to join pool.</p>" +
                "<p>If you support, please click: <a href='"+ UtilService.WEB_URL +"/verifyjoinpoolref?userId=" + userId +"&poolId=" + poolId + "&join=true'>Support</a></p>"+
                "<p>If you do not support, please click: <a href='"+ UtilService.WEB_URL +"/verifyjoinpoolref?userId="  + userId +"&poolId=" + poolId + "&join=false'>Not Support</a></p>";
        mailService.sendHtmlMail(to, subject, content);
    }

    public void sendJoinPoolEmailLeader(String to, String userScreenName, Long userId, Long poolId){
        String subject = "Joining Pool Request";
        String content = "<p>User "+ userScreenName + " requests to join pool.</p>" +
                "<p>If you approve, please click: <a href='"+ UtilService.WEB_URL +"/verifyjoinpoolleader?userId=" + userId +"&poolId=" + poolId + "&join=true'>Approve</a></p>"+
                "<p>If you do not approve, please click: <a href='"+ UtilService.WEB_URL +"/verifyjoinpoolleader?userId=" + userId +"&poolId=" + poolId + "&join=false'>Not Approve</a></p>";
        mailService.sendHtmlMail(to, subject, content);
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
