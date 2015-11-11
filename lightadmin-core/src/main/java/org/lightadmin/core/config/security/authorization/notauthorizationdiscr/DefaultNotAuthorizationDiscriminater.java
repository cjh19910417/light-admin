package org.lightadmin.core.config.security.authorization.notauthorizationdiscr;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lightadmin.core.config.security.authorization.NotAuthorizationDiscriminater;


public class DefaultNotAuthorizationDiscriminater implements NotAuthorizationDiscriminater {
	
	private static final Log logger = LogFactory.getLog(DefaultNotAuthorizationDiscriminater.class);
	private List<NotAuthorizationDiscriminater> otherNotAuthorizationDiscriminaters;
	private Pattern pattern;
	
	public boolean isNotAuthorizationLink(HttpServletRequest request) {
		String requetPath = request.getRequestURI();
		if(logger.isDebugEnabled()){
		    logger.debug(">>>>>>>>>requetPath is:"+requetPath);
		}
		
		if(otherNotAuthorizationDiscriminaters!=null){
			for(NotAuthorizationDiscriminater notAuthorizationDis : otherNotAuthorizationDiscriminaters){
				if(notAuthorizationDis.isNotAuthorizationLink(request)){
					return true;
				}
			}
		}
		
		if(pattern!=null){
			return pattern.matcher(requetPath).matches();
		}
		
		return false;
	}
	public List<NotAuthorizationDiscriminater> getOtherNotAuthorizationDiscriminaters() {
		return otherNotAuthorizationDiscriminaters;
	}
	public void setOtherNotAuthorizationDiscriminaters(
			List<NotAuthorizationDiscriminater> otherNotAuthorizationDiscriminaters) {
		this.otherNotAuthorizationDiscriminaters = otherNotAuthorizationDiscriminaters;
	}
	
	public void setIgnoreLinks(String ignoreLinks) {
		if(ignoreLinks!=null&&!ignoreLinks.equals("")){
		    this.pattern = Pattern.compile(ignoreLinks);
		}
	}

}
