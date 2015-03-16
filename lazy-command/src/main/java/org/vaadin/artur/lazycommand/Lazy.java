package org.vaadin.artur.lazycommand;

import java.util.concurrent.ThreadFactory;

import com.vaadin.server.Extension;
import com.vaadin.ui.UI;

public class Lazy {

	private static ThreadFactory threadFactory = new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r);
		}
	};

	public static void scheduleAsync(AsyncCommand command) {
		scheduleAsync(UI.getCurrent(), command);
	}

	public static void scheduleAsync(final UI ui, final AsyncCommand command) {
		final LazyExtension e = findExtension(ui);
		final Thread t = Lazy.getThreadFactory().newThread(new Runnable() {

			@Override
			public void run() {
				LazyExtension.log("Started async thread");
				// Run without lock
				command.execute();
				LazyExtension.log("Work done");
				ui.access(new Runnable() {
					@Override
					public void run() {
						// Run with lock
						LazyExtension.log("UI update (locked)");
						command.updateUI();
					}
				});
				LazyExtension.log("UI updated, thread done");
				e.asyncWorkDone();
			}
		});
		e.asyncWorkStarted();
		t.start();

	}

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

	public static void setThreadFactory(ThreadFactory threadFactory) {
		Lazy.threadFactory = threadFactory;
	}

	public static ThreadFactory getThreadFactory() {
		return threadFactory;
	}

}
