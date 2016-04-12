package org.rapla.client.gwt;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import org.rapla.client.Application;
import org.rapla.client.gwt.view.RaplaPopups;
import org.rapla.entities.domain.Allocatable;
import org.rapla.facade.RaplaFacade;
import org.rapla.facade.internal.FacadeImpl;
import org.rapla.framework.logger.Logger;
import org.rapla.scheduler.Promise;
import org.rapla.storage.RaplaSecurityException;
import org.rapla.storage.StorageOperator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Arrays;
import java.util.Collection;

public class Bootstrap
{

    private final Provider<Application> application;
    private final RaplaFacade facade;
    private final StorageOperator operator;
    private final Logger logger;

    @Inject
    public Bootstrap(RaplaFacade facade, StorageOperator operator,  Logger logger)
    {
        this.application = null;//FIXME add application;
        this.operator = operator;
        this.facade = facade;
        this.logger = logger;
    }

    public void load()
    {
        final FacadeImpl facadeImpl = (FacadeImpl) facade;
        ((FacadeImpl) facade).setOperator(operator);
        Promise<Void> load = facadeImpl.load();
        logger.info("Loading resources");
        RaplaPopups.getProgressBar().setPercent(40);
        load.thenRun(() ->
        {
            try
            {
                RaplaPopups.getProgressBar().setPercent(70);
                Collection<Allocatable> allocatables = Arrays.asList(facadeImpl.getAllocatables());
                logger.info("loaded " + allocatables.size() + " resources. Starting application");
                boolean defaultLanguageChosen = false;
                application.get().start(defaultLanguageChosen, () -> {
                    logger.info("Restarting.");
                    Window.Location.reload();
                }
                );
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
                if (e instanceof RaplaSecurityException)
                {
                    Window.Location.replace("../rest/auth");
                }

            }
        }).exceptionally((e) ->
        {
            logger.error(e.getMessage(), e);
            if (e instanceof RaplaSecurityException)
            {
                Window.Location.replace("../rest/auth");
            }
            return null;
        });

    }
}
