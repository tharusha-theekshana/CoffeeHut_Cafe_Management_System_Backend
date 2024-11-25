package com.cafe_mn_system.coffeehut_backend.Security;


import com.cafe_mn_system.coffeehut_backend.Repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    private com.cafe_mn_system.coffeehut_backend.Models.User user;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", email);
        user = userRepo.findByEmail(email);

        if (!Objects.isNull(user)) {
            return new User(email, user.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException(
                    "User not Found ... !"
            );
        }
    }

    public com.cafe_mn_system.coffeehut_backend.Models.User getUserDetails(){
        return user;
    }
}
