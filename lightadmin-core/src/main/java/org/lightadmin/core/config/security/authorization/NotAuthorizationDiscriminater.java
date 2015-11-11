package org.lightadmin.core.config.security.authorization;

import javax.servlet.http.HttpServletRequest;

public interface NotAuthorizationDiscriminater {
	boolean isNotAuthorizationLink(HttpServletRequest request);
}
