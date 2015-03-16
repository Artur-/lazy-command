package org.vaadin.artur.demo;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;

public class DemoNavi extends HorizontalLayout {

	public DemoNavi() {
		setSpacing(true);
		Link l1 = new Link("Simple demo", new ExternalResource("../simple/"));
		Link l2 = new Link("Asynchronous demo", new ExternalResource(
				"../async/"));
		Link l3 = new Link("Chained demo", new ExternalResource("../chained/"));
		Link l4 = new Link("Grid demo", new ExternalResource("../grid/"));
		addComponents(l1, l2, l3, l4);
		setSizeUndefined();
	}
}
