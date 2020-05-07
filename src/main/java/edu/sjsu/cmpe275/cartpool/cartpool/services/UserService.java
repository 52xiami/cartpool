package edu.sjsu.cmpe275.cartpool.cartpool.services;

import edu.sjsu.cmpe275.cartpool.cartpool.models.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User getUser(Long id);
    User getUserByEmail(String email);
    User registerNewUser(User user);
    User verifyUser(String token);
    User joinCartPool(Long id, Long cartPoolId);
    User setContributionScore(Long id, Integer score);
    User firebaseLogin(User user);
}
