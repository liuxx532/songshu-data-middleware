package com.comall.songshu.repository;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wdc on 2017/4/19.
 */

// TODO: Add AvgOrderRevenue entity
public interface AvgOrderRevenueRepository extends JpaRepository<Author, Long> {
}
