
public class Clock {

	private int[] vector;
	private int index;

	// initialize
	public Clock(int size, int index) {
		this.vector = new int[size];
		this.index = index;
		for (int i = 0; i < size; i++)
			this.vector[i] = 1;
	}

	// copy
	public Clock(int[] vector) {
		this.vector = new int[vector.length];
		for (int i = 0; i < vector.length; i++) {
			this.vector[i] = vector[i];
		}
	}

	// copy
	public Clock(Clock c) {
		this.vector = new int[c.getSize()];
		for (int i = 0; i < c.getSize(); i++) {
			this.vector[i] = this.getVector()[i];
		}

	}

	// Update when my clock is smaller than others
	// TODO: update condition is not correct better do it again.
	public void update(Clock c) {
		// Only update when is smaller
		if (isSmaller(c)) {
			for (int i = 0; i < c.getSize(); i++) {
				if (i != index)
					this.vector[i] = this.getVector()[i];
			}
		}
	}

	// Check if my clock index are smaller than
	public boolean isSmaller(Clock c) {
		int count = 0;

		if (this.vector.length == c.getSize()) {
			for (int i = 0; i < c.getSize(); i++) {
				// Do not compare itself, cause we only want to update index by
				// the nodes itself
				if (i != index) {
					// Only compare when count is 0
					if (count == 0 && this.vector[i] < c.getVector()[i]) {
						count++;
					} else if (this.vector[i] > c.getVector()[i]) {
						return false;
					}
				}
			}
			// if there is one element strictly smaller while others are leq
			// to, then true
			if (count > 0)
				return true;

		}

		return false;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int[] getVector() {
		return this.vector;
	}

	public int getSize() {
		return this.vector.length;
	}

	public int get(int i) {
		return vector[i];
	}

	public void increment(int i) {
		vector[i]++;
	}

	public boolean Equal(Clock clock) {
		for (int i = 0; i < vector.length; i++) {
			if (vector[i] != clock.vector[i])
				return false;
		}
		return true;
	}

	public String toString() {
		String s = "[";
		for (int i = 0; i < vector.length; i++) {
			s = s + vector[i] + ",";
		}
		s = s.substring(0, s.length() - 1);
		s = s + "]";
		return s;

	}

}
