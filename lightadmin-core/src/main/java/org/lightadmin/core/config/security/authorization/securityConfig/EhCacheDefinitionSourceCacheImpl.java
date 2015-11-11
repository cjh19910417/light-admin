package org.lightadmin.core.config.security.authorization.securityConfig;

import java.util.Collection;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;



public class EhCacheDefinitionSourceCacheImpl implements DefinitionSourceCache {
	
	private final Log logger = LogFactory.getLog(this.getClass());

    private Ehcache cache;

	public Collection<ConfigAttribute> getDefinitionSource(String path) {
		Element element = null;
		try{
			element = cache.get(path);
		}catch(CacheException cacheException){
			throw new DataRetrievalFailureException("Cache failure: " + cacheException.getMessage());
        }
		if (logger.isDebugEnabled()) {
            logger.debug("path: " + path+" is  hit in Cache: " + (element != null));
        }
		if(element!=null){
			return (Collection<ConfigAttribute>)element.getValue();
		}else{
			return null;
		}
	}

	public void putDefinitionSource(String path,
			Collection<ConfigAttribute> configAttributes) {
		Element element = new Element(path, configAttributes);

        if (logger.isDebugEnabled()) {
            logger.debug("Cache put: " + path+"="+configAttributes.toString());
        }

        cache.put(element);
	}

	public void removeDefinitionSource(String path) {
		if (logger.isDebugEnabled()) {
            logger.debug("Cache remove: " + path);
        }
		
		cache.remove(path);
	}

	public Ehcache getCache() {
		return cache;
	}

	public void setCache(Ehcache cache) {
		this.cache = cache;
	}

}
