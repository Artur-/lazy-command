package org.vaadin.artur.lazycommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.UI;

import elemental.json.JsonArray;

@JavaScript("lazy-extension.js")
public class LazyExtension extends AbstractJavaScriptExtension {

	private List<Command> commands = new ArrayList<Command>();
	private AtomicInteger asyncCommandsDone = new AtomicInteger(0);
	private Command asyncCommand = null;
	private int asyncCommandsStarted = 0;
	private int asyncCommandsHandled = 0;

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
		if (!commands.isEmpty())
			callFunction("schedule");
	}

	public void addCommand(Command command) {
		commands.add(command);
		markAsDirty();
	}

	private void executeCommands() {
		ArrayList<Command> commandsCopy = new ArrayList<Command>(commands);
		commands.clear();
		for (Command c : commandsCopy) {
			c.execute();
		}
	}

	public void asyncWorkStarted() {
		asyncCommandsStarted++;
		log("Async work started, count: " + asyncCommandsStarted);
		if (asyncCommand == null) {
			asyncCommand = new Command() {
				@Override
				public void execute() {
					log("Request from client");
					// Busy wait for any work to finish
					while (true) {
						if (asyncCommandsDone.get() <= asyncCommandsHandled) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
							continue;
						}
						
						// 1+ commands finished, return so access() is run and
						// the UI updated (and retrigger waiting)
						asyncCommandsHandled = asyncCommandsDone.get();

						log("Done reporting work for " + asyncCommandsHandled);
						// Trigger a new wait if there a commands still pending
						if (asyncCommandsStarted > asyncCommandsHandled) {
							log("Triggering a new wait");
							addCommand(asyncCommand);
						}
						return;
					}
				}
			};
			addCommand(asyncCommand);
		}

	}

	public static void log(String string) {
		System.out.println("[" + Thread.currentThread().getName() + "]: "
				+ string);
	}

	public void asyncWorkDone() {
		// Called without UI lock
		int done = asyncCommandsDone.incrementAndGet();
		log("Async work done is now " + done);
	}
}
