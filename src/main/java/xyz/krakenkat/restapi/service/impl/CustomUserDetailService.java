package xyz.krakenkat.restapi.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.krakenkat.restapi.domain.model.User;
import xyz.krakenkat.restapi.domain.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", username));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), List.of());
    }
}
