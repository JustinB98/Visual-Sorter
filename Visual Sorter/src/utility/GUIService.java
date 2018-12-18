package utility;

import java.util.Objects;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GUIService extends Service<Void> {

	private Runnable runnable;

	private static int thread_count;

	public GUIService(Runnable runnable) {
		this.runnable = Objects.requireNonNull(runnable, "Accepter value can't be null!");
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Thread.currentThread().setName("Sorting Thread " + ++thread_count);
				Utility.resetLineColors();
				long startTime = System.nanoTime();
				if (Global.len() != 0)
					runnable.run();
				else
					Global.START.set(false);
				long endTime = System.nanoTime();
				long totalTime = endTime - startTime;
				System.out.println(totalTime + " ns");
				System.out.println(totalTime * Math.pow(10, -9) + " s");
				System.out.println();
				return null;
			}
		};
	}

}