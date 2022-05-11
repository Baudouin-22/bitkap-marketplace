package net.bitkap.marketplace.dao;

import net.bitkap.marketplace.model.entity.AppParameters;

public interface AppParametersDAO {
    AppParameters getAppParameters();

    int useNextPipeline();
}
