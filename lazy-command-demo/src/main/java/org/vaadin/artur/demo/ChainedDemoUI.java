package org.vaadin.artur.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.artur.lazycommand.Lazy;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
public class ChainedDemoUI extends UI {

	@WebServlet(value = "/chained/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ChainedDemoUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		setContent(layout);
		layout.addComponent(new DemoNavi());

		Panel p = new Panel("Chained loading of content");
		p.setWidth("500px");
		p.setHeight("300px");

		VerticalLayout labelContainer = new VerticalLayout();
		p.setContent(labelContainer);
		Label l = new Label("Lazy loading, please wait...");
		labelContainer.addComponent(l);
		Lazy.schedule(() -> {
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}
			labelContainer.addComponent(new Label("Step 1 complete"));
			Lazy.schedule(() -> {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
				}
				labelContainer.addComponent(new Label("All done"));
			});
		});

		layout.addComponent(p);
	}
}
