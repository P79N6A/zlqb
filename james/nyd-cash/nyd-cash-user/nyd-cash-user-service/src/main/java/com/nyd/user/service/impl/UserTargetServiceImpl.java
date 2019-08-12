package com.nyd.user.service.impl;

import com.nyd.user.dao.mapper.UserTargetMapper;
import com.nyd.user.entity.UserTarget;
import com.nyd.user.model.enums.RuleEnum;
import com.nyd.user.service.UserTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserTargetServiceImpl implements UserTargetService{
    private static final Logger logger = LoggerFactory.getLogger(UserTargetServiceImpl.class);

    @Autowired
    private UserTargetMapper userTargetMapper;

    @Override
    public List<UserTarget> findByMobile(String mobile) {
        List<UserTarget> list = userTargetMapper.selectByMobile(mobile);
        return list;
    }

    @Override
    public void updateUserTarget(UserTarget userTarget) {
        userTargetMapper.updateUserTargetByMobile(userTarget);
    }

    @Override
    public void save(UserTarget userTarget) {
        userTargetMapper.insert(userTarget);
    }

    @Override
    public List toHitByRuleCodeAndMobile(Map<String, String> params) {
        String ruleCode = params.get("ruleCode");
        String mobile = params.get("mobile");
        String type = params.get("type");
        if (ruleCode.equals(RuleEnum.Rule1.getCode()) && "1".equals(type)){             //规则1和普通手机号(30天活跃且资料完整)
            List<UserTarget> list = userTargetMapper.findByRuleOneAndTypeOne(params);
            return list;

        }else if (ruleCode.equals(RuleEnum.Rule1.getCode()) && "2".equals(type)){       //规则1和MD5手机号
            List<UserTarget> list = userTargetMapper.findByRuleOneAndTypeTwo(params);
            return list;

        }else if (ruleCode.equals(RuleEnum.Rule1.getCode()) && "3".equals(type)){       //规则1和sha手机号
            List<UserTarget> list = userTargetMapper.findByRuleOneAndTypeThree(params);
            return list;

        } else if (ruleCode.equals(RuleEnum.Rule2.getCode()) && "1".equals(type)){      //规则2和普通手机号(60天活跃且资料完整)
            List<UserTarget> list = userTargetMapper.findByRuleTwoAndTypeOne(params);
            return list;

        }else if (ruleCode.equals(RuleEnum.Rule2.getCode()) && "2".equals(type)){       //规则2和MD5手机号
            List<UserTarget> list = userTargetMapper.findByRuleTwoAndTypeTwo(params);
            return list;

        }else if (ruleCode.equals(RuleEnum.Rule2.getCode()) && "3".equals(type)){       //规则2和sha手机号
            List<UserTarget> list = userTargetMapper.findByRuleTwoAndTypeThree(params);
            return list;

        } else if (ruleCode.equals(RuleEnum.Rule3.getCode()) && "1".equals(type)){      //规则3和普通手机号(90天活跃且资料完整)
            List<UserTarget> list = userTargetMapper.findByRuleThreeAndTypeOne(params);
            return list;

        }else if (ruleCode.equals(RuleEnum.Rule3.getCode()) && "2".equals(type)){        //规则3和MD5手机号
            List<UserTarget> list = userTargetMapper.findByRuleThreeAndTypeTwo(params);
            return list;

        }else if (ruleCode.equals(RuleEnum.Rule3.getCode()) && "3".equals(type)){      //规则3和sha手机号
            List<UserTarget> list = userTargetMapper.findByRuleThreeAndTypeThree(params);
            return list;

        } else if (ruleCode.equals(RuleEnum.Rule4.getCode()) && "1".equals(type)){     //规则4(撞全库),普通手机号,撞t_user_target
            List<UserTarget> list = userTargetMapper.findByRuleFourAndTypeOne(params);
            return list;

        } else if (ruleCode.equals(RuleEnum.Rule4.getCode()) && "2".equals(type)){     //规则4(撞全库),MD5手机号,撞t_user_target
            List<UserTarget> list = userTargetMapper.findByRuleFourAndTypeTwo(params);
            return list;

        } else if (ruleCode.equals(RuleEnum.Rule4.getCode()) && "3".equals(type)){     //规则4(撞全库),sha手机号,撞t_user_target
            List<UserTarget> list = userTargetMapper.findByRuleFourAndTypeThree(params);
            return list;
        }
        return null;
    }

	@Override
	public List<UserTarget> getUserTarget(Map<String, String> params) {
		List<UserTarget> list =userTargetMapper.userTargetFindByMd5mobile(params);
		return list;
	}
}
