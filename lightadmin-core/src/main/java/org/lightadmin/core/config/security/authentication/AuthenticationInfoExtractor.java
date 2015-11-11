package org.lightadmin.core.config.security.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

public interface AuthenticationInfoExtractor {
	public Authentication extractAuthenticationInfo(HttpServletRequest request);
}
