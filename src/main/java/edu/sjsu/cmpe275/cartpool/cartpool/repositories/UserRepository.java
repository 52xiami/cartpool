package edu.sjsu.cmpe275.cartpool.cartpool.repositories;

import edu.sjsu.cmpe275.cartpool.cartpool.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByToken(String token);
    boolean existsByEmail(String email);
    boolean existsByScreenName(String screenName);
    User findByScreenName(String screenName);
    List<User> findByPoolId(Long poolId);
}
