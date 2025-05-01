package com.example.user_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

/**
 * Represents a role in the system.
 * This entity is used to define a set of permissions that can be assigned to users.
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    /**
     * The unique identifier for the role.
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "id")
    private UUID id;

    /**
     * The name of the role.
     * This should be unique across all roles.
     */
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    /**
     * A description of the role.
     * This provides additional context about what the role allows.
     */
    @Column(name = "description")
    private String description;

    /**
     * The permissions associated with this role.
     * This defines what actions can be performed by users with this role.
     */
    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions = new ArrayList<>();

    /**
     * Constructor to create a Role with a name and description.
     *
     * @param roleName     The name of the role.
     * @param description  A description of the role.
     * @param permissions  The permissions associated with this role.
     */
    public Role(String roleName, String description, List<Permission> permissions) {
        this.roleName = roleName;
        this.description = description;
        this.permissions = permissions;
    }


}
