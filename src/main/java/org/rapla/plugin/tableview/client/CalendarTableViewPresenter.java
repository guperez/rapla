package org.rapla.plugin.tableview.client;

import com.google.web.bindery.event.shared.EventBus;
import org.rapla.client.base.CalendarPlugin;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;
import org.rapla.client.event.StartActivityEvent;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.CalendarSelectionModel;
import org.rapla.framework.RaplaException;
import org.rapla.framework.logger.Logger;
import org.rapla.inject.Extension;
import org.rapla.plugin.tableview.client.CalendarTableView.Presenter;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Extension(provides = CalendarPlugin.class, id = CalendarTableViewPresenter.TABLE_VIEW)
public class CalendarTableViewPresenter implements Presenter, CalendarPlugin
{

    public static final String TABLE_VIEW = "table";
    private final CalendarTableView view;
    private final Logger logger;
    private final EventBus eventBus;
    private final CalendarSelectionModel model;
    
    @SuppressWarnings("unchecked")
    @Inject
    public CalendarTableViewPresenter(CalendarTableView view, Logger logger, EventBus eventBus, CalendarSelectionModel model)
    {
        this.view = view;
        this.logger = logger;
        this.eventBus = eventBus;
        this.model = model;
        this.view.setPresenter(this);
    }
    
    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "list";
    }

    @Override
    public Date calcNext(Date currentDate)
    {
        return DateTools.addMonths(currentDate, 1);
    }

    @Override
    public Date calcPrevious(Date currentDate)
    {
        return DateTools.addMonths(currentDate, -1);
    }

    @Override
    public Object provideContent()
    {
        updateContent();
        return view.provideContent();
    }

    @Override
    public void selectReservation(Reservation selectedObject)
    {
        final StartActivityEvent activity = new StartActivityEvent(ReservationPresenter.EDIT_ACTIVITY_ID, selectedObject.getId());
        eventBus.fireEvent(activity);
        logger.info("selection changed");

    }

    @Override
    public void updateContent()
    {
        try
        {
            Reservation[] reservations = model.getReservations();
            Collection<Reservation> result = Arrays.asList(reservations);
            logger.info(result.size() + " Reservations loaded.");
            view.update(result);
        }
        catch (RaplaException e)
        {
            logger.error(e.getMessage(), e);
        }
    }
}