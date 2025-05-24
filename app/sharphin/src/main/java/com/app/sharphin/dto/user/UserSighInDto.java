package com.app.sharphin.dto.user;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserSighInDto implements UserDetails{
    private String user_id;
    private String username;
    private String password;
    private String email;
    private boolean disable;
    private Collection<GrantedAuthority> authorities;

    public UserSighInDto(UserDto user) {
        password = user.password();
        user_id = user.user_id();
        username = user.user_name();
        email = user.email();
        disable = user.disable();
        authorities = Collections.singleton(new SimpleGrantedAuthority(user.authority()));
    }
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }
    public String getUser_id() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public boolean isAccountNonLocked() {
        return true;
    }
    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isDisabled() {
        return !disable;
    }
}
