package org.vaadin.artur.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.artur.lazycommand.AsyncCommand;
import org.vaadin.artur.lazycommand.Lazy;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
public class AdvancedDemoUI extends UI {

	@WebServlet(value = "/advanced/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = AdvancedDemoUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		VerticalLayout content = new VerticalLayout();
		content.setSizeFull();
		content.addComponent(new DemoNavi());
		
		final GridLayout layout = new GridLayout(3, 3);
		layout.setSizeFull();
		layout.addComponent(createLazyPanel(1000));
		layout.addComponent(createLazyPanel(2000));
		layout.addComponent(createLazyPanel(3000));
		layout.addComponent(createLazyPanel(3000));
		layout.addComponent(createLazyPanel(2000));
		layout.addComponent(createLazyPanel(1000));

		content.addComponent(layout);
		content.setExpandRatio(layout, 1);
		setContent(content);
	}

	private Component createLazyPanel(long timeout) {
		Panel p = new Panel("Complex content");
		p.setSizeFull();

		Label l = new Label("Lazy loading (" + timeout
				+ "ms), please wait...");
		p.setContent(l);
		Lazy.scheduleAsync(new AsyncCommand() {

			@Override
			public void execute() {
				try {
					Thread.sleep(timeout);
				} catch (Exception e) {
				}
			}

			@Override
			public void updateUI() {
				l.setValue("Done");
			}
		});

		return p;
	}
}
