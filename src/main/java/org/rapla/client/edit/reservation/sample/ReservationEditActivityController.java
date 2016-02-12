package org.rapla.client.edit.reservation.sample;

import org.rapla.client.ActivityManager;
import org.rapla.client.ActivityPresenter;
import org.rapla.entities.Entity;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.storage.ReferenceInfo;
import org.rapla.facade.RaplaFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.logger.Logger;
import org.rapla.inject.Extension;
import org.rapla.storage.StorageOperator;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Extension(provides = ActivityPresenter.class, id = ReservationPresenter.EDIT_ACTIVITY_ID)
@Singleton
public class ReservationEditActivityController  implements ActivityPresenter
{
    final private Provider<ReservationPresenter> presenterProvider;
    final private RaplaFacade facade;
    final private Logger logger;
    private final Map<String, ReservationPresenter> opendPresenter = new HashMap<>();

    @Inject
    public ReservationEditActivityController(Provider<ReservationPresenter> presenterProvider, RaplaFacade facade, Logger logger)
    {
        this.presenterProvider = presenterProvider;
        this.facade = facade;
        this.logger = logger;
    }

    @Override @SuppressWarnings("rawtypes") public boolean startActivity(ActivityManager.Activity activity)
    {
        try
        {
            final StorageOperator operator = facade.getOperator();
            final ReferenceInfo<Reservation> info = new ReferenceInfo(activity.getInfo(), Reservation.class);
            final List<ReferenceInfo<Reservation>> referenceInfos =  (List)Collections.singletonList(info);
            final Map<ReferenceInfo<Reservation>, Reservation> entities = operator.getFromId(referenceInfos, false);
            final Collection<Reservation> values = entities.values();
            for (Reservation reservation : values)
            {
                if (reservation != null )
                {
                    final ReservationPresenter alreadyOpendPresenter = opendPresenter.get(reservation.getId());
                    if(alreadyOpendPresenter == null || !alreadyOpendPresenter.isVisible())
                    {
                        final ReservationPresenter newReservationPresenter = presenterProvider.get();
                        newReservationPresenter.edit(reservation, false);
                        opendPresenter.put(reservation.getId(), newReservationPresenter);
                    }
                    return true;
                }
            }
        }
        catch (RaplaException e)
        {
            logger.error("Error initializing activity: " + activity, e);
        }
        return false;
    }
}