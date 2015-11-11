package org.lightadmin.core.config.security.authorization.securityConfig;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface DefinitionSourceCache {
	
	/**
     
     */
	Collection<ConfigAttribute> getDefinitionSource(String path);

    /**
     
     */
    void putDefinitionSource(String path,Collection<ConfigAttribute> configAttributes);

    /**
     
     */
    void removeDefinitionSource(String path);

}
