package com.workday.rangequery;

/**
 * Factory Implementation class to initialize a container
 *
 */
public class RangeContainerFactoryImpl implements RangeContainerFactory {

	/* (non-Javadoc)
	 * @see com.workday.rangequery.RangeContainerFactory#createContainer(long[])
	 */
	public RangeContainer createContainer(long[] data) {
		return new RangeContainerImpl(data);
	}

}
