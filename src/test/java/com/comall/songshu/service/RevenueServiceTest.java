package com.comall.songshu.service;

import com.comall.songshu.SongshuDataMiddlewareApp;
import com.comall.songshu.domain.Author;
import com.comall.songshu.repository.index.RevenueRepository;
import com.comall.songshu.service.index.RevenueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by lgx on 17/4/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SongshuDataMiddlewareApp.class)
public class RevenueServiceTest {

    @InjectMocks
    private RevenueService revenueService;

    @Mock
    private RevenueRepository revenueRepository;

    @Test
    public void getRevenue() throws Exception {
        List<Author> list = new ArrayList<>();
        list.add(new Author());
        list.add(new Author());

        when(revenueRepository.findAll()).thenReturn(list);

        //assertArrayEquals(list.toArray(), revenueService.getRevenue());
    }

}
