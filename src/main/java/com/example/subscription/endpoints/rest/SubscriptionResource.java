package com.example.subscription.endpoints.rest;

import com.example.subscription.services.SubscriptionService;
import com.example.subscription.services.dto.CreateSubscriptionDto;
import com.example.subscription.services.dto.SubscriberDto;
import com.example.subscription.services.dto.SubscriptionDto;
import com.example.subscription.services.dto.UpdateSubscriptionDto;
import com.example.subscription.services.specs.SubscriberSpecificationBuilder;
import com.example.subscription.services.specs.SubscriptionSpecificationBuilder;
import com.example.subscription.services.specs.core.PaginationResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@RestController
@RequestMapping("/v1/subscriptions")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionResource {

    private final SubscriptionService subscriptionService;

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> get(@PathVariable("id") String id) {
        var response = subscriptionService.get(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginationResult<SubscriptionDto>> search(@Valid SubscriptionSpecificationBuilder.SubscriptionSearch search) {
        var response = subscriptionService.search(search);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto> create(@RequestBody @Valid CreateSubscriptionDto dto) {
        var response = subscriptionService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<SubscriptionDto> update(@RequestBody @Valid UpdateSubscriptionDto dto) {
        var response = subscriptionService.update(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SubscriptionDto> delete(@PathVariable("id") String id) {
        subscriptionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
