package net.bitkap.marketplace.dao;

import net.bitkap.marketplace.model.entity.AppParameters;
import net.bitkap.marketplace.repository.AppParametersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("AppParametersDAO")
public class AppParametersDAOImpl implements AppParametersDAO{

    @Autowired
    AppParametersRepository appParametersRepository;

    @Override
    public AppParameters getAppParameters() {

        List<AppParameters> appParameters = appParametersRepository.findAll();

        if(appParameters != null){
            return appParameters.get(0);
        }
        return null;
    }

    @Override
    public int useNextPipeline() {

        int pipeline = 1;

        AppParameters appParameters = getAppParameters();

        if(appParameters != null){

            pipeline =  appParameters.getNextPipeline();
            if(pipeline > appParameters.getTotalPipeline()){
                appParameters.setNextPipeline(1);
            }else{
                appParameters.setNextPipeline(pipeline + 1);
            }
            appParametersRepository.saveAndFlush(appParameters);
        }else{
            AppParameters appParameters1 = new AppParameters();
            appParameters1.setNextPipeline(pipeline + 1);
            appParametersRepository.saveAndFlush(appParameters1);
        }
        return pipeline;
    }
}
