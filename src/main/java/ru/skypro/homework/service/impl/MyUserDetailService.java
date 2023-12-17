package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;

@Service
@Slf4j
public class MyUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override//вызываем в методе логин
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        UserEntity user = userRepository.findUserEntityByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }else {
//            log.info("Пользователь найден - {}", user);
        }
        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
