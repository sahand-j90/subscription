package com.example.subscription.security.userdetail;

import com.example.subscription.enums.UserAuthorityEnum;
import com.example.subscription.exceptions.BizException;
import com.example.subscription.exceptions.Errors;
import com.example.subscription.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Component
@RequiredArgsConstructor
public class CredentialsUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CredentialsUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userRepository.findById(username)
                .orElseThrow(() -> new BizException(Errors.INVALID_CREDENTIALS_EXCEPTION));

        return CredentialsUserDetails.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(UserAuthorityEnum.convertToGrantedAuthority(user.getAuthorities()))
                .enabled(user.getEnabled())
                .build();
    }
}
