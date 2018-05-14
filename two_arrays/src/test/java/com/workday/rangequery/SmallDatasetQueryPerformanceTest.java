package com.workday.rangequery;

import org.junit.Before;
import org.junit.Test;

import com.workday.rangequery.Ids;
import com.workday.rangequery.RangeContainer;
import com.workday.rangequery.RangeContainerFactory;
import com.workday.rangequery.RangeContainerFactoryImpl;

public class SmallDatasetQueryPerformanceTest {

	private RangeContainer rc;

	@Before
	public void setUp() {
		RangeContainerFactory rf = new RangeContainerFactoryImpl();
		rc = rf.createContainer(new long[] { 10, 12, 17, 21, 2, 15, 16 });
	}

	@Test
	public void checkARangeQueryTime() {
		long startTime = System.nanoTime();
		Ids ids = rc.findIdsInRange(14, 17, true, true);
		long stopTime = System.nanoTime();
        System.out.println("Two Arrays: Small Dataset Query 1: " + (stopTime - startTime));

		startTime = System.nanoTime();
		ids = rc.findIdsInRange(14, 17, true, false);
		stopTime = System.nanoTime();
        System.out.println("Two Arrays: Small Dataset Query 2: " + (stopTime - startTime));

		startTime = System.nanoTime();
		ids = rc.findIdsInRange(20, Long.MAX_VALUE, false, true);
		stopTime = System.nanoTime();
        System.out.println("Two Arrays: Small Dataset Query 3: " + (stopTime - startTime));
	}

	@Test
	public void checkARangeQueryMemory() {
		Ids ids = rc.findIdsInRange(14, 17, true, true);
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Two Arrays: Small Dataset Query memory in bytes : " + memory);
	}

}
