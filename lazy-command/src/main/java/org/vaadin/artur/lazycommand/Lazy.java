package org.vaadin.artur.lazycommand;

import com.vaadin.server.Extension;
import com.vaadin.ui.UI;

public class Lazy {

	public static void schedule(Command command) {
		schedule(UI.getCurrent(), command);
	}

	public static void schedule(UI ui, Command command) {
		LazyExtension e = findExtension(ui);
		e.addCommand(command);
	}

	private static LazyExtension findExtension(UI ui) {
		for (Extension e : ui.getExtensions())
			if (e instanceof LazyExtension) {
				return (LazyExtension) e;
			}
		LazyExtension e = new LazyExtension();
		e.extend(ui);
		return e;
	}

}
