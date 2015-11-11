package org.lightadmin.core.config.security.authorization;

import javax.servlet.http.HttpServletRequest;

public interface ActionPathParser {

	public String parse (String accessPath, HttpServletRequest request);
	
}
