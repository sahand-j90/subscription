package com.example.subscription.security.authorization;

import com.example.subscription.enums.AuthenticationFlowEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Component
public class AuthorizationCommandFactory {


    private final Map<AuthenticationFlowEnum, AuthorizationCommand> authorizationCommandMap;

    public AuthorizationCommandFactory(List<AuthorizationCommand> authorizationCommands) {
        this.authorizationCommandMap = authorizationCommands.stream()
                .collect(Collectors.toMap(AuthorizationCommand::authenticationFlow, Function.identity()));
    }

    public Optional<AuthorizationCommand> getCommand(AuthenticationFlowEnum authenticationFlow) {
        return Optional.ofNullable(authorizationCommandMap.get(authenticationFlow));
    }
}
