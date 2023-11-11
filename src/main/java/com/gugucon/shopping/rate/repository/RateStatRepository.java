package com.gugucon.shopping.rate.repository;

import com.gugucon.shopping.rate.domain.entity.RateStat;
import com.gugucon.shopping.member.domain.vo.BirthYearRange;
import com.gugucon.shopping.member.domain.vo.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RateStatRepository extends JpaRepository<RateStat, Long> {

    @Query("UPDATE RateStat rs " +
            "SET rs.totalScore = rs.totalScore + :score, rs.count = rs.count + 1 " +
            "WHERE rs.productId = :productId " +
            "AND rs.birthYearRange = :birthYearRange " +
            "AND rs.gender = :gender")
    @Modifying
    void updateRateStatByScore(@Param("score") Short score,
                               @Param("productId") Long productId,
                               @Param("birthYearRange") BirthYearRange birthYearRange,
                               @Param("gender") Gender gender);

}
