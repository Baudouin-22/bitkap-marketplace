package net.bitkap.marketplace.security;

import net.bitkap.marketplace.controller.ControllerVariables;
import net.bitkap.marketplace.dao.AppParametersDAO;
import net.bitkap.marketplace.dao.AppUserDAO;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("AppUserDAO")
    AppUserDAO appUserDAO;

    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("AppParametersDAO")
    AppParametersDAO appParametersDAO;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        super.configure(http);

        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors();

        http.headers().frameOptions().sameOrigin();

/*        if(!UtilsApp.isProdEnv(environment)){

            http.authorizeRequests()
                    .antMatchers(ControllerVariables.devAntPatterns)
                    .permitAll();
        }*/

        http.authorizeRequests()
                .antMatchers(ControllerVariables.publicAntPatterns)
                .permitAll();

        http.authorizeRequests()
                .antMatchers(ControllerVariables.userAntPatterns)
                .hasAnyRole(new String[]{ControllerVariables.USER_ROLE_NAME, ControllerVariables.ADMIN_ROLE_NAME, ControllerVariables.ROOT_ROLE_NAME});

        http.authorizeRequests()
                .antMatchers(ControllerVariables.adminAntPatterns)
                .hasAnyRole(new String[]{ControllerVariables.ADMIN_ROLE_NAME, ControllerVariables.ROOT_ROLE_NAME});

        http.authorizeRequests()
                .antMatchers(ControllerVariables.rootAntPatterns)
                .hasAnyRole(ControllerVariables.ROOT_ROLE_NAME);

        http.authorizeRequests()
                .anyRequest()
                .authenticated();

        http.addFilterAfter(
                new CheckAndCreateUserFilter(appUserDAO, appParametersDAO), FilterSecurityInterceptor.class
        );

       /* http.addFilterAfter(
                new CheckAndCreateUserFilter(), BasicAuthenticationFilter.class
        );*/

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }


    @Bean
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakAuthenticatedActionsFilterBean(
            KeycloakAuthenticatedActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakSecurityContextRequestFilterBean(
            KeycloakSecurityContextRequestFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }
}
