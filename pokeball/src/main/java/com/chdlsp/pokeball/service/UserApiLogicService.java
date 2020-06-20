package com.chdlsp.pokeball.service;


import com.chdlsp.pokeball.model.entity.User;
import com.chdlsp.pokeball.model.enumClass.UserStatus;
import com.chdlsp.pokeball.model.network.Header;
import com.chdlsp.pokeball.model.network.request.UserApiRequest;
import com.chdlsp.pokeball.model.network.response.UserApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserApiLogicService extends BaseService<UserApiRequest, UserApiResponse, User> {

    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> ApiRequest) {

        UserApiRequest body = ApiRequest.getData();

        User user = User.builder()
                .account(body.getAccount())
                .password(body.getPassword())
                .status(UserStatus.REGISTERED)
                .phoneNumber(body.getPhoneNumber())
                .email(body.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();

        User newUser = baseRepository.save(user);

        return Header.OK(response(newUser));
    }

    @Override
    public Header<UserApiResponse> read(Long id) {
        return baseRepository.findById(id)
                .map(user -> response(user)) // map 을 통해 다른 type 으로 변환
                .map(userApiResponse -> Header.OK(userApiResponse))
                .orElseGet(()->Header.ERROR("Data Not Exists"));
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> ApiRequest) {

        UserApiRequest body = ApiRequest.getData();
        Optional<User> readUser = baseRepository.findById(body.getId());

        return readUser.map(user -> {
            user.setAccount(body.getAccount())
                    .setPassword(body.getPassword())
                    .setStatus(body.getStatus())
                    .setPhoneNumber(body.getPhoneNumber())
                    .setEmail(body.getEmail())
                    .setRegisteredAt(body.getRegisteredAt())
                    .setUnregisteredAt(body.getUnregisteredAt());
            return user;                                // new object return
        })
            .map(user -> baseRepository.save(user))     // update
            .map(updateUser -> response(updateUser))    // create userApiResponse
            .map(userApiResponse -> Header.OK(userApiResponse))
            .orElseGet(()->Header.ERROR("Data Not Exist"));
    }

    @Override
    public Header delete(Long id) {
        Optional<User> readUser = baseRepository.findById(id);

        return readUser.map(user->{
            baseRepository.delete(user);
            return Header.OK();
        })
        .orElseGet(()->Header.ERROR("Data Not Exist"));
    }

    private UserApiResponse response(User user) {

        // user object -> userApiResponse
        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword()) // TODO: encryption
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .build();

        // Header + Data
        return userApiResponse;
    }

        public Header<List<UserApiResponse>> search(Pageable pageable) {
            Page<User> users = baseRepository.findAll(pageable);
            List<UserApiResponse> userApiResponseList = users.stream()
                    .map(user -> response(user))
                    .collect(Collectors.toList());

            return Header.OK(userApiResponseList);
    }
}
