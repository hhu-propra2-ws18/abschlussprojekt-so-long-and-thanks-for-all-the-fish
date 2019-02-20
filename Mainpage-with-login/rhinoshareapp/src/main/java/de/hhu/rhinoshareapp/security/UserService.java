package de.hhu.rhinoshareapp.security;

import de.hhu.rhinoshareapp.security.database.ServiceUser;
import de.hhu.rhinoshareapp.security.database.ServiceUserProvider;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("UserDetails")
public class UserService implements UserDetailsService{


    @Autowired
    private ServiceUserProvider users;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ServiceUser> user = users.findByUsername(username);
        if (user.isPresent()) {
            ServiceUser u = user.get();
            UserDetails userdetails = User.builder()
                    .username(u.getUsername())
                    .password(u.getPassword())
                    .authorities(u.getRole())
                    .build();
            return userdetails;
        }
        throw new UsernameNotFoundException("Invalid Username");
    }
}
