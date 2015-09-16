package org.rapla.client.plugin.tableview.gwt;

import java.util.Collection;
import java.util.Locale;

import javax.inject.Inject;

import org.gwtbootstrap3.client.ui.constants.Responsiveness;
import org.gwtbootstrap3.client.ui.html.Div;
import org.rapla.RaplaResources;
import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;
import org.rapla.client.event.StartActivityEvent;
import org.rapla.client.plugin.tableview.CalendarTableView;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;
import org.rapla.inject.DefaultImplementation;
import org.rapla.inject.InjectionContext;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

@DefaultImplementation(of = CalendarTableView.class, context = InjectionContext.gwt)
public class CalendarListViewImpl extends AbstractView<org.rapla.client.plugin.tableview.CalendarTableView.Presenter>implements CalendarTableView<IsWidget>
{
    private static final String ELEMENT_ID = "raplaId";
    private static final String HIDE_CSS_STYLES = Responsiveness.VISIBLE_MD.getCssName() + " " + Responsiveness.VISIBLE_LG.getCssName();

    private final Div content = new Div();
    private final TableElement table = Document.get().createTableElement();
    private final RaplaResources i18n;

    @Inject
    public CalendarListViewImpl(final EventBus eventBus, final RaplaResources i18n)
    {
        this.i18n = i18n;
        content.getElement().appendChild(table);
        content.addDomHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                event.stopPropagation();
                Element clickTarget = DOM.eventGetTarget((Event) event.getNativeEvent());
                while (!TableRowElement.is(clickTarget) && clickTarget != content.getElement())
                {
                    clickTarget = clickTarget.getParentElement();
                }
                if (clickTarget == content.getElement())
                {
                    return;
                }
                final String reservationId = clickTarget.getAttribute(ELEMENT_ID);
                String id = ReservationPresenter.EDIT_ACTIVITY_ID;
                eventBus.fireEvent(new StartActivityEvent(id, reservationId));
            }
        }, ClickEvent.getType());
        table.setClassName("table table-striped table-hover table-selectable");
    }

    @Override
    public void update(Collection<Reservation> result)
    {
        table.removeAllChildren();
        final Document document = Document.get();
        {// create header
            final TableSectionElement header = document.createTHeadElement();
            final TableRowElement headerRow = document.createTRElement();
            header.appendChild(headerRow);
            {
                final TableCellElement name = document.createTHElement();
                name.setInnerText("Name");
                headerRow.appendChild(name);
            }
            {
                final TableCellElement persons = document.createTHElement();
                persons.setInnerText("Personen");
                headerRow.appendChild(persons);
                persons.setClassName(HIDE_CSS_STYLES);
            }
            {
                final TableCellElement resources = document.createTHElement();
                resources.setInnerText("Ressourcen");
                headerRow.appendChild(resources);
                resources.setClassName(HIDE_CSS_STYLES);
            }
            table.appendChild(header);
        }
        {// create body
            final TableSectionElement tb = document.createTBodyElement();
            table.appendChild(tb);
            for (Reservation reservation : result)
            {
                final String id = reservation.getId();
                {
                    final TableRowElement tr = document.createTRElement();
                    tr.setAttribute(ELEMENT_ID, id);
                    tb.appendChild(tr);
                    final TableCellElement td = document.createTDElement();
                    tr.appendChild(td);
                    td.setInnerText(reservation.getName(getRaplaLocale().getLocale()));
                    {
                        final Allocatable[] allocatables = reservation.getPersons();
                        insertAllocatables(tr, allocatables);
                    }
                    {
                        final Allocatable[] allocatables = reservation.getResources();
                        insertAllocatables(tr, allocatables);
                    }
                }
            }
        }
    }

    private void insertAllocatables(final TableRowElement tr, final Allocatable[] allocatables)
    {
        final TableCellElement tdElement = Document.get().createTDElement();
        tr.appendChild(tdElement);
        final StringBuilder sb = new StringBuilder();
        final Locale locale = getRaplaLocale().getLocale();
        for (Allocatable allocatable : allocatables)
        {
            final String name = allocatable.getName(locale);
            sb.append(name);
            sb.append(", ");
        }
        tdElement.setClassName(HIDE_CSS_STYLES);
        if (sb.length() > 0)
        {
            sb.delete(sb.length() - 2, sb.length());
        }
        tdElement.setInnerText(sb.toString());
    }

    @Override
    public IsWidget provideContent()
    {
        return content;
    }
}
