package com.project.devblog.service;

import com.project.devblog.controller.dto.request.UserRequest;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
    }

    public UserEntity findById(Integer id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public void deleteById(Integer userId) {
        UserEntity user = findById(userId);
        user.setStatus(StatusUser.BANNED);
        userRepository.save(user);
    }

    public UserEntity update(Integer userId, UserRequest userRequest) {
        UserEntity user = findById(userId);

        String firstname = userRequest.getFirstname();
        if (firstname != null && !firstname.isEmpty()) {
            user.getPersonalInfo().setFirstname(firstname);
        }

        String lastname = userRequest.getLastname();
        if (lastname != null && !lastname.isEmpty()) {
            user.getPersonalInfo().setLastname(lastname);
        }

        String phone = userRequest.getPhone();
        if (phone != null && !phone.isEmpty()) {
            user.getPersonalInfo().setPhone(phone);
        }

        String info = userRequest.getInfo();
        if (info != null && !info.isEmpty()) {
            user.getPersonalInfo().setInfo(info);
        }

        String nickname = userRequest.getNickname();
        if (nickname != null && !nickname.isEmpty() && !userRepository.existsByPersonalInfoNickname(nickname)) {
            user.getPersonalInfo().setNickname(nickname);
        }

        userRepository.save(user);
        return user;
    }

    public Page<UserEntity> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
