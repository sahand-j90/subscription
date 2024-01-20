package com.example.subscription.endpoints.rest;

import com.example.subscription.services.UserService;
import com.example.subscription.services.dto.ChangePasswordDto;
import com.example.subscription.services.dto.CreateUserDto;
import com.example.subscription.services.dto.UpdateUserDto;
import com.example.subscription.services.dto.UserDto;
import com.example.subscription.services.specs.UserSpecificationBuilder;
import com.example.subscription.services.specs.core.PaginationResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> get(@PathVariable("username") String username) {
        var response = userService.get(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginationResult<UserDto>> search(@Valid UserSpecificationBuilder.UserSearch search) {
        var response = userService.search(search);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid CreateUserDto dto) {
        var response = userService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody @Valid UpdateUserDto dto) {
        var response = userService.update(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok().build();
    }

}
