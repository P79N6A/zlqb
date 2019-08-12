package com.nyd.member.ws.controller;


import com.nyd.member.model.BaseInfo;
import com.nyd.member.service.MemberInfoService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * Created by hwei on 2017/11/6.
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    private static Logger LOGGER = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    MemberInfoService memberInfoService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(value = "/fetch", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchMemberInfo(@RequestBody BaseInfo baseInfo) {
      return memberInfoService.fetchMemberInfo(baseInfo);
    }

    @RequestMapping(value = "/index" ,produces="text/plain;charset=UTF-8")
    @ResponseBody
    public String index() throws UnsupportedEncodingException {
        Jedis jedis=null;
        String re = "a";
        try {
            redisTemplate.opsForValue().set("aaaa1", "bb", 20, TimeUnit.MINUTES);
            re = (String) redisTemplate.opsForValue().get("aaaa1");
            LOGGER.info("&&&&&&&&&&&&&&&&" + re);
            jedis = jedisPool.getResource();
        }catch (Exception e){
            LOGGER.info("yichang",e);
            e.printStackTrace();
        }finally {
            if(jedis!=null){
                jedis.close();
            }

        }
        return re;


    }



}
