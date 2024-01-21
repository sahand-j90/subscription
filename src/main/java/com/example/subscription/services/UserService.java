package com.example.subscription.services;

import com.example.subscription.domains.SubscriberEntity_;
import com.example.subscription.domains.UserEntity;
import com.example.subscription.exceptions.BizException;
import com.example.subscription.exceptions.Errors;
import com.example.subscription.repositories.UserRepository;
import com.example.subscription.security.SecurityFacade;
import com.example.subscription.services.dto.ChangePasswordDto;
import com.example.subscription.services.dto.CreateUserDto;
import com.example.subscription.services.dto.UpdateUserDto;
import com.example.subscription.services.dto.UserDto;
import com.example.subscription.services.mapper.UserMapper;
import com.example.subscription.services.specs.UserSpecificationBuilder;
import com.example.subscription.services.specs.core.PaginationResult;
import com.example.subscription.services.validators.ChangePasswordValidation;
import com.example.subscription.services.validators.CreateUserValidation;
import com.example.subscription.services.validators.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityFacade securityFacade;

    @Transactional(readOnly = true)
    public UserDto get(String username) {

        var user = findEntity(username);

        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public PaginationResult<UserDto> search(UserSpecificationBuilder.UserSearch search) {

        var pageNumber = search.getPageNumber();
        var pageSize = search.getPageSize();

        var pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, SubscriberEntity_.CREATED_AT));
        var spec = new UserSpecificationBuilder().build(search);

        var page = userRepository.findAll(spec, pageRequest);
        var items = userMapper.toDto(page.getContent());

        return PaginationResult.<UserDto>builder()
                .currentPage(pageNumber)
                .totalItems(page.getTotalElements())
                .pageSize(pageSize)
                .items(items)
                .build();
    }

    @Transactional(rollbackFor = Throwable.class)
    public UserDto create(CreateUserDto createUser) {

        var user = userMapper.toEntity(createUser);

        new Validator()
                .with(new CreateUserValidation(user.getUsername(), userRepository))
                .validate();

        encodePassword(user, user.getPassword());
        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Transactional(rollbackFor = Throwable.class)
    public UserDto update(UpdateUserDto updateUser) {

        var user = findEntity(updateUser.getUsername())
                .toBuilder().build();

        userMapper.update(updateUser, user);
        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void changePassword(ChangePasswordDto dto) {

        var user = findEntity(dto.getUsername());

        new Validator()
                .with(new ChangePasswordValidation(user, dto.getOldPassword(), securityFacade))
                .validate();

        encodePassword(user, dto.getNewPassword());
        userRepository.save(user);
    }

    public UserEntity findEntity(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new BizException(Errors.USER_NOT_FOUND));
    }

    private void encodePassword(UserEntity user, String password) {
        var encodedPassword = securityFacade.encode(user.getUsername(), password);
        user.setPassword(encodedPassword);
    }
}
