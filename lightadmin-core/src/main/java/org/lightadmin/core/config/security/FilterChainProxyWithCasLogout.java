package org.lightadmin.core.config.security;

import java.io.IOException; 

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.FilterChainProxy;

public class FilterChainProxyWithCasLogout extends FilterChainProxy{
	
	private FilterChainProxy casLoginFilterChainProxy;
	private FilterChainProxy localLoginFilterChainProxy;
	
	
	private static final Log logger = LogFactory.getLog(FilterChainProxyWithCasLogout.class);

	 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
		 HttpServletRequest httpRequest = (HttpServletRequest)request;
		 HttpSession session= httpRequest.getSession();
		 String logoutParam = httpRequest.getParameter("logoutParam");
	     if(logoutParam!=null&&!logoutParam.equals("")){
	        logoutSystem(response, session, logoutParam);
	        return;
	     }else{
	    	 
	    	 super.doFilter(httpRequest, response, chain);
	     }
	 }
	 
	 private void logoutSystem(ServletResponse response, HttpSession session,
				String logoutParam) throws IOException {
			session.invalidate();
			int nextLogoutAddressEndIndex = logoutParam.indexOf(";");
			String nextLogoutAddress = null;
			String newLogoutParam = null;
			if(nextLogoutAddressEndIndex>-1){
				nextLogoutAddress = logoutParam.substring(0, nextLogoutAddressEndIndex);
				newLogoutParam = logoutParam.substring(nextLogoutAddressEndIndex+1);
				
			}else{
				nextLogoutAddress = logoutParam;
				//newLogoutParam = "http://192.168.14.33:4500/casserver/logout";
			}
			logger.debug(">>>>>>>>logout url:"+nextLogoutAddress+"?logoutParam="+newLogoutParam);
			((HttpServletResponse) response).sendRedirect(nextLogoutAddress+"?logoutParam="+newLogoutParam);
	}

	public FilterChainProxy getCasLoginFilterChainProxy() {
		return casLoginFilterChainProxy;
	}

	public void setCasLoginFilterChainProxy(
			FilterChainProxy casLoginFilterChainProxy) {
		this.casLoginFilterChainProxy = casLoginFilterChainProxy;
	}

	public FilterChainProxy getLocalLoginFilterChainProxy() {
		return localLoginFilterChainProxy;
	}

	public void setLocalLoginFilterChainProxy(
			FilterChainProxy localLoginFilterChainProxy) {
		this.localLoginFilterChainProxy = localLoginFilterChainProxy;
	}
}
