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
public class SimpleDemoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = SimpleDemoUI.class, widgetset = "org.vaadin.artur.demo.DemoWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		setContent(layout);
		Link link = new Link("Advanced demo", new ExternalResource("../advanced/"));
		layout.addComponent(link);

		Panel p = new Panel("Complex content");
		p.setWidth("500px");
		p.setHeight("300px");

		Label l = new Label("Lazy loading, please wait...");
		p.setContent(l);
		Lazy.schedule(() -> {
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}
			l.setValue("Done");
		});

		layout.addComponent(p);
	}
}
