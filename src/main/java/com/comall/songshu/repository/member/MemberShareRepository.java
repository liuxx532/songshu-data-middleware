package com.comall.songshu.repository.member;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by huanghaizhou on 2017/5/4.
 */
public interface MemberShareRepository  extends JpaRepository<Author,Long> {
}
