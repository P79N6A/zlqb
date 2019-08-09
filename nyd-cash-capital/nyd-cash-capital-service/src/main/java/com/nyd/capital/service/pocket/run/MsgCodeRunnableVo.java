package com.nyd.capital.service.pocket.run;

import com.nyd.capital.service.UserPocketService;
import com.nyd.capital.service.pocket.service.Pocket2Service;
import com.nyd.capital.service.pocket.util.PocketConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author liuqiu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MsgCodeRunnableVo {

    private WebDriver driver;
    private String code;
    private Pocket2Service pocket2Service;
    private UserPocketService userPocketService;
    private String userId;
    private String idNumber;
    private PocketConfig pocketConfig;
    private RedisTemplate redisTemplate;
}
