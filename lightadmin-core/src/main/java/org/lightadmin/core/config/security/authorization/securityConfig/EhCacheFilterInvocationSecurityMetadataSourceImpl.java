package org.lightadmin.core.config.security.authorization.securityConfig;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lightadmin.core.config.security.authorization.ActionPathParser;
import org.lightadmin.core.config.security.authorization.FilterInvocationWithPatternMetadataSource;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.Assert;


public class EhCacheFilterInvocationSecurityMetadataSourceImpl implements
		FilterInvocationSecurityMetadataSource {

	private final Log logger = LogFactory.getLog(this.getClass());
	//private PathMatcher pathMatcher = new AntPathMatcher();

	private FilterInvocationWithPatternMetadataSource delegate;
	private DefinitionSourceCache cache;
	private ActionPathParser actionPathParser;

	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		if ((object == null) || !this.supports(object.getClass())) {
			throw new IllegalArgumentException("Object must be a FilterInvocation");
		}

		String url = ((FilterInvocation) object).getRequestUrl().trim();//带参数的url
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		if(url.length()>0&&url.charAt(0)=='/'){
			url = url.substring(1);
		}
		String urlWithPathPrefix = url;
		if(url.length()<1||url.charAt(0)!='/'){
			urlWithPathPrefix = "/"+url;
		}

		String actionPath = actionPathParser.parse(url,request);//不带参数的url
		String actingPathWithPathPrefix = "/"+actionPath;
		if (logger.isDebugEnabled()){
			logger.debug("urlWithPathPrefix:"+urlWithPathPrefix);
			logger.debug("url:"+url);
			logger.debug("actionPath:"+actionPath);
			logger.debug("actingPathWithPathPrefix:"+actionPath);
		}

		Collection<ConfigAttribute> result = new HashSet<ConfigAttribute>();

		Collection<ConfigAttribute> urlConfigAttribute = cache.getDefinitionSource(url);//拿缓存
		Collection<ConfigAttribute> actionPathConfigAttribute = cache.getDefinitionSource(actionPath);//拿缓存

		if(urlConfigAttribute==null){
			urlConfigAttribute = new HashSet<ConfigAttribute>();
			urlConfigAttribute.addAll(delegate.getAttributes(new PathInfo(url,actionPath)));
			urlConfigAttribute.addAll(delegate.getAttributes(new PathInfo(urlWithPathPrefix,actionPath)));
			cache.putDefinitionSource(url,urlConfigAttribute);
			result.addAll(urlConfigAttribute);
		}else{
			result.addAll(urlConfigAttribute);
		}

		if(actionPathConfigAttribute==null){
			actionPathConfigAttribute = new HashSet<ConfigAttribute>();
			actionPathConfigAttribute.addAll(delegate.getAttributes(new PathInfo(actionPath,actionPath)));
			actionPathConfigAttribute.addAll(delegate.getAttributes(new PathInfo(actingPathWithPathPrefix,actionPath)));
			cache.putDefinitionSource(actionPath,actionPathConfigAttribute);
			result.addAll(actionPathConfigAttribute);
		}else{
			result.addAll(actionPathConfigAttribute);
		}

		Assert.notNull(result, "FilterInvocationSecurityMetadataSource " + delegate + " returned null for url " + url + ". " +
				"This is an interface contract violation");

		return result;
	}

	public void removeDefinitionSource(String path) {
		cache.removeDefinitionSource(path);
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return delegate.supports(clazz);
	}
	
	/*public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}*/

	public FilterInvocationWithPatternMetadataSource getDelegate() {
		return delegate;
	}

	public void setDelegate(FilterInvocationWithPatternMetadataSource delegate) {
		this.delegate = delegate;
	}

	public DefinitionSourceCache getCache() {
		return cache;
	}

	public void setCache(DefinitionSourceCache cache) {
		this.cache = cache;
	}

	public void setActionPathParser(ActionPathParser actionPathParser) {
		this.actionPathParser = actionPathParser;
	}

}
