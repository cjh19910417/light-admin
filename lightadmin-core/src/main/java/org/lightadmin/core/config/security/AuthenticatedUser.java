package org.lightadmin.core.config.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthenticatedUser extends User {

    private static final long serialVersionUID = 7720871443153270432L;

    private String dept;           //单位编号
    private String sys_reserver3;  //单位名称
    private String jh;             //警号
    private String userRealName;   //由于受springSecurity的影响父类的userName其实对应的是数据库中的USERID列,
    //而userRealName才对应数据库中的USERNAME列

    private String deptzero;       //单位编号截取00

    private String workdept;        //工作单位编号


    private String workdeptzero;    //工作单位编号截取00

    private String zjhm; //身份证号

    /**
     * 是否已签发数字证书
     */
    private String isDigitalCertificateSigned;

    public AuthenticatedUser(String username, String password, boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {

        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

    }

    public AuthenticatedUser(String username, String password, boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked, GrantedAuthority[] authorities) {
        this(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
                authorities == null ? null : Arrays.asList(authorities));
    }

    public AuthenticatedUser(String username, String password, String dept, String sys_reserver3, String userRealName,
                             String jh, String deptzero, String workdept, String workdeptzero, String zjhm, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                             boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.dept = dept;
        this.sys_reserver3 = sys_reserver3;
        this.userRealName = userRealName;
        this.jh = jh;
        this.deptzero = deptzero;
        this.workdept = workdept;
        this.workdeptzero = workdeptzero;
        this.zjhm = zjhm;

    }

    public AuthenticatedUser(String username, String password, String dept, String sys_reserver3, String userRealName,
                             String jh, String deptzero, String workdept, String workdeptzero, String zjhm, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                             boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                             String isDigitalCertificateSigned) {
        this(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.dept = dept;
        this.sys_reserver3 = sys_reserver3;
        this.userRealName = userRealName;
        this.jh = jh;
        this.deptzero = deptzero;
        this.workdept = workdept;
        this.workdeptzero = workdeptzero;
        this.zjhm = zjhm;
        this.isDigitalCertificateSigned = isDigitalCertificateSigned;
    }

    public AuthenticatedUser clone() {
        return new AuthenticatedUser(getUsername(), getPassword(), this.dept, this.sys_reserver3,
                this.userRealName, this.jh, this.deptzero, this.workdept, this.workdeptzero, this.zjhm, isEnabled(), isAccountNonExpired(),
                isCredentialsNonExpired(), isAccountNonLocked(), getAuthorities(),
                getIsDigitalCertificateSigned());
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getSys_reserver3() {
        return sys_reserver3;
    }

    public void setSys_reserver3(String sys_reserver3) {
        this.sys_reserver3 = sys_reserver3;
    }

    public String getJh() {
        return jh;
    }

    public void setJh(String jh) {
        this.jh = jh;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }


    public void setDeptzero(String deptzero) {
        this.deptzero = deptzero;
    }

    public String getDeptzero() {
        return deptzero;
    }


    public String getWorkdept() {
        return workdept;
    }

    public void setWorkdept(String workdept) {
        this.workdept = workdept;
    }

    public String getWorkdeptzero() {
        return workdeptzero;
    }

    public void setWorkdeptzero(String workdeptzero) {
        this.workdeptzero = workdeptzero;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getIsDigitalCertificateSigned() {
        return isDigitalCertificateSigned;
    }

    public void setIsDigitalCertificateSigned(String isDigitalCertificateSigned) {
        this.isDigitalCertificateSigned = isDigitalCertificateSigned;
    }

    @Override
    public String toString() {
        return "AuthenticatedUser [dept=" + dept + ", sys_reserver3=" + sys_reserver3 + ", jh=" + jh + ", userRealName=" + userRealName
                + ", deptzero=" + deptzero + ", workdept=" + workdept + ", workdeptzero=" + workdeptzero + ", zjhm=" + zjhm + ", isDigitalCertificateSigned=" + isDigitalCertificateSigned + "]";
    }
}