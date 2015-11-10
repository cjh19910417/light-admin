package org.lightadmin.core.config.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


/**
 * 
 * 
 */
public class RdbmsFilterInvocationDefinitionSource extends JdbcDaoSupport implements FilterInvocationWithPatternMetadataSource {

	private static final Log logger = LogFactory.getLog(RdbmsFilterInvocationDefinitionSource.class);
	
	private String rolePrefix = "ROLE_";
	
	private String definitionSourceByUrlQuery;
	
	private String definitionSourceByMatchingUrlQuery;
	
	private String definitionSourcePermitToAllQuery;

	private PathMatcher pathMatcher = new AntPathMatcher();
	
	private ActionPathParser actionPathParser;

	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException{
		if ((object == null) || !this.supports(object.getClass())) {
            throw new IllegalArgumentException("Object must be a FilterInvocation");
        }
		
		//String method = ((FilterInvocation) object).getHttpRequest().getMethod();
        //String path = url.trim()+","+method.trim();
		
		Collection<ConfigAttribute> resultConfigAttribute = new HashSet<ConfigAttribute>();
		String url = null;
		String actionPath = null;
		if(PathInfo.class.isAssignableFrom(object.getClass())){
			PathInfo pathInfo = (PathInfo) object;
			url = pathInfo.getUrl();
			actionPath = pathInfo.getActionPath();
			
		}else{
			url = ((FilterInvocation) object).getRequestUrl();
		}
		ConfigAttribute permitToAllConfigAttribute = getPermitToAllAttribute(actionPath);
		if(permitToAllConfigAttribute!=null){
			resultConfigAttribute.add(permitToAllConfigAttribute);
		}
		
		if(url!=null){
			Collection<ConfigAttribute> urlConfigAttribute =  null;
			urlConfigAttribute =  getURLAttributes(url);
		    if(urlConfigAttribute!=null){
		        resultConfigAttribute.addAll(urlConfigAttribute);
		    }
		    Collection<ConfigAttribute> patternConfigAttribute =  getPatternAttributes(url);
		    if(patternConfigAttribute!=null){
		        resultConfigAttribute.addAll(patternConfigAttribute);
		    }
		}
		
		return resultConfigAttribute;
		
	}
	
	private Collection<ConfigAttribute> getURLAttributes(final String url) throws IllegalArgumentException{
		logger.debug("definitionSourceByUrlQuery:"+definitionSourceByUrlQuery);
    	logger.debug("queryParameter:"+url+"%");
        return getJdbcTemplate().query(definitionSourceByUrlQuery, new String[] {url+"%"}, new RowMapper<ConfigAttribute>() {
        	
            public ConfigAttribute mapRow(ResultSet rs, int rowNum) throws SQLException {
                String role = rs.getString(1);
                if(role==null){
                	logger.error(url+"所授予的权限对应的角色中有角色名为空的数据");
                	return null;
                }
                role = role.trim();
                if(!"permitAll".equals(role)){
                    role = rolePrefix +role.trim();
                }
                String parameter = rs.getString(2);
                return new UrlWithParamSecurityConfig(role,parameter);
            }

        });
	}
	
	private Collection<ConfigAttribute> getPatternAttributes(String url) throws IllegalArgumentException{
		Collection<ConfigAttribute> resultConfigAttribute = new HashSet<ConfigAttribute>();
		Map<String,Collection<ConfigAttribute>> patternMap = getAllPatternAttributes();
    	for(Map.Entry<String,Collection<ConfigAttribute>> entry:patternMap.entrySet()){
    		String pattern = entry.getKey();
    		if(pathMatcher.match(pattern, url)){
    			Collection<ConfigAttribute> patternConfigAttribute = entry.getValue();
    			resultConfigAttribute.addAll(patternConfigAttribute);
    		}
    	}
		return resultConfigAttribute;
	}
	
	public Map<String,Collection<ConfigAttribute>> getAllPatternAttributes() throws IllegalArgumentException{
		
		final Map<String,Collection<ConfigAttribute>> resultAttributes= new HashMap<String,Collection<ConfigAttribute>>();
        
		getJdbcTemplate().query(definitionSourceByMatchingUrlQuery, new RowMapper<Object>() {
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                String pattern = rs.getString(1);
                if(pattern==null){
                	logger.error("权限中存在访问地址为空的数据");
                	return null;
                	//throw new SecurityException("权限中存在访问地址为空的数据",SecurityException.authorizationErrorCode);
                }
                String role = rs.getString(2);
                if(role==null){
                	logger.error("所授予的权限对应的角色中有角色名为空的数据");
                	return null;
                	//throw new SecurityException("所授予的权限对应的角色中有角色名为空的数据",SecurityException.authorizationErrorCode);
                }
                role = rolePrefix +role.trim();
                String parameter = rs.getString(3);
                Collection<ConfigAttribute> matchingConfigAttribute = resultAttributes.get(pattern);
                if(matchingConfigAttribute==null){
                	matchingConfigAttribute = new ArrayList<ConfigAttribute>();
                	resultAttributes.put(pattern,matchingConfigAttribute);
                }
                matchingConfigAttribute.add(new UrlWithParamSecurityConfig(role,parameter));
                return null;
            }

        });
        return resultAttributes;
	}
	
	private ConfigAttribute getPermitToAllAttribute(final String actionPath)throws IllegalArgumentException{
		
        if(actionPath==null||actionPath.equals("")||(actionPath.length()==1&&actionPath.equals("/"))){
        	return new UrlWithParamSecurityConfig("permitAll",null);
        }
        
        String actionPathMatch = "%"+actionPath+"%";
		List<ConfigAttribute> listConfigAttribute = getJdbcTemplate().query(
				definitionSourcePermitToAllQuery, new String[] {actionPathMatch}, 
						new RowMapper<ConfigAttribute>() {
            public ConfigAttribute mapRow(ResultSet rs, int rowNum) throws SQLException {
                int functionCoutnt = rs.getInt(1);
                if(functionCoutnt<1){
                	return new UrlWithParamSecurityConfig("permitAll",null);
                }else{
                	return null;
                }
            }

        });
		if(listConfigAttribute.size()>0){
			return listConfigAttribute.get(0);
		}else{
			return null;
		}
	}

	public Collection<ConfigAttribute> getAllConfigAttributes(){
        return null;
	}

	public boolean supports(Class clazz) {
		return PathInfo.class.isAssignableFrom(clazz)||FilterInvocation.class.isAssignableFrom(clazz);
	}

	public void setDefinitionSourceByUrl(String definitionSourceByUrl) {
		this.definitionSourceByUrlQuery = definitionSourceByUrl;
	}

	public String getRolePrefix() {
		return rolePrefix;
	}

	public void setRolePrefix(String rolePrefix) {
		this.rolePrefix = rolePrefix;
	}
	
	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	public void setActionPathParser(ActionPathParser actionPathParser) {
		this.actionPathParser = actionPathParser;
	}

	public void setDefinitionSourceByUrlQuery(String definitionSourceByUrlQuery) {
		this.definitionSourceByUrlQuery = definitionSourceByUrlQuery;
	}

	public void setDefinitionSourceByMatchingUrlQuery(
			String definitionSourceByMatchingUrlQuery) {
		this.definitionSourceByMatchingUrlQuery = definitionSourceByMatchingUrlQuery;
	}

	public void setDefinitionSourcePermitToAllQuery(
			String definitionSourcePermitToAllQuery) {
		this.definitionSourcePermitToAllQuery = definitionSourcePermitToAllQuery;
	}
}