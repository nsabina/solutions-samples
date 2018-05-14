package com.workday.rangequery;

import java.util.Arrays;

/**
 * Implementation fo Container Factory class
 *
 */
public class RangeContainerFactoryImpl implements RangeContainerFactory {

	/* (non-Javadoc)
	 * @see com.workday.rangequery.RangeContainerFactory#createContainer(long[])
	 */
	public RangeContainer createContainer(long[] data) {
		short[] index = new short[data.length];
		// Initialize index array with 0, 1, 2, 3... prior to sorting
		for (short i = 0; i < index.length; i++) {
			index[i] = i;
		}
		sortIndex(data, index);
		return new RangeContainerImpl(data, index);
	}

	/**
	 * Comparator for indexes to sort based on original array values
	 */
	private static class IndexedComparator {
		long data[];

		public IndexedComparator(long[] data) {
			this.data = data;
		}

		int compare(short d1, short d2) {
			return Long.compare(data[d1], data[d2]);
		}
	}

	private static final int INSERTIONSORT_THRESHOLD = 7;

	/**
	 * Sorts index array based on the actual data its elements pointing to.
	 * 
	 * @param data
	 * @param index
	 */
	private static void sortIndex(long data[], short[] index) {

		short[] aux = Arrays.copyOf(index, index.length);
		mergeSort(aux, index, 0, index.length, 0, new IndexedComparator(data));
	}

	/**
	 * This is modified Java legacy merge sort.
	 */
	private static void mergeSort(short[] src, short[] dest, int low, int high,
			int off, IndexedComparator c) {
		int length = high - low;

		if (length < INSERTIONSORT_THRESHOLD) {
			for (int i = low; i < high; i++)
				for (int j = i; j > low && c.compare(dest[j - 1], dest[j]) > 0; j--)
					swap(dest, j, j - 1);
			return;
		}

		int destLow = low;
		int destHigh = high;
		low += off;
		high += off;
		int mid = (low + high) >>> 1;
		mergeSort(dest, src, low, mid, -off, c);
		mergeSort(dest, src, mid, high, -off, c);

		if (c.compare(src[mid - 1], src[mid]) <= 0) {
			System.arraycopy(src, low, dest, destLow, length);
			return;
		}

		for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
			if (q >= high || p < mid && c.compare(src[p], src[q]) <= 0)
				dest[i] = src[p++];
			else
				dest[i] = src[q++];
		}
	}

	private static void swap(short[] x, int a, int b) {
		long t = x[a];
		x[a] = x[b];
		x[b] = (short) t;
	}

}
