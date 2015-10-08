package org.aitek.collections.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.management.relation.RoleList;
import javax.swing.SwingWorker;

import org.aitek.collections.gui.Main;
import org.aitek.collections.gui.StatsPanel;
import org.aitek.collections.utils.Constants;

public class ListSample extends CollectionSample implements PropertyChangeListener {

	private ArrayList<Integer> arrayList;
	private LinkedList<Integer> linkedList;
	private CopyOnWriteArrayList<Integer> copyOnWriteArrayList;
	private Stack<Integer> stack;
	private RoleList rl;
	private long[] times;
	private Task task;

	public ListSample(StatsPanel statsPanel, Main main) {

		super(statsPanel, main);
		COLLECTION_TYPES = 4;
		times = new long[COLLECTION_TYPES];
		arrayList = new ArrayList<Integer>(Constants.COLLECTION_MAX_SIZE * 1000);
		linkedList = new LinkedList<Integer>();
		copyOnWriteArrayList = new CopyOnWriteArrayList<Integer>();
		stack = new Stack<Integer>();
		rl = new RoleList(50000);

	}

	public HashSet<OperationType> getSupportedOperations() {

		HashSet<OperationType> set = new HashSet<OperationType>();

		set.add(OperationType.POPULATE);
		set.add(OperationType.INSERT);
		set.add(OperationType.REMOVE);
		set.add(OperationType.SEARCH);
		set.add(OperationType.ITERATE);
		set.add(OperationType.SORT);

		return set;
	}

	public void execute(OperationType operation) {

		this.currentOperation = operation;

		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		statusBar.updateProgressBar(task.getProgress());
	}

	private class Task extends SwingWorker<Void, Void> {

		private double mult;

		@Override
		public Void doInBackground() {

			mult = 100d / iterations;
			switch (currentOperation) {
				case POPULATE:
					times = fillLists();
					statsPanel.setTimes("Populating", times);
				break;
				case INSERT:
					times = insertIntoLists();
					statsPanel.setTimes("Inserting new elements", times);
				break;
				case REMOVE:
					times = removeFromLists();
					statsPanel.setTimes("Removing existing elements", times);
				break;
				case SEARCH:
					times = searchLists();
					statsPanel.setTimes("Searching existing elements", times);
				break;
				case ITERATE:
					times = iterateOnLists();
					statsPanel.setTimes("Iterating elements", times);
				break;
				case SORT:
					times = sortLists();
					statsPanel.setTimes("Sorting elements", times);
				break;
			}

			return null;
		}

		@Override
		public void done() {

			main.setButtonsState();
			main.setReady();
		}

		private long[] fillLists() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Filling list with " + getListFormattedSize() + " elements...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				times = populateList();
				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			return times;
		}

		private long[] populateList() {

			long times[] = new long[COLLECTION_TYPES];
			arrayList.clear();
			copyOnWriteArrayList.clear();
			linkedList.clear();
			stack.clear();

			int[] toBeInserted = new int[listSize];
			for (int j = 0; j < getListSize(); j++) {
				toBeInserted[j] = (int) (Math.random() * listSize);
			}

			long startingTime = System.nanoTime();
			for (int j = 0; j < getListSize(); j++) {
				arrayList.add(toBeInserted[j]);
			}
			times[0] += System.nanoTime() - startingTime;

			startingTime = System.nanoTime();
			for (int j = 0; j < getListSize(); j++) {
				linkedList.add(toBeInserted[j]);
			}
			times[1] += System.nanoTime() - startingTime;

			startingTime = System.nanoTime();
			for (int j = 0; j < getListSize(); j++) {
				copyOnWriteArrayList.add(toBeInserted[j]);
			}
			times[2] += System.nanoTime() - startingTime;

			startingTime = System.nanoTime();
			for(int j = 0; j < getListSize(); j++){
				stack.push(toBeInserted[j]);
			}
			times[3] += System.nanoTime() - startingTime;

			return times;
		} // done

		private long[] insertIntoLists() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Inserting elements into list...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				long startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					arrayList.add(arrayList.size() / 2, 0);
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					linkedList.add(linkedList.size() / 2, 0);
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					copyOnWriteArrayList.add(copyOnWriteArrayList.size() / 2, 0);
				times[2] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					stack.add(stack.size() / 2, 0);
				times[3] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			return times;
		} // done

		private long[] removeFromLists() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Removing elements from list...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				long startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++)
					arrayList.remove(0);
				for (int j = 0; j < 10; j++)
					arrayList.remove(arrayList.size() / 2);
				for (int j = 0; j < 10; j++)
					arrayList.remove(arrayList.size() -1);
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++)
					linkedList.remove(0);
				for (int j = 0; j < 10; j++)
					linkedList.remove(linkedList.size() / 2);
				for (int j = 0; j < 10; j++)
					linkedList.remove(linkedList.size()-1);
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++)
					copyOnWriteArrayList.remove(0);
				for (int j = 0; j < 10; j++)
					copyOnWriteArrayList.remove(copyOnWriteArrayList.size() / 2);
				for (int j = 0; j < 10; j++)
					copyOnWriteArrayList.remove(copyOnWriteArrayList.size()-1);
				times[2] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++)
					stack.remove(0);
				for (int j = 0; j < 10; j++)
					stack.remove(stack.size() / 2);
				for (int j = 0; j < 10; j++)
					stack.remove(stack.size()-1);
				times[3] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}
			System.out.println(arrayList.size());
			return times;
		} // done

		private long[] searchLists() { //done

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Searching elements in list...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				long startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++) {
					arrayList.get(0);
					arrayList.get(arrayList.size() / 2);
					arrayList.get(arrayList.size() - 1);
				}
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++) {
					linkedList.get(0);
					linkedList.get(linkedList.size() / 2);
					linkedList.get(linkedList.size() - 1);
				}
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++) {
					copyOnWriteArrayList.get(0);
					copyOnWriteArrayList.get(copyOnWriteArrayList.size() / 2);
					copyOnWriteArrayList.get(copyOnWriteArrayList.size() - 1);
				}
				times[2] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for(int j = 0; j < 10; j++){
					stack.get(0);
					stack.get(stack.size() / 2);
					stack.get(stack.size() - 1);
				}
				times[3] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			return times;
		} //done

		private long[] sortLists() { //done

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Sorting elements...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				populateList();

				long startingTime = System.nanoTime();
				Collections.sort(arrayList);
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				Collections.sort(linkedList);
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				Collections.sort(copyOnWriteArrayList);
				times[2] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				Collections.sort(stack);
				times[3] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			return times;
		} //done

		private long[] iterateOnLists() { //done

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Iterating on elements...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				long startingTime = System.nanoTime();
				Iterator<Integer> iterator = arrayList.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				iterator = linkedList.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				iterator = copyOnWriteArrayList.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[2] += System.nanoTime() - startingTime;


				startingTime = System.nanoTime();
				iterator = stack.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[3] += System.nanoTime() - startingTime;
				setProgress((int) (z * mult));
			}

			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			setProgress(100);

			return times;
		} //done

	}

	@Override
	public boolean isPopulated() {

		return copyOnWriteArrayList.size() > 0;
	}

}
