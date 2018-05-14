package com.workday.rangequery;

import java.util.Arrays;

/**
 * Container implementation class
 *
 */
public class RangeContainerImpl implements RangeContainer {

	private long[] data;
	private short[] index;

	/**
	 * @param data
	 * @param index
	 */
	public RangeContainerImpl(long[] data, short[] index) {
		this.data = data;
		this.index = index;
	}

	/* (non-Javadoc)
	 * @see com.workday.rangequery.RangeContainer#findIdsInRange(long, long, boolean, boolean)
	 */
	public Ids findIdsInRange(long fromValue, long toValue,
			boolean fromInclusive, boolean toInclusive) {

		return new IdsImpl(searchAndSort(data, index, fromValue, toValue,
				fromInclusive, toInclusive));
	}

	/**
	 * Implementation of Ids interface to provide cursor to fetch original
	 * indexes in sorted order
	 *
	 */
	class IdsImpl implements Ids {

		private int currentIndex;
		private short data[];

		IdsImpl(short data[]) {
			this.data = data;
		}

		public short nextId() {
			if (currentIndex >= data.length) {
				return END_OF_IDS;
			}
			return data[currentIndex++];
		}
	}

	/**
	 * Search and sort performed for each query execution
	 * 
	 * @param data
	 * @param index
	 * @param lowBound
	 * @param highBound
	 * @param fromInclusive
	 * @param toInclusive
	 * @return
	 */
	private short[] searchAndSort(long[] data, short[] index, long lowBound,
			long highBound, boolean fromInclusive, boolean toInclusive) {
		int start = boundingIndexOf(data, index, 0, index.length,
				fromInclusive ? lowBound : lowBound + 1, true);
		int end = boundingIndexOf(data, index, 0, index.length,
				toInclusive ? highBound : highBound - 1, false);

		int length = end - start + 1;
		short target[] = new short[length];
		for (int i = 0; i < target.length; i++) {
			target[i] = index[start + i];
		}
		Arrays.sort(target);

		return target;
	}

	/**
	 * Bounding index for a key value
	 * 
	 * @param data
	 * @param index
	 * @param fromIndex
	 * @param toIndex
	 * @param key
	 * @param lowHigh
	 * @return
	 */
	static int boundingIndexOf(long[] data, short index[], int fromIndex,
			int toIndex, long key, boolean lowHigh) {
		int low = fromIndex;
		int high = toIndex - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			long midVal = data[index[mid]];

			if (midVal < key) {
				low = mid + 1;
			} else if (midVal > key) {
				high = mid - 1;
			} else {
				return mid;
			}
		}

		return lowHigh ? low : high;
	}

}
