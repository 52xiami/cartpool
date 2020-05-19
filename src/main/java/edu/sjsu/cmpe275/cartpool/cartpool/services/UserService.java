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
    User joinPoolRef(Long id, Long poolId, String refScreenName);
    User joinPoolLeader(Long id, Long poolId);
    User verifyJoinPoolRef(Long userId, Long poolId, boolean join);
    User verifyJoinPoolLeader(Long userId, Long poolId, boolean join);
    User leavePool(Long userId, Long poolId);
    User sendEmail(Long userId, String toUserScreenName, String subject, String content);
}
