package org.lightadmin.core.config.security;

import javax.servlet.http.HttpServletRequest;

public interface ActionPathParser {

	public String parse (String accessPath, HttpServletRequest request);

}
