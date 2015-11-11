package org.lightadmin.core.config.security;

import org.lightadmin.core.config.security.authentication.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil{
	public static AuthenticatedUser getCurrentUser(){
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext==null){
			return null;
		}
		
		Authentication authentication = securityContext.getAuthentication();
		if(authentication==null){
			return null;
		}
		
		
		AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
   
		return authenticatedUser;
	}
	
	/**
	 * 清除用户信息缓存<br>
	 * 例如需要删除manager用户的缓存，使用默认缓存的：<code> SecurityUtil.removeUserFromCache("manager", null);</code>
	 *
	 * @param userid 当前登录用户的userid， 一般是警号。
	 * @param cacheBeanId 可以为空，也可以自行指定缓存的地方
	 *
	 */
	public static void removeUserFromCache(String userid, String cacheBeanId) {
		/*String BEAN_NAME = "userDetailsCache";
		if (null == cacheBeanId) {
			cacheBeanId = BEAN_NAME;
		}
		ApplicationContext context = ApplicationContextManagement.getApplicationContext();
		if(!context.containsBean(cacheBeanId)){
			return ;
		}
		UserCache userCache = (UserCache) context.getBean(cacheBeanId);
		if (null == userCache) {
			return ;
		}
		UserDetails userDetails = userCache.getUserFromCache(userid);
		if (null != userDetails) {
			userCache.removeUserFromCache(userid);
		}*/
	}
}