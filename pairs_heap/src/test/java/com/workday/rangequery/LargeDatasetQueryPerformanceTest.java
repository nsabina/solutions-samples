package com.workday.rangequery;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.workday.rangequery.Ids;
import com.workday.rangequery.RangeContainer;
import com.workday.rangequery.RangeContainerFactory;
import com.workday.rangequery.RangeContainerFactoryImpl;

public class LargeDatasetQueryPerformanceTest {

	private RangeContainer rc;

	@Before
	public void setUp() {
		RangeContainerFactory rf = new RangeContainerFactoryImpl();
		rc = rf.createContainer(createArray());
	}

	@Test
	public void checkARangeQueryTime() {
		long startTime = System.nanoTime();
		Ids ids = rc.findIdsInRange(14, 12517, true, true);
		long stopTime = System.nanoTime();
		System.out.println("Minheap Pairs: Large Dataset Query 1: " + (stopTime - startTime));

		startTime = System.nanoTime();
		ids = rc.findIdsInRange(14, 517, true, false);
		stopTime = System.nanoTime();
		System.out.println("Minheap Pairs: Large Dataset Query 2: " + (stopTime - startTime));

		startTime = System.nanoTime();
		ids = rc.findIdsInRange(Long.MAX_VALUE-100, Long.MAX_VALUE, false, true);
		stopTime = System.nanoTime();
		System.out.println("Minheap Pairs: Large Dataset Query 3: " + (stopTime - startTime));
	}

	@Test
	public void checkARangeQueryMemory() {
		Ids ids = rc.findIdsInRange(14, 12517, true, true);
		// Get the Java runtime
		Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
		runtime.gc();
		// Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		System.out.println("Minheap Pairs: Large Dataset Query memory in bytes : " + memory);
	}

	private static long[] createArray() {
		long[] anArray = new long[32000];
		for (int i = 0; i < anArray.length; i++) {
			anArray[i] = randomFill();
		}
		return anArray;
	}

	private static long randomFill() {

		Random rand = new Random();
		long randomNum = rand.nextLong();
		return randomNum;
	}

}
