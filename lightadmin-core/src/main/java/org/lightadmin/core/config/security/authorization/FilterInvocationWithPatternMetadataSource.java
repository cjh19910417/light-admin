package org.lightadmin.core.config.security.authorization;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public interface FilterInvocationWithPatternMetadataSource extends FilterInvocationSecurityMetadataSource{
	Map<String,Collection<ConfigAttribute>> getAllPatternAttributes();
}
