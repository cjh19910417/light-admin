package org.lightadmin.core.config.security.authentication.x509auth;

import java.security.cert.X509Certificate;

import org.lightadmin.core.config.security.authentication.AuthenticatedUser;
import org.lightadmin.core.config.security.authentication.ExtendUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;


public class SSLAuthenticationProvider implements AuthenticationProvider {

	private X509PrincipalExtractor x509PrincipalExtractor = new PolicePKIX509PrincipalExtractor();

	private AuthenticationUserDetailsService authenticatedUserDetailsService;
	private ExtendUserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		X509AuthenticationToken x509AuthenticationToken = (X509AuthenticationToken)authentication;
		String sfzhm = (String)x509PrincipalExtractor.extractPrincipal((X509Certificate)x509AuthenticationToken.getCredentials());
		String userId = userDetailsService.getUseridBySFZ(sfzhm);
		x509AuthenticationToken.setPrincipal(userId);
		AuthenticatedUser userDetails = (AuthenticatedUser)authenticatedUserDetailsService.loadUserDetails(x509AuthenticationToken);
		if(userDetails == null){
			throw new BadCredentialsException("PKI 常未在系统中配置");
		}
		return new X509AuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {

		return (X509AuthenticationToken.class.isAssignableFrom(authentication));
	}

	public AuthenticationUserDetailsService getAuthenticatedUserDetailsService() {
		return authenticatedUserDetailsService;
	}

	public void setAuthenticatedUserDetailsService(
			AuthenticationUserDetailsService authenticatedUserDetailsService) {
		this.authenticatedUserDetailsService = authenticatedUserDetailsService;
	}

	public ExtendUserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(ExtendUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}



}

