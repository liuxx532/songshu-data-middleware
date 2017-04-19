package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.repository.AuthorRepository;
import com.comall.songshu.service.RevenueService;
import com.comall.songshu.web.rest.util.TargetsMap;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/index")
public class IndexResource {

    @Autowired
    private RevenueService revenueService;


    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    public IndexResource(RevenueService revenueService){
        this.revenueService = revenueService;
    }

    @GetMapping("")
    @Timed
    public Map<String,String> getTargets() {
        return TargetsMap.getTargets();
    }

    @PostMapping("/search")
    @Timed
    public Collection<String> getKeys() {
        return TargetsMap.getTargets().values();
    }


    @PostMapping("/query")
    @Timed
    public Object[] query(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody String requestBody) throws Exception{

        log.debug("[RequestBody] {}", requestBody);

        JSONObject obj = new JSONObject(requestBody);

        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        JSONObject range = (JSONObject)obj.get("range");


        Timestamp fromTime = Timestamp.valueOf(new DateTime(range.get("from")).toString(dateTimeFormat));
        Timestamp toTime = Timestamp.valueOf(new DateTime(range.get("to")).toString(dateTimeFormat));
        JSONArray targets = (JSONArray)obj.get("targets");
        JSONObject targetObj = (JSONObject)targets.get(0);
        String target = targetObj.get("target").toString();
        String platform = obj.get("platform").toString();

        if (target.equals("Revenue")) {
            return revenueService.getRevenue();
        }

        return null;
    }

}
