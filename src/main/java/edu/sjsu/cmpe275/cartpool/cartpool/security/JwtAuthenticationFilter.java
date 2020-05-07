package edu.sjsu.cmpe275.cartpool.cartpool.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sjsu.cmpe275.cartpool.cartpool.exceptions.ResponseBody;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        //JwtUser loginUser = new ObjectMapper().readValue(request.getInputStream(), JwtUser.class);
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getParameter("username"), request.getParameter("password"), new ArrayList<>())
        );

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();

        String role = "";

        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for (GrantedAuthority authority : authorities){
            role = authority.getAuthority();
        }

        String token = JwtTokenUtil.generateJsonWebToken(jwtUser.getUsername(), role);

        response.setHeader("token", JwtTokenUtil.TOKEN_PREFIX + token);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "token");
        response.setContentType("application/json; charset=utf-8");

        ResponseBody responseBody = new ResponseBody(LocalDateTime.now().toString(),HttpServletResponse.SC_OK, "Authentication passed", JwtTokenUtil.TOKEN_PREFIX + token);

        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=utf-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        ResponseBody responseBody = new ResponseBody(LocalDateTime.now().toString(),HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed", failed.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
    }
}
