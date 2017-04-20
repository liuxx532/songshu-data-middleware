package com.comall.songshu.service;

import com.comall.songshu.SongshuDataMiddlewareApp;
import com.comall.songshu.domain.Author;
import com.comall.songshu.repository.GrossMarginRepository;
import com.comall.songshu.repository.RevenueRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.when;

/**
 * Created by liugaoyu on 2017/4/20.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SongshuDataMiddlewareApp.class)
public class GrossMarginServiceTest {

    @InjectMocks
    private GrossMarginService grossMarginService;

    @Mock
    private GrossMarginRepository grossMarginRepository;

    @Test
    public void getRevenue() throws Exception {
        List<Author> list = new ArrayList<>();
        list.add(new Author());
        list.add(new Author());

        when(grossMarginRepository.findAll()).thenReturn(list);

        assertArrayEquals(list.toArray(), grossMarginService.getGrossMargin());
    }
}
