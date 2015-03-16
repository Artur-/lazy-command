package org.vaadin.artur.lazycommand;


public interface AsyncCommand {
	public void execute();

	public void updateUI();
}