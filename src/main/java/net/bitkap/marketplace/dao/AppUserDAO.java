package net.bitkap.marketplace.dao;

import net.bitkap.marketplace.exception.ResourceNotFoundException;
import net.bitkap.marketplace.model.entity.AppUser;

public interface AppUserDAO {
    boolean isUserExist(AppUser appUser);
    AppUser createAppUser(AppUser appUser) throws ResourceNotFoundException;

    AppUser getAppUser(AppUser appUser) throws ResourceNotFoundException;

    AppUser updateAppUser(AppUser appUser) throws ResourceNotFoundException;
}
