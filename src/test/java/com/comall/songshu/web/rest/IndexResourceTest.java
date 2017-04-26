package com.comall.songshu.web.rest;

import com.comall.songshu.SongshuDataMiddlewareApp;
import com.comall.songshu.service.*;
import com.comall.songshu.web.rest.errors.ExceptionTranslator;
import com.comall.songshu.web.rest.util.TargetsMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lgx on 17/4/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SongshuDataMiddlewareApp.class)
public class IndexResourceTest {

    @InjectMocks
    private IndexResource indexResource;

    @Mock
    private RevenueService revenueService;

    @Mock
    private OrderCountService orderCountService;

    @Mock
    private AvgOrderRevenueService avgOrderRevenueService;

    @Mock
    private VisitorsService visitorsService;

    @Mock
    private RefundService refundService;

    @Mock
    private GrossMarginRateService grossMarginRateService;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restAuthorMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //IndexResource ir = new IndexResource(revenueService);
        IndexResource ir = new IndexResource(grossMarginRateService);





        this.restAuthorMockMvc = MockMvcBuilders.standaloneSetup(ir)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    public void getTargets() throws Exception {
        restAuthorMockMvc.perform(get("/index"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .equals(TargetsMap.getTargets());
    }

    @Test
    public void getKeys() throws Exception {
        restAuthorMockMvc.perform(post("/index/search"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .equals(TargetsMap.getTargets().values());
    }

    @Test
    public void query() throws Exception {
        //String requstBody = "{\"panelId\":10,\"range\":{\"from\":\"2017-03-01T16:00:00.000Z\",\"to\":\"2017-04-08T15:59:59.999Z\"},\"rangeRaw\":{\"from\":\"now-1w/w\",\"to\":\"now-1w/w\"},\"interval\":\"1d\",\"targets\":[{\"target\":\"Revenue\",\"refId\":\"A\"}],\"format\":\"json\",\"maxDataPoints\":3,\"platform\":\"ios\",\"cacheTimeout\":null}\n";
        String requstBody = "{\"panelId\":10,\"range\":{\"from\":\"2017-03-01T16:00:00.000Z\",\"to\":\"2017-04-08T15:59:59.999Z\"},\"rangeRaw\":{\"from\":\"now-1w/w\",\"to\":\"now-1w/w\"},\"interval\":\"1d\",\"targets\":[{\"target\":\"GrossMargin" +
            "\",\"refId\":\"A\"}],\"format\":\"json\",\"maxDataPoints\":3,\"platform\":\"ios\",\"cacheTimeout\":null}\n";

        String[] s = new String[2];
        s[0] = "1111";
        s[1] = "2222";

        //when(revenueService.getRevenue()).thenReturn(s);
        //when(grossMarginRateService.findGrossMarginRate().thenReturn(s));

        restAuthorMockMvc.perform(post("/index/query")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requstBody)))
            .andExpect(status().isOk())
            .equals(s);
    }

}
