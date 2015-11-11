/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.config.context;

import com.google.common.collect.Lists;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.security.authentication.AuthenticationInfoExtractor;
import org.lightadmin.core.config.security.authentication.ExtendUserDetailsService;
import org.lightadmin.core.config.security.authentication.RdbmsUserDetailsServiceImpl;
import org.lightadmin.core.config.security.authentication.commonauth.CommonAuthenticationInfoExtractor;
import org.lightadmin.core.config.security.authentication.commonauth.UsernamePasswordAuthenticationProvider;
import org.lightadmin.core.config.security.authentication.web.LocalAuthenticationFilter;
import org.lightadmin.core.config.security.authentication.x509auth.SSLAuthenticationProvider;
import org.lightadmin.core.config.security.authentication.x509auth.X509AuthenticationInfoExtractor;
import org.lightadmin.core.config.security.authorization.ActionPathParser;
import org.lightadmin.core.config.security.authorization.FilterInvocationWithPatternMetadataSource;
import org.lightadmin.core.config.security.authorization.actionpathparser.LocalizationActionPathParser;
import org.lightadmin.core.config.security.authorization.securityConfig.DefinitionSourceCache;
import org.lightadmin.core.config.security.authorization.securityConfig.EhCacheFilterInvocationSecurityMetadataSourceImpl;
import org.lightadmin.core.config.security.authorization.securityConfig.RdbmsFilterInvocationDefinitionSource;
import org.lightadmin.core.config.security.authorization.vote.PermitAllVoter;
import org.lightadmin.core.web.security.LightAdminRequestCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.util.Arrays.asList;

/**
 * Spring Scurity配置
 */
@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
@PropertySource("classpath:security.properties")
public class LightAdminSecurityConfiguration {

	private static final String REMEMBER_ME_DIGEST_KEY = "LightAdmin";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";

	/**
	 * 公共类资源url pattern，无需认证
	 */
	private static final String[] PUBLIC_RESOURCES = {
			"/images/**", "/scripts/**", "/styles/**",
			"/rest/**/file",
			"/login", "/page-not-found", "/access-denied",
			"/dynamic/logo"
	};

	@Autowired
	private LightAdminConfiguration lightAdminConfiguration;

	@Autowired
	private DataSource dataSource;

