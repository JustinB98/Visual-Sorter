package utility;

import java.util.Objects;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.Accepter;

public class GUIService extends Service<Void> {

	private Accepter accepter;

	private static int thread_count;

	public GUIService(Accepter accepter) {
		this.accepter = Objects.requireNonNull(accepter, "Accepter value can't be null!");
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
					accepter.accept();
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