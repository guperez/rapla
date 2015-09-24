package org.rapla.client.extensionpoints;

import org.rapla.facade.CalendarSelectionModel;
import org.rapla.gui.toolkit.IdentifiableMenuEntry;
import org.rapla.inject.ExtensionPoint;
import org.rapla.inject.InjectionContext;

@ExtensionPoint(context = InjectionContext.swing,id="ExportMenuInsert")
public interface ExportMenuExtension extends IdentifiableMenuEntry
{
}