	private @Value("${usersByUsernameQuery}") String usersByUsernameQuery;
	private @Value("${authoritiesByUsernameQuery}") String authoritiesByUsernameQuery;
	private @Value("${useridBySFZQuery}") String useridBySFZQuery;
	private @Value("${definitionSourcePermitToAllQuery}") String definitionSourcePermitToAllQuery;
	private @Value("${definitionSourceByMatchingUrlQuery}") String definitionSourceByMatchingUrlQuery;
	private @Value("${definitionSourceByUrlQuery}") String definitionSourceByUrlQuery;
	private @Value("${server.ssladdress}") String serverSSLAddress;
	private @Value("${localLoginCheckUrl}") String localLoginCheckUrl;
	private @Value("${usernameParameter}") String usernameParameter;
	private @Value("${passwordParameter}") String passwordParameter;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	@Bean
	@Autowired
	public FilterChainProxy springSecurityFilterChain(Filter filterSecurityInterceptor, Filter authenticationFilter, Filter rememberMeAuthenticationFilter, Filter logoutFilter, Filter exceptionTranslationFilter, Filter securityContextPersistenceFilter) {
		List<SecurityFilterChain> filterChains = newArrayList();
		for (String pattern : PUBLIC_RESOURCES) {
			filterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(applicationUrl(pattern))));
		}

		filterChains.add(new DefaultSecurityFilterChain(AnyRequestMatcher.INSTANCE, securityContextPersistenceFilter, exceptionTranslationFilter, logoutFilter, authenticationFilter, rememberMeAuthenticationFilter, filterSecurityInterceptor));

		return new FilterChainProxy(filterChains);
	}

	//################################权限设置start################################

	/**
	 * 资源访问权限安全拦截器
	 * @param authenticationManager
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Autowired
	public Filter filterSecurityInterceptor(AuthenticationManager authenticationManager) throws Exception {
		FilterSecurityInterceptor filter = new FilterSecurityInterceptor();
		filter.setAuthenticationManager(authenticationManager);
		filter.setAccessDecisionManager(new AffirmativeBased(asList((AccessDecisionVoter) new RoleVoter())));
		filter.setSecurityMetadataSource(rdbmsFilterInvocationDefinitionSource());
		filter.afterPropertiesSet();
		return filter;
	}

	/**
	 * 是否有访问权限取决于它
	 * @return
	 */
	@Bean
	public AffirmativeBased accessDesisionManager(){
		PermitAllVoter permitAllVoter = new PermitAllVoter();//permitAll去处理，没有在funciton表中控制的uri，表示为资源“全允许”
		permitAllVoter.setVoter(new RoleVoter());//角色投票者，判断是否拥有指定角色
		return new AffirmativeBased(Lists.<AccessDecisionVoter>newArrayList(permitAllVoter));
	}

	/**
	 * 加入缓存机制
	 * @return
	 */
	public FilterInvocationSecurityMetadataSource cachingFilterInvocationSecurityMetadataSource(){
		EhCacheFilterInvocationSecurityMetadataSourceImpl cacheFilterInvocationSecurityMetadataSource = new EhCacheFilterInvocationSecurityMetadataSourceImpl();
		cacheFilterInvocationSecurityMetadataSource.setActionPathParser(actionPathParser());
		cacheFilterInvocationSecurityMetadataSource.setCache(cachingDefinitionSourceCache());
		cacheFilterInvocationSecurityMetadataSource.setDelegate((FilterInvocationWithPatternMetadataSource) rdbmsFilterInvocationDefinitionSource());
		return cacheFilterInvocationSecurityMetadataSource;
	}


	/**
	 * 请求uri缓存，后期可用redis代替
	 * @return
	 */
	@Bean
	public DefinitionSourceCache cachingDefinitionSourceCache(){
		return null;
	}

	/**
	 * 保护的资源
	 * @return
	 */
	@Bean
	public FilterInvocationSecurityMetadataSource rdbmsFilterInvocationDefinitionSource() {
		RdbmsFilterInvocationDefinitionSource rdbmsFilterInvocationDefinitionSource = new RdbmsFilterInvocationDefinitionSource();
		//数据源设置
		rdbmsFilterInvocationDefinitionSource.setDataSource(dataSource);
		//资源路径解析器
		rdbmsFilterInvocationDefinitionSource.setActionPathParser(actionPathParser());
		//角色前缀
		rdbmsFilterInvocationDefinitionSource.setRolePrefix("ROLE_");

		rdbmsFilterInvocationDefinitionSource.setDefinitionSourceByUrlQuery(definitionSourceByUrlQuery);

		rdbmsFilterInvocationDefinitionSource.setDefinitionSourceByMatchingUrlQuery(definitionSourceByMatchingUrlQuery);

		rdbmsFilterInvocationDefinitionSource.setDefinitionSourcePermitToAllQuery(definitionSourcePermitToAllQuery);

		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> map = newLinkedHashMap();
		map.put(AnyRequestMatcher.INSTANCE, asList((ConfigAttribute) new SecurityConfig(ROLE_ADMIN)));
		return new DefaultFilterInvocationSecurityMetadataSource(map);
	}

	private ActionPathParser actionPathParser() {
		return null;
	}


	//################################权限设置end################################


	/**
	 * 用户认证Filter
	 * @param authenticationManager
	 * @param requestCache
	 * @return
	 */
	@Bean
	@Autowired
	public Filter authenticationFilter(AuthenticationManager authenticationManager, RequestCache requestCache) {
		LocalAuthenticationFilter authenticationFilter = new LocalAuthenticationFilter(applicationUrl("/j_spring_security_check"));
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setRequestCache(requestCache);
		authenticationFilter.setAuthenticationSuccessHandler(successHandler);
		authenticationFilter.setAuthenticationManager(authenticationManager);
		authenticationFilter.setSslServerAddress(serverSSLAddress);
		authenticationFilter.setAuthenticationInfoExtractors(authenticationInfoExtractors());
		authenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(applicationUrl("/login?login_error=1")));
		return authenticationFilter;
	}

	/**
	 * 登录信息提取集合
	 * @return
	 */
	private Map<String, AuthenticationInfoExtractor> authenticationInfoExtractors() {
		Map<String, AuthenticationInfoExtractor> extractors = newHashMap();
		extractors.put("pki_login", x509AuthenticationInfoExtractor());//x.509证书登录
		extractors.put("common_login", commonAuthenticationInfoExtractor());//普通的form表单登录
		return null;
	}

	/**
	 * x.509证书提取器
	 * @return
	 */
	@Bean
	public AuthenticationInfoExtractor x509AuthenticationInfoExtractor(){
		return new X509AuthenticationInfoExtractor();
	}

	/**
	 * 普通form登录信息提取器
	 * @return
	 */
	@Bean
	public AuthenticationInfoExtractor commonAuthenticationInfoExtractor(){
		CommonAuthenticationInfoExtractor commonExtractor = new CommonAuthenticationInfoExtractor();
		commonExtractor.setUsernameParameter(usernameParameter);
		commonExtractor.setPasswordParameter(passwordParameter);
		return commonExtractor;
	}

	/**
	 * 认证异常过滤器
	 * @param requestCache
	 * @return
	 */
	@Bean
	public Filter exceptionTranslationFilter(RequestCache requestCache) {
		AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
		accessDeniedHandler.setErrorPage(applicationUrl("/access-denied"));
		LoginUrlAuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(applicationUrl("/login"));
		ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(authenticationEntryPoint, requestCache);
		exceptionTranslationFilter.setAccessDeniedHandler(accessDeniedHandler);
		return exceptionTranslationFilter;
	}

	/**
	 * 登出过滤器
	 * @return
	 */
	@Bean
	public Filter logoutFilter() {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.setInvalidateHttpSession(true);
		LogoutFilter logoutFilter = new LogoutFilter(applicationUrl("/"), logoutHandler);
		logoutFilter.setFilterProcessesUrl(applicationUrl("/logout"));
		return logoutFilter;
	}

	/**
	 * SecurityContext持久化过滤器?为何需要持久化?
	 * @return
	 */
	@Bean
	public Filter securityContextPersistenceFilter() {
		HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
		repo.setSpringSecurityContextKey(keyWithNamespace(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY));
		return new SecurityContextPersistenceFilter(repo);
	}

	/**
	 * "记住我"认证过滤器
	 * @param authenticationManager
	 * @param userDetailsService
	 * @return
	 */
	@Bean
	public Filter rememberMeAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
		TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(REMEMBER_ME_DIGEST_KEY, userDetailsService);
		rememberMeServices.setCookieName(keyWithNamespace(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY));
		return new RememberMeAuthenticationFilter(authenticationManager, rememberMeServices);
	}

	/**
	 * 请求缓存??
	 * @return
	 */
	@Bean
	public RequestCache requestCache() {
		return new LightAdminRequestCache();
	}

	/**
	 * 本地登录用户认证管理者,认证服务集合
	 * @return
	 */
	@Bean
	public AuthenticationManager localAuthenticationManager() throws Exception {
		return new ProviderManager(asList(authenticationProvider(), sslAuthenticationProvider(), rememberMeAuthenticationProvider()));
	}

	/**
	 * ssl用户认证器
	 * @return
	 */
	@Bean
	public AuthenticationProvider sslAuthenticationProvider(){
		SSLAuthenticationProvider provider = new SSLAuthenticationProvider();
		provider.setAuthenticatedUserDetailsService(userDetailsByNameServiceWrapper());
		provider.setUserDetailsService((ExtendUserDetailsService) userDetailsService());
		return provider;
	}

	/**
	 * 普通的用户认证器,从userDetailService中获取待认证用户信息,再通过设置的加密算法和提交的认证密码做校验
	 * @return
	 * @throws Exception
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() throws Exception {
		UsernamePasswordAuthenticationProvider provider = new UsernamePasswordAuthenticationProvider();
		provider.setAuthenticatedUserDetailsService(userDetailsByNameServiceWrapper());
		return provider;
	}

	@Bean
	public AuthenticationUserDetailsService userDetailsByNameServiceWrapper(){
		UserDetailsByNameServiceWrapper userDetailsService = new UserDetailsByNameServiceWrapper();
		userDetailsService.setUserDetailsService(userDetailsService());
		return userDetailsService;
	}

	/**
	 * 用户详细信息service
	 * @return
	 */
	@Bean
	@Primary
	public UserDetailsService userDetailsService() {
		RdbmsUserDetailsServiceImpl userDetailsService = new RdbmsUserDetailsServiceImpl();
		userDetailsService.setDataSource(dataSource);
		userDetailsService.setUsersByUsernameQuery(usersByUsernameQuery);
		userDetailsService.setAuthoritiesByUsernameQuery(authoritiesByUsernameQuery);
		userDetailsService.setUseridBySFZQuery(useridBySFZQuery);
		userDetailsService.setRolePrefix("ROLE_");
		return userDetailsService;
	}

	/**
	 * 只需要校验"记住我"的cookie信息,所以需要提供remember_me_digest_key
	 * @return
	 */
	@Bean
	public AuthenticationProvider rememberMeAuthenticationProvider() {
		return new RememberMeAuthenticationProvider(REMEMBER_ME_DIGEST_KEY);
	}

	private String applicationUrl(String path) {
		return lightAdminConfiguration.getApplicationUrl(path);
	}

	private String keyWithNamespace(String key) {
		return "lightadmin:" + key;
	}

}
