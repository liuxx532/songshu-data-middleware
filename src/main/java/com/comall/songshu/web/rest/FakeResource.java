package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.constants.CommonConstants;
import com.comall.songshu.constants.TrendConstants;
import com.comall.songshu.service.FakeDataService;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TargetsMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于测试数据
 * Created by liushengling on 2017/5/18.
 */
@RestController
@RequestMapping("/fake")
public class FakeResource {

    @Autowired
    private FakeDataService fakeDataService;

    @GetMapping("")
    @Timed
    public Map<String,String> getTargets() {
        return TargetsMap.fakeTargets();
    }

    @PostMapping("/search")
    @Timed
    public Collection<String> getKeys() {
        return TargetsMap.fakeTargets().keySet();
    }

    @PostMapping("/query")
    @Timed
    public String query(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestBody String requestBody) throws Exception{
        if(Optional.ofNullable(requestBody).isPresent()){

            JSONObject obj = new JSONObject(requestBody);



            //指标中文名称
            JSONArray targets = (JSONArray)obj.get("targets");
            JSONObject targetJsonObj = (JSONObject)targets.get(0);
            String target =  (String)targetJsonObj.get("target");

            String result = fakeDataService.getFakeData(target);
            if(result != null){
                return result;
            }

        }
        return  null;
    }

}
