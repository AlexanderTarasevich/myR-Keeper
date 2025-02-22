package by.tms.myRkeeper.repository;

import by.tms.myRkeeper.dto.SalesReport;
import by.tms.myRkeeper.entity.Order;
import by.tms.myRkeeper.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByWaiter(User waiter);

    @Query("SELECT new by.tms.myRkeeper.dto.SalesReport(u.username, SUM(o.totalPrice), COUNT(o.id)) " + "FROM Order o JOIN o.waiter u GROUP BY u.username")
    List<SalesReport> findSalesReport();
}
