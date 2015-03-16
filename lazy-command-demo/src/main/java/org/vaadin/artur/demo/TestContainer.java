package org.vaadin.artur.demo;

import com.vaadin.data.Container.Indexed;
import com.vaadin.data.util.IndexedContainer;

public class TestContainer {

	public static Indexed createContainer() {
		IndexedContainer ic = new IndexedContainer();
		ic.addContainerProperty("firstName", String.class, "");
		ic.addContainerProperty("lastName", String.class, "");

		add(ic, "John", "Doe", 67);
		add(ic, "Mr", "Vaadin", 23);
		
		return ic;
	}

	private static void add(IndexedContainer ic, String first, String last,
			int age) {
		ic.addItem(first);
		ic.getItem(first).getItemProperty("firstName").setValue(first);
		ic.getItem(first).getItemProperty("lastName").setValue(last);
//		ic.getItem(id).getItemProperty("age").setValue(age);
	}

}
