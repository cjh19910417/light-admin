package org.lightadmin.core.config.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

public class RdbmsUserDetailsServiceImpl extends JdbcDaoImpl{

    private static final Log logger = LogFactory.getLog(RdbmsUserDetailsServiceImpl.class);
	
	private String useridBySFZQuery;
	
    protected List<UserDetails> loadUsersByUsername(String username) {
        return getJdbcTemplate().query(getUsersByUsernameQuery(), new String[] {username}, new RowMapper<UserDetails>() {
            public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                String username = rs.getString(1);
                String password = rs.getString(2);
                boolean enabled = rs.getBoolean(3);
                String dept = rs.getString(4);
                String sys_reserver3 = rs.getString(5);
                String userRealName = rs.getString(6);
                String jh = rs.getString(7);
                String workdept = rs.getString(8);
                String zjhm = rs.getString(9);
                String isDigitalCertificateSigned = rs.getString("SYS_RESERVER19");//是否已签发数字证书

                AuthenticatedUser user = new AuthenticatedUser(username, password,dept,sys_reserver3, userRealName,
                		jh,getCodeWithoutZero(dept, 2, "00") + "%",workdept,getCodeWithoutZero(workdept, 2, "00") + "%"  ,zjhm,enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES,
                		isDigitalCertificateSigned);
                return user;
            }
        });
    }
    
    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery,
            List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromUserQuery.getUsername();
        AuthenticatedUser authenticatedUser = (AuthenticatedUser)userFromUserQuery;
        
        if (!isUsernameBasedPrimaryKey()) {
            returnUsername = username;
        }
        return new AuthenticatedUser(returnUsername, authenticatedUser.getPassword(),authenticatedUser.getDept(),authenticatedUser.getSys_reserver3(), 
        		authenticatedUser.getUserRealName(),authenticatedUser.getJh(),authenticatedUser.getDeptzero(), authenticatedUser.getWorkdept(), authenticatedUser.getWorkdeptzero(), authenticatedUser.getZjhm(), authenticatedUser.isEnabled(),true, true, true, combinedAuthorities,authenticatedUser.getIsDigitalCertificateSigned());
    }
    
    protected List<GrantedAuthority> loadUserAuthorities(final String username) {
        return getJdbcTemplate().query(getAuthoritiesByUsernameQuery(), new String[] {username}, new RowMapper<GrantedAuthority>() {
            public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
                String roleName = rs.getString(2);
                if(roleName==null){
                	logger.error("认证时发现 "+username+" 所对应的角色中有角色名为空的数据");
                	return null;
                	//throw new SecurityException("认证时发现 "+username+" 所对应的角色中有角色名为空的数据",SecurityException.authenticationErrorCode);
                }
                roleName = getRolePrefix() + roleName.trim();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

                return authority;
            }
        });
    }
    
    public String getUseridBySFZ(String sfz){
    	
    	List<String> userIds = getJdbcTemplate().query(useridBySFZQuery, new String[] {sfz}, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String userId = rs.getString(1);
                return userId;
            }
        });
    	if(userIds.size()>0){
    		return userIds.get(0);
    	}else{
    		return null;
    	}
    }

	public String getUseridBySFZQuery() {
		return useridBySFZQuery;
	}

	public void setUseridBySFZQuery(String useridBySFZQuery) {
		this.useridBySFZQuery = useridBySFZQuery;
	}
	
	
	/**
	 * 用于截取字符串。 <br>
	 * 例如截取部门编号： 442012100000。 以2个00为截取，以此达到获取上级部门的编号。
	 * @description
	 * @param code 被截取的对象。 例如部门编码、工作部门编码之类的。
	 * @param cutSize 截取的长度
	 * @param cutWhat 截取的东西。 例如截取部门编码中的00.
	 * @return 返回截取好的字符串。 
	 */
	private String getCodeWithoutZero(String code, int cutSize, String cutWhat) {
		
		if (null == code || code.length() % cutSize != 0 || code.length() < cutSize+1) {
			return code;
		}
		int length = code.length();
		
		if (cutWhat.equals(code.substring(length - cutSize, length))) {
		    return getCodeWithoutZero(code.substring(0, length-cutSize), cutSize, cutWhat);
		} else {
			return code;
		}
	}
}