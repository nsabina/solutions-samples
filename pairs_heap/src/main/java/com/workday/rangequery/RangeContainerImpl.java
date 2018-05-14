package com.workday.rangequery;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Container interface implementation
 *
 */
public class RangeContainerImpl implements RangeContainer {

	private ContainerEntry entries[];

	/**
	 * Container constructor to build and pre-sort entries from given data array
	 * 
	 * @param data
	 */
	public RangeContainerImpl(long[] data) {
		ContainerEntry entries[] = new ContainerEntry[data.length];
		for (int i = 0; i < data.length; i++) {
			entries[i] = new ContainerEntry(data[i], (short) i);
		}
		Arrays.sort(entries);
		this.entries = entries;
	}

	/* (non-Javadoc)
	 * @see com.workday.rangequery.RangeContainer#findIdsInRange(long, long, boolean, boolean)
	 */
	public Ids findIdsInRange(long fromValue, long toValue,
			boolean fromInclusive, boolean toInclusive) {
		long shift = (long) 1;
		int startIndex = boundingIndexOf(entries, 0, entries.length,
				fromInclusive ? fromValue : fromValue + shift, true);
		int endIndex = boundingIndexOf(entries, 0, entries.length,
				toInclusive ? toValue : toValue - shift, false);

		PriorityQueue<ContainerEntry> minHeapBuffer = null;
		if (startIndex <= endIndex) {
			minHeapBuffer = new PriorityQueue<ContainerEntry>(endIndex
					- startIndex + 1, new ContainerIndexComparator());
			while (startIndex <= endIndex) {
				minHeapBuffer.add(entries[startIndex++]);
			}
		}

		return new IdsImpl(minHeapBuffer);
	}

	/**
	 * Binary search for the value index
	 * 
	 * @param a
	 * @param fromIndex
	 * @param toIndex
	 * @param key
	 * @param lowHigh
	 * @return bounding index for a given key value
	 */
	static int boundingIndexOf(Object[] a, int fromIndex, int toIndex,
			Long key, boolean lowHigh) {
		int low = fromIndex;
		int high = toIndex - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			@SuppressWarnings("rawtypes")
			Comparable midVal = (Comparable) a[mid];
			int cmp = (int) ((ContainerEntry) midVal).valueOffset(key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid;
		}
		return lowHigh ? low : high;
	}

	/**
	 * Nested class for Container Entry
	 *
	 */
	class ContainerEntry implements Comparable<ContainerEntry> {
		long value;
		short originalIndex;

		public ContainerEntry(long value, short originalIndex) {
			this.value = value;
			this.originalIndex = originalIndex;
		}

		public int compareTo(ContainerEntry o) {
			return Long.compare(value, o.value);
		}

		public int valueOffset(long queryValue) {
			return Long.compare(value, queryValue);
		}

		public long getValue() {
			return value;
		}

		public short getOriginalIndex() {
			return originalIndex;
		}

	}

	/**
	 * Nested class to compare Container Entries based on original index value
	 *
	 */
	class ContainerIndexComparator implements Comparator<ContainerEntry> {

		public int compare(ContainerEntry data1, ContainerEntry data2) {
			return data1.getOriginalIndex() - data2.getOriginalIndex();
		}

	}

	/**
	 * Nested class to implement Ids cursor to fetch original indexes off
	 * minHeap
	 *
	 */
	class IdsImpl implements Ids {

		private PriorityQueue<ContainerEntry> minHeap;

		IdsImpl(PriorityQueue<ContainerEntry> pQueue) {
			this.minHeap = pQueue;
		}

		public short nextId() {
			return minHeap != null && minHeap.size() > 0 ? minHeap.remove()
					.getOriginalIndex() : Ids.END_OF_IDS;
		}
	}

}