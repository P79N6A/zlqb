package com.zhiwang.zfm.config.shiro;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.common.util.security.MD5;
import com.zhiwang.zfm.entity.sys.Module;
import com.zhiwang.zfm.entity.sys.User;
import com.zhiwang.zfm.service.api.sys.ModuleService;
import com.zhiwang.zfm.service.api.sys.UserService;

/**
 * 认证
 * 
 * @author chenbo
 * @email 381756915@qq.com
 * @date 2018-01-5 19:22
 */
@Component
public class UserRealm extends AuthorizingRealm {
	private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);

//    @Autowired
//    private SysMenuService sysMenuService;
	@Autowired
	ModuleService<Module> moduleService;
	
	@Autowired
	UserService<User> userService;
    
    /**
     * 授权(验证权限时调用)
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
//		Long userId = 0L ;   // user.getUserId();

		//用户权限列表
//		Set<String> permsSet = sysMenuService.getUserPermissions(userId);
//		UserVO user =(UserVO) principals.getPrimaryPrincipal();
//		SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
		System.out.println("=========doGetAuthorizationInfo================");
		Set<String> permsSet = new HashSet<String>();
		try {
			UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
			permsSet =moduleService.getModuleListByRoleId(userVO.getRoleIdList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		return info;
	}

	/**
	 * 认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("=========doGetAuthenticationInfo认证(登录时调用)================");
		String username = (String) token.getPrincipal();
		if (Objects.isNull(username)) {
            return null;
        }
        String password = new String((char[]) token.getCredentials());
        
        //查询用户信息
        SysUserEntity user = new SysUserEntity();
        user.setUsername(username);
        user.setPassword(password);
		UserVO userVO=null;
		try {
			userVO = userService.getUserByLoginName(username);//不获取角色信息
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new IncorrectCredentialsException("系统异常");
		}
		if(Objects.isNull(userVO)) {
			throw new UnknownAccountException("账号不存在");
		}
		if(!Objects.equals(userVO.getStatus(), 1)) {
			throw new LockedAccountException("账号已被禁用");
		}
        //密码错误
		if(!(MD5.GetMD5Code(password)).equals(userVO.getPassword())) {//
            throw new IncorrectCredentialsException("账号或密码不正确");
        }
		ShiroUtils.setSessionAttribute(ShiroUtils.EMPLOYEE_SESSION, userVO);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        return info;
	}

	public static void main(String[] args) {

		System.out.println(new Sha256Hash(SysUserEntity.defaultPW).toHex());
		
		System.out.println(MD5.GetMD5Code("888888"));
		
	}
}
