package edu.sjsu.cmpe275.cartpool.cartpool.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="screen_name")
    private String screenName;

    @Column(name="nickname")
    private String nickname;

    @Column(name="pool_id")
    private Long poolId;

    @Column(name="pool_status")
    private String poolStatus;

    @Column(name="contribution")
    private Integer contribution;

    @Column(name="role")
    private String role;

    @Column(name="active")
    private Boolean active;

    @Column(name="token")
    private String token;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="creation_time")
    private LocalDateTime creationTime = LocalDateTime.now();

}
