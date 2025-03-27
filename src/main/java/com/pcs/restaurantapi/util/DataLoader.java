package com.pcs.restaurantapi.util;

import com.pcs.restaurantapi.model.Role;
import com.pcs.restaurantapi.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        List<String> roles = List.of("MANAGER", "CUSTOMER", "DELIVERY_CREW");

        for (String roleName : roles) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        System.out.println("Preloading role: " + roleName);
                        return roleRepository.save(new Role(roleName));
                    });
        }
    }
}

