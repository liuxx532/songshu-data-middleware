package com.comall.songshu.repository.member;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户注册数据
 *
 * @author liushengling
 * @create 2017-05-04-16:39
 **/
public interface ChannelRegisterMemberRepository extends JpaRepository<Author,Long> {



    @Query(value = " ", nativeQuery = true)
    List<Object[]> getChannelMemberRegisterCountAllPlatform(Timestamp beginTime, Timestamp endTime, Integer topCount);


    @Query(value = " ", nativeQuery = true)
    List<Object[]> getChannelMemberRegisterCountSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm, Integer topCount);
}
