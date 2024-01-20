package com.example.subscription.endpoints.rest;

import com.example.subscription.services.SubscriberService;
import com.example.subscription.services.dto.CreateSubscriberDto;
import com.example.subscription.services.dto.SubscriberDto;
import com.example.subscription.services.dto.UpdateSubscriberDto;
import com.example.subscription.services.specs.SubscriberSpecificationBuilder;
import com.example.subscription.services.specs.core.PaginationResult;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@RestController
@RequestMapping("/v1/subscribers")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "Authorization")
public class SubscriberResource {

    private final SubscriberService subscriberService;

    @GetMapping("/{id}")
    public ResponseEntity<SubscriberDto> get(@PathVariable("id") String id) {
        var response = subscriberService.get(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginationResult<SubscriberDto>> search(@Valid SubscriberSpecificationBuilder.SubscriberSearch search) {
        var response = subscriberService.search(search);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SubscriberDto> create(@RequestBody @Valid CreateSubscriberDto dto) {
        var response = subscriberService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<SubscriberDto> update(@RequestBody @Valid UpdateSubscriberDto dto) {
        var response = subscriberService.update(dto);
        return ResponseEntity.ok(response);
    }
}
