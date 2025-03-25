package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.model.Role;
import com.pcs.restaurantapi.model.RoleName;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Cacheable("roles")
    Optional<Role> findByName(RoleName name);
}
