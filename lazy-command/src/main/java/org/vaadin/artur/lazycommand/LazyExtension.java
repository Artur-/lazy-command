package org.vaadin.artur.lazycommand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.UI;

import elemental.json.JsonArray;

@JavaScript("lazy-extension.js")
public class LazyExtension extends AbstractJavaScriptExtension {

	private List<Command> commands = new ArrayList<Command>();

	public LazyExtension() {
		addFunction("execute", new JavaScriptFunction() {
			@Override
			public void call(JsonArray arguments) {
				executeCommands();
			}
		});
	}

	public void extend(UI target) {
		super.extend(target);
	}

	@Override
	public void beforeClientResponse(boolean initial) {
		super.beforeClientResponse(initial);
		callFunction("schedule");
	}

	public void addCommand(Command command) {
		commands.add(command);
		markAsDirty();
	}

	private void executeCommands() {
		for (Command c : commands) {
			c.execute();
		}
	}

	
}
