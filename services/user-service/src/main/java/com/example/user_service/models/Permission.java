package com.example.user_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

/**
 * Represents a permission in the system.
 * This entity is used to define specific actions that can be performed
 * within the application, which can be assigned to roles.
 */
@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    /**
     * The unique identifier for the permission.
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "id")
    private UUID id;

    /**
     * The name of the permission.
     * This should be unique across all permissions.
     */
    @Column(name = "permission_name", nullable = false, unique = true)
    private String permissionName;

    /**
     * A description of the permission.
     * This provides additional context about what the permission allows.
     */
    @Column(name = "description")
    private String description;

    /**
     * Constructor to create a Permission with a name and description.
     *
     * @param permissionName The name of the permission.
     * @param description    A description of the permission.
     */
    public Permission(String permissionName, String description) {
        this.permissionName = permissionName;
        this.description = description;
    }
}
