package edu.sjsu.cmpe275.cartpool.cartpool.controllers;

import edu.sjsu.cmpe275.cartpool.cartpool.models.User;
import edu.sjsu.cmpe275.cartpool.cartpool.services.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    /**
     * Retrieve all users
     * /api/user
     */
    @GetMapping("")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Retrieve specific user by id
     * /api/user/{id}
     */
    @GetMapping("{id}")
    public User getUser(@PathVariable("id") @NotNull Long id) {
        return userService.getUser(id);
    }

    /**
     * Retrieve specific user by email
     * /api/user/search
     */
    @GetMapping("/search")
    public User getUser(@RequestParam(value = "email") @Email @NotNull @Length(max = 128) String email) {
        return userService.getUserByEmail(email);
    }

    /**
     * Create a new user
     * /api/user/registration
     */
    @PostMapping("/registration")
    public User registerNewUser(@RequestParam(value = "email") @Email @NotNull @Length(max = 128) String email,
                           @RequestParam(value = "password") @NotNull @Length(max = 32) String password,
                           @RequestParam(value = "screenName") @NotNull @Length(max = 50) String screenName,
                           @RequestParam(value = "nickname", required = false) @Length(max = 50) String nickname) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setScreenName(screenName);
        user.setNickname(nickname);
        user.setContribution(0);

        userService.registerNewUser(user);

        return user;
    }

    /**
     * Verify user by token
     * /api/user/verification
     */
    @GetMapping("/verification")
    public User verifyUser(@RequestParam(value = "token") @NotNull @Length(max = 32) String token) {
        return userService.verifyUser(token);
    }


    /**
     * User joins pool
     * /api/user/joinpoolref
     */
    @GetMapping("/joinpoolref")
    public User joinPoolRef(@RequestParam(value = "userId") @NotNull Long userId,
                            @RequestParam(value = "poolId") @NotNull Long poolId,
                            @RequestParam(value = "refScreenName") @NotNull String refScreenName ) {
        return userService.joinPoolRef(userId, poolId, refScreenName);

    }

    /**
     * User joins pool
     * /api/user/joinpoolleader
     */
    @GetMapping("/joinpoolleader")
    public User joinPoolLeader(@RequestParam(value = "userId") @NotNull Long userId,
                               @RequestParam(value = "poolId") @NotNull Long poolId) {
        return userService.joinPoolLeader(userId, poolId);
    }

    /**
     * Verify joining pool request by reference
     * /api/user/verifyjoinpoolref
     */
    @GetMapping("/verifyjoinpoolref")
    public User verifyJoinPoolRef(@RequestParam(value = "userId") @NotNull Long userId,
                                  @RequestParam(value = "poolId") @NotNull Long poolId,
                                  @RequestParam(value = "join") @NotNull boolean join) {
        return userService.verifyJoinPoolRef(userId, poolId, join);
    }

    /**
     * Verify joining pool request by leader
     * /api/user/verifyjoinpoolleader
     */
    @GetMapping("/verifyjoinpoolleader")
    public User verifyJoinPoolLeader(@RequestParam(value = "userId") @NotNull Long userId,
                                     @RequestParam(value = "poolId") @NotNull Long poolId,
                                     @RequestParam(value = "join") @NotNull boolean join) {
        return userService.verifyJoinPoolLeader(userId, poolId, join);
    }

    /**
     * Join a CartPool
     * /api/user/joinpool
     */
    @PutMapping("/{id}/joinpool/{cartPoolId}")
    public User joinCartPool(@PathVariable("id") @NotNull Long id,
                             @PathVariable("cartPoolId") @NotNull Long cartPoolId ){


        return userService.joinCartPool(id,cartPoolId);
    }

    /**
     * Update contribution score
     * /api/user/contribution
     */
    @PutMapping("/{id}/contribution/{score}")
    public User setContributionScore(@PathVariable("id") @NotNull Long id,
                             @PathVariable("score") @NotNull Integer score ){

        return userService.setContributionScore(id,score);
    }

    /**
     * Update a user's role
     * /api/user/{id}/updaterole
     */
    @PutMapping("{id}/updaterole")
    public User updateRole(@PathVariable("id") @NotNull Integer id, @RequestParam(value = "poolId") @NotNull Long poolId){
        return null;
    }

    /**
     * Leave a pool
     * /api/user/{userId}/pool/{poolId}
     */
    @PutMapping("/{userId}/pool/{poolId}")
    public User leavePool(@PathVariable("userId") @NotNull Long userId,
                                     @PathVariable("poolId") @NotNull Long poolId ){

        return userService.leavePool(userId, poolId);
    }



    /**
     * Firebase Login
     * /api/user/firebaselogin
     */
    @PostMapping("/firebaselogin")
    public User firebaseLogin(@RequestParam(value = "email") @Email @NotNull @Length(max = 128) String email,
                                @RequestParam(value = "screenName") @NotNull @Length(max = 50) String screenName,
                                @RequestParam(value = "nickname", required = false) @Length(max = 50) String nickname) {

        User user = new User();
        user.setEmail(email);
        user.setScreenName(screenName);
        user.setNickname(nickname);

        userService.firebaseLogin(user);

        return user;
    }

    /**
     * Send email
     * /api/user/email
     */
    @PostMapping("/email")
    public User sendEmail(@RequestParam(value = "userId") Long userId,
                          @RequestParam(value = "toUserScreenName") String toUserScreenName ,
                          @RequestParam(value = "subject") String subject,
                          @RequestParam(value = "content", required = false) String content) {

        return userService.sendEmail(userId, toUserScreenName, subject, content);

    }

}
