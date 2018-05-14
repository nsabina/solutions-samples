package com.workday.rangequery;

interface RangeContainer {

	/**
	 * @return the Ids of all instances found in the container that have data
	 *         value between fromValue and toValue with optional inclusivity.
	 *         The ids should be returned in ascending order when retrieved
	 *         using nextId().
	 */
	Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive);
}
