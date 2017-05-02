package com.comall.songshu.service;

import com.comall.songshu.SongshuDataMiddlewareApp;
import com.comall.songshu.domain.Author;
import com.comall.songshu.repository.index.OrderCountRepository;
import com.comall.songshu.service.index.OrderCountService;
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
public class OrderCountServiceTest {

    @InjectMocks
    private OrderCountService orderCountService;

    @Mock
    private OrderCountRepository orderCountRepository;


    @Test
    public void getOrder() throws  Exception{
        List<Author>list= new ArrayList();
        list.add(new Author());
        list.add(new Author());

        when(orderCountRepository.findAll()).thenReturn(list);

        assertArrayEquals(list.toArray(), orderCountService.getOrder());
    }



}
