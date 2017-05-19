package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.web.rest.util.TargetsMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于前端mock
 * Created by huanghaizhou on 2017/5/18.
 */
@RestController
@RequestMapping("/mock")
public class MockResource {
    private Map<String,Object> datas = new ConcurrentHashMap<>();
    private static final int TYPE_OBJ = 0;//jsonObject
    private static final int TYPE_ARR = 1;//jsonArray
    @GetMapping("")
    @Timed
    public Map<String,String> getTargets() {
        return TargetsMap.mockTargets();
    }

    @PostMapping("/search")
    @Timed
    public Collection<String> getKeys() {
        return TargetsMap.mockTargets().keySet();
    }

    @PostMapping("/query")
    @Timed
    public String query(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestBody String requestBody) throws Exception{
        JSONObject obj = new JSONObject(requestBody);
        String target = new JSONObject(obj.getJSONArray("targets").get(0).toString()).getString("target");
        String user = TargetsMap.mockTargets().get(target);
        Object o = datas.get(user);
        if(o != null) return o.toString();
        return "";
    }

    /**
     * 使用方式，传入：{"user":"userA","type":1,"data":[1,2,3],"format":"json","maxDataPoints":3,"groupType":"all","region":"all","platform":"all","cacheTimeout":null}
     * 以上例子仅需修改type,user,和data;
     * type 0 jsonobject,  1jsonarray.
     * data放具体数据
     * user和TargetsMap.mockTargets()对应。
     */
    @PostMapping("/injectMock")
    @Timed
    public String injectMock(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestBody String requestBody) throws Exception{
        JSONObject obj = new JSONObject(requestBody);
        String name = obj.getString("user");
        int type = obj.getInt("type");
        switch(type){
            case TYPE_OBJ:
                JSONObject jsonObject = obj.getJSONObject("data");
                this.datas.put(name,jsonObject);
                break;
            case TYPE_ARR:
                JSONArray jsonArray = obj.getJSONArray("data");
                this.datas.put(name,jsonArray);
                break;
        }
        return "ok";
    }
}
