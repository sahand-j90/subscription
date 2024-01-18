package com.example.subscription.services;

import com.example.subscription.domains.ApplicationEntity;
import com.example.subscription.repositories.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public List<ApplicationEntity> getAll() {
        return applicationRepository.findAll();
    }
}
