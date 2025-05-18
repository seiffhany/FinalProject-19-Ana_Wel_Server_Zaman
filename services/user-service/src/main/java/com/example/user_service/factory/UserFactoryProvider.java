package com.example.user_service.factory;

import com.example.user_service.models.Role;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Provider class that manages different user factories based on roles
 */
@Component
public class UserFactoryProvider {
    private final Map<Role, UserFactory> factoryMap;

    public UserFactoryProvider(AdminUserFactory adminUserFactory, RegularUserFactory regularUserFactory) {
        this.factoryMap = Map.of(
                Role.ADMIN, adminUserFactory,
                Role.USER, regularUserFactory);
    }

    /**
     * Gets the appropriate factory for the specified role
     * 
     * @param role The role for which to get the factory
     * @return The appropriate UserFactory implementation
     * @throws IllegalArgumentException if no factory is found for the specified
     *                                  role
     */
    public UserFactory getFactory(Role role) {
        UserFactory factory = factoryMap.get(role);
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for role: " + role);
        }
        return factory;
    }
}