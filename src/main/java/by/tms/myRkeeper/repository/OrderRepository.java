package by.tms.myRkeeper.repository;

import by.tms.myRkeeper.entity.Order;
import by.tms.myRkeeper.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByWaiterAndStatus(User waiter, String status);
}
