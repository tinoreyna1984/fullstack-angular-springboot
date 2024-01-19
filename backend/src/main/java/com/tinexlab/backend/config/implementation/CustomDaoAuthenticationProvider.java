package com.tinexlab.backend.config.implementation;

import com.tinexlab.backend.model.entity.User;
import com.tinexlab.backend.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

// implementación con clases Custom
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private final UserRepository userRepository;

    //!! No usar @Autowired, generar constructor con userRepository
    public CustomDaoAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
                                                  throws AuthenticationException {

        final int MAX_LOGIN_ATTEMPTS = 3;

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        int failedAttempts = userRepository.getFailedAttempts(username);
        boolean credencialesValidas = getPasswordEncoder().matches(
                authentication.getCredentials().toString(),
                user.getPassword()
        );
        boolean isAccountNotLocked = userRepository.isAccountNotLocked(username);

        try {
            if(!isAccountNotLocked) {
                throw new LockedException("Cuenta bloqueada. Solicitar su desbloqueo al administrador");
            }
            else {
                if(!credencialesValidas) {
                    ++failedAttempts;
                    user.setFailedAttempts(failedAttempts);
                    userRepository.save(user);
                    if(failedAttempts >= MAX_LOGIN_ATTEMPTS){
                        user.setLockTime(new Date());
                        user.setAccountNonLocked(false);
                        userRepository.save(user);
                        throw new LockedException("Cuenta bloqueada. Solicitar su desbloqueo al administrador");
                    }
                    throw new BadCredentialsException("Error en usuario y/o contraseña");
                }
                else{
                    if(failedAttempts > 0) {
                        failedAttempts = 0;
                        user.setFailedAttempts(failedAttempts);
                        userRepository.save(user);
                    }
                }
            }
        } catch (AuthenticationException e) {
            System.err.println(e.getMessage());
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
