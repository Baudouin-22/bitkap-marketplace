package net.bitkap.marketplace.security;

import net.bitkap.marketplace.dao.AppParametersDAO;
import net.bitkap.marketplace.dao.AppUserDAO;
import net.bitkap.marketplace.exception.ResourceNotFoundException;
import net.bitkap.marketplace.model.entity.AppUser;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.representations.AccessToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class CheckAndCreateUserFilter extends GenericFilterBean {

    AppUserDAO appUserDAO;

    AppParametersDAO appParametersDAO;

    public CheckAndCreateUserFilter (AppUserDAO appUserDAO, AppParametersDAO appParametersDAO){
        this.appUserDAO = appUserDAO;
        this.appParametersDAO = appParametersDAO;
    }

    private static final Logger log = LoggerFactory.getLogger(CheckAndCreateUserFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication==null || authentication.getName() == null || ("anonymousUser").equals(authentication.getName()) ){
            chain.doFilter(request, response);
            return;
        }

        AppUser appUser =  new AppUser();
        appUser.setId(authentication.getName());


        if(!appUserDAO.isUserExist(appUser)){

            SimpleKeycloakAccount account = (SimpleKeycloakAccount) authentication.getDetails();
            AccessToken accessToken = account.getKeycloakSecurityContext().getToken();
            String username = accessToken.getPreferredUsername();
            String emailID = accessToken.getEmail();
            String lastname = accessToken.getFamilyName();
            String firstname = accessToken.getGivenName();

            appUser.setUsername(username);
            appUser.setEmail(emailID);
            appUser.setFirstname(firstname);
            appUser.setLastname(lastname);
            appUser.setPipeline(appParametersDAO.useNextPipeline());
            try {
                appUser = appUserDAO.createAppUser(appUser);

                log.debug("New user " + appUser);

            } catch (ResourceNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            appUser = appUserDAO.getAppUser(appUser);

            if(appUser.getPipeline()<1 || appUser.getPipeline() > 10){

                appUser.setPipeline(appParametersDAO.useNextPipeline());
                appUserDAO.updateAppUser(appUser);

            }

        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        chain.doFilter(request, response);
    }
}
