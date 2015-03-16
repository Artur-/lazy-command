package org.vaadin.artur.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.artur.lazycommand.Lazy;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container.Indexed;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
public class GridDemoUI extends UI {

	@WebServlet(value = "/grid/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = GridDemoUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		setContent(layout);
		layout.addComponent(new DemoNavi());

		
		Grid grid = new Grid();
		grid.setSizeFull();
		grid.addColumn("dummy").setHeaderCaption("Fetching data, please wait");
		grid.addRow("Fetching data, please wait...");
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(1);

		Lazy.schedule(() -> {
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}
			grid.removeAllColumns();
			grid.setContainerDataSource(TestContainer.createContainer());
			grid.setHeightByRows(2);
			Lazy.schedule(() -> {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				Indexed dataSource = grid.getContainerDataSource();
				dataSource.addContainerProperty("age", Integer.class, 0);
				dataSource.getItem("John").getItemProperty("age").setValue(67);
				dataSource.getItem("Mr").getItemProperty("age").setValue(23);

				grid.addRow("One", "More", 55);
				grid.setHeightByRows(3);
			});
		});

		layout.addComponent(grid);
	}
}
