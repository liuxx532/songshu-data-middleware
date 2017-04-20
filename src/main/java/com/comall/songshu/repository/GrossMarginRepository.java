package com.comall.songshu.repository;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by liugaoyu on 2017/4/20.
 */
public interface GrossMarginRepository extends JpaRepository<Author,Long> {
}
