package com.example.subscription.endpoints.rest;

import com.example.subscription.services.SubscriberService;
import com.example.subscription.services.dto.CreateSubscriberDto;
import com.example.subscription.services.dto.SubscriberDto;
import com.example.subscription.services.dto.UpdateSubscriberDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@RestController
@RequestMapping("/subscribers")
@RequiredArgsConstructor
@Slf4j
public class SubscriberResource {

    private final SubscriberService subscriberService;

    @GetMapping("/{id}")
    public ResponseEntity<SubscriberDto> get(@PathVariable("id") String id) {
        var response = subscriberService.get(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SubscriberDto> create(@RequestBody @Valid CreateSubscriberDto dto) {
        var response = subscriberService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<SubscriberDto> update(@RequestBody @Valid UpdateSubscriberDto dto) {
        var response = subscriberService.create(dto);
        return ResponseEntity.ok(response);
    }
}
