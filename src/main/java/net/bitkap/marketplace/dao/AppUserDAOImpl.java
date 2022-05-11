package net.bitkap.marketplace.dao;

import net.bitkap.marketplace.exception.ExceptionType;
import net.bitkap.marketplace.exception.ResourceNotFoundException;
import net.bitkap.marketplace.model.entity.AppUser;
import net.bitkap.marketplace.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("AppUserDAO")
public class AppUserDAOImpl implements AppUserDAO{

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public boolean isUserExist(AppUser appUser) {

        Optional<AppUser> appUserEntity = appUserRepository.findById(appUser.getId());

        return appUserEntity.isPresent();
    }

    @Override
    public AppUser createAppUser(AppUser appUser) throws ResourceNotFoundException {

        AppUser savedAppUser = appUserRepository.save(appUser);

        return savedAppUser;
    }

    @Override
    public AppUser getAppUser(AppUser appUser) throws ResourceNotFoundException {

        AppUser appUserEntity = null;

        if(appUser.getId() != null){

            appUserEntity = appUserRepository.findById(appUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionType.ERROR_MSG_USER_PROFILE_NOT_FOUND));

        }else if (appUser.getUsername()!= null){

            appUserEntity = appUserRepository.findByUsername(appUser.getUsername());

            if(appUserEntity == null){
                throw  new ResourceNotFoundException(ExceptionType.ERROR_MSG_USER_PROFILE_NOT_FOUND);
            }
        }

        return appUserEntity;
    }

    @Override
    public AppUser updateAppUser(AppUser appUser) throws ResourceNotFoundException {

        AppUser savedEntity = appUserRepository.save(appUser);

        return savedEntity;
    }
}
