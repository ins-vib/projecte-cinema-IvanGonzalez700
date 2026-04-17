package com.daw.cinemadaw.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.cinemadaw.domain.cinema.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<OrderSummary> findAllProjectedBy();

    interface OrderSummary {
        Long getId();
        LocalDateTime getDatetime();
        Double getTotal();
        Long getUserId();
    }
}

