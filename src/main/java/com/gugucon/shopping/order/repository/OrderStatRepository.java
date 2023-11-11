package com.gugucon.shopping.order.repository;

import com.gugucon.shopping.order.domain.entity.OrderStat;
import com.gugucon.shopping.member.domain.vo.BirthYearRange;
import com.gugucon.shopping.member.domain.vo.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatRepository extends JpaRepository<OrderStat, Long> {

    @Query("UPDATE OrderStat os " +
            "SET os.count = os.count + :count " +
            "WHERE os.productId = :productId " +
            "AND os.birthYearRange = :birthYearRange " +
            "AND os.gender = :gender")
    @Modifying
    void updateOrderStatByCount(@Param("count") final Integer count,
                                @Param("productId") final Long productId,
                                @Param("birthYearRange") final BirthYearRange birthYearRange,
                                @Param("gender") final Gender gender);
}
