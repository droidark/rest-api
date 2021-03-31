package xyz.krakenkat.restapi.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.krakenkat.restapi.domain.repository.UserRepository;

import java.util.ArrayList;

@AllArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        xyz.krakenkat.restapi.domain.model.User user = userRepository.findByName(username);

        if(user == null) {
            throw new UsernameNotFoundException(String.format("The username %username doesn't exist", username));
        }

        return new User(user.getName(), user.getPassword(), new ArrayList<>());
    }
}
