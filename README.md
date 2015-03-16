# A Vaadin add-on for doing lazy initialization of a UI without using push

## Demo 
See a live demo at http://artur.app.fi/lazy-command/simple/

## Usage
The main use case for this add-on is lazy initialization of a view in a Vaadin application. This is mainly useful when DB access takes a lot of time and you do not want to delay UI rendering until DB access is done.

Example 1 - a single block for lazy loading
```java
protected void init(VaadinRequest request) {
   // Do quick UI initialization
   ... 
   // Defer slow data fetching
   Lazy.schedule(() -> {
      // access db
      // update UI with results
   });
```
Using it this way is quick and easy if you have a single init block and do not mind that the UI is locked during the whole lazy initialization operation (normal loading indicator is shown)


Example 2 - multiple blocks to lazy load
```java
protected void init(VaadinRequest request) {
  // Do quick UI initialization
  ...
  // Defer slow data fetching
  Lazy.scheduleAsync(new AsyncCommand() {
			@Override
			public void execute() {
				// Access DB without UI being locked
			}
			@Override
			public void updateUI() {
				// Update UI based on results
			}
		});

  // Defer second slow data fetching
  Lazy.scheduleAsync(new AsyncCommand() {
			@Override
			public void execute() {
				// Access DB without UI being locked
			}
			@Override
			public void updateUI() {
				// Update UI based on results
			}
		});
```
Using it this way will run both the first and send async DB code at the same time. When one of them returns, its updateUI method will be run and the UI updated. When both have finished and updated the UI, the loading indicator will disappear.

   
   


