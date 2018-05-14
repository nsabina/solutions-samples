package com.workday.rangequery;

interface RangeContainerFactory {
	/**
	 * builds an immutable container optimized for range queries. Data is
	 * expected to be 32k items or less. The position in the â€œdataâ€? array
	 * represents the â€œidâ€? for that instance in question. For the
	 * â€œPayrollResultâ€? example before, the â€œidâ€? might be the workerâ€™s employee
	 * number, the data value is the corresponding net pay. E.g, data[5]=2000
	 * means that employee #6 has net pay of 2000.
	 */
	RangeContainer createContainer(long[] data);
}
