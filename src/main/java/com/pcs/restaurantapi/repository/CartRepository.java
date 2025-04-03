package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.model.Cart;
import com.pcs.restaurantapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByCustomer(User customer);

    @Query("SELECT c FROM Cart c WHERE c.customer.username = :username")
    Optional<Cart> findByCustomerUsername(@Param("username") String username);
}
