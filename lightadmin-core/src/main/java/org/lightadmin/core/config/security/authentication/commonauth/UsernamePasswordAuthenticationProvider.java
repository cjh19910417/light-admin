package org.lightadmin.core.config.security.authentication.commonauth;

import org.lightadmin.core.config.security.authentication.AuthenticatedUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;


public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	private AuthenticationUserDetailsService authenticatedUserDetailsService;
	private PasswordEncoder passwordEncoder = new DigestPasswordEncoder();

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		AuthenticatedUser userDetails = (AuthenticatedUser)authenticatedUserDetailsService.loadUserDetails(authentication);
		if(userDetails == null){
			throw new BadCredentialsException("用户:"+authentication.getPrincipal()+" 常未在系统中配置");
		}

		//已签发数字证书不允许使用此方式登录
		if("1".equals(userDetails.getIsDigitalCertificateSigned())){
			throw new BadCredentialsException("该用户已经签发了数字证书，请用数字证书登录");
		}
		String password = (String)authentication.getCredentials();
		String encodePassword = userDetails.getPassword();
		String reEncodePassword = passwordEncoder.encodePassword(password, null);
		if(!reEncodePassword.equals(encodePassword)){
			throw new BadCredentialsException("密码有误");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
	}

	public boolean supports(Class<? extends Object> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	public AuthenticationUserDetailsService getAuthenticatedUserDetailsService() {
		return authenticatedUserDetailsService;
	}

	public void setAuthenticatedUserDetailsService(
			AuthenticationUserDetailsService authenticatedUserDetailsService) {
		this.authenticatedUserDetailsService = authenticatedUserDetailsService;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}
