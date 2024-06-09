package io.bitbucket.plt.sdp.bohnanza.gui;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.collection.IsArrayWithSize;
import org.hamcrest.core.Every;
import org.hamcrest.core.Is;
import org.hamcrest.number.OrderingComparison;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

public class DistributionTest {
	
	private enum IntSequence {
		IS_1(new int[] {60, 60, 60}),
		IS_2(new int[] {80, 60, 60}),
		IS_3(new int[] {60, 80, 60}),
		IS_4(new int[] {60, 60, 80}),
		IS_5(new int[] {80, 80, 60}),
		IS_6(new int[] {60, 80, 80}),
		IS_7(new int[] {80, 60, 80}),
		
		IS_8(new int[] {60, 60}),
		IS_9(new int[] {80, 60, 80}),
		IS_10(new int[] {80, 60, 80, 80, 60});
		
		private int[] ints;
		IntSequence(int[] ints) {
			this.ints = ints;
		}
		
		public int[] getInts() {
			return ints;
		}
		
		@Override
		public String toString() {
			return "Ints: " + Arrays.toString(ints);
		}
	}
	
	@DisplayName("Distribute properly guards conditions for arguments.")
	@Test
	void testIllegalArguments() {		
		assertThrows(IllegalArgumentException.class, () -> {
			Compartment.distribute(new int[] {-10}, 10, 10);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			Compartment.distribute(new int[] {10}, -10, 10);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Compartment.distribute(new int[] {10}, 10, -10);
		});
	}
	
	@DisplayName("Distribute uses smaller value as start.")
	@Test
	void testSwap() {		
		final int start = 65;
		final int end = 5;
		
		
		final int[] startPositions = Compartment.distribute(new int[] {80}, start, end);

		assertNotNull(startPositions);
		
		assertThat(ArrayUtils.toObject(startPositions), IsArrayWithSize.arrayWithSize(1));
		
		assertEquals(end, startPositions[0]);
	}
	
	@DisplayName("Distribute for null array.")
	@Test
	void testNull() {
		final int start = 5;
		final int end = 395;
		
		final int[] startPositions = Compartment.distribute(null, start, end);

		assertNotNull(startPositions);
		
		assertThat(ArrayUtils.toObject(startPositions), IsArrayWithSize.emptyArray());
	}
	
	@DisplayName("Distribute for empty array.")
	@Test
	void testEmpty() {
		final int start = 5;
		final int end = 395;
		
		final int[] startPositions = Compartment.distribute(new int[0], start, end);

		assertNotNull(startPositions);
		
		assertThat(ArrayUtils.toObject(startPositions), IsArrayWithSize.emptyArray());
	}

	@ParameterizedTest(name = "{index} Distribute single card of dimension {0}")
	@ValueSource(ints = {0, 60, 90, 100})
	void testSingle(int dimension) {
		final int start = 5;
		final int end = 95;
		
		final int[] startPositions = Compartment.distribute(new int[] {dimension}, start, end);

		assertNotNull(startPositions);
		
		assertThat(ArrayUtils.toObject(startPositions), IsArrayWithSize.arrayWithSize(1));
		assertThat(startPositions[0], Is.is(start));
	}

	
	@ParameterizedTest(name = "{index} Distribute without overlap has equi-distant spacing. ({0})")
	@EnumSource(value = IntSequence.class)
	void testNoOverlap(final IntSequence ints) {
		final int start = 5;
		final int end = 395;
		final int[] dimensions = ints.getInts();
		
		final int[] startPositions = Compartment.distribute(dimensions, start, end);

		// correct number of computed y positions
		assertEquals(dimensions.length, startPositions.length);
		
		// start at start
		assertEquals(startPositions[0], start);
		
		// end at end
		assertEquals(startPositions[dimensions.length - 1] + dimensions[dimensions.length  - 1], end);
		
		// distance between cards is equal (+/- 1)
		int minDiff = Integer.MAX_VALUE;
		int maxDiff = Integer.MIN_VALUE;
		int prevEndPos = start + dimensions[0];
		for (int i = 1; i < dimensions.length; i++) {
			int diff = startPositions[i] - prevEndPos;
			if (diff <= minDiff)
				minDiff = diff;
			if (diff >= maxDiff)
				maxDiff = diff;
			prevEndPos = startPositions[i] + dimensions[i];
		}
		
		// distance is always positive (i.e., first to last element arranged from top to bottom)
		assertThat(minDiff, OrderingComparison.greaterThanOrEqualTo(0));
		assertThat(maxDiff, OrderingComparison.greaterThanOrEqualTo(0));
		// allow minor rounding errors
		assertThat(maxDiff - minDiff, OrderingComparison.lessThanOrEqualTo(1));
	}
	
	@ParameterizedTest(name = "{index} Distribute with overlap has equi-distant starting position. ({0})")
	@EnumSource(value = IntSequence.class)
	void testOverlap(final IntSequence ints) {
		final int start = 5;
		final int end = 105;
		final int[] dimensions = ints.getInts();
		
		final int[] startPositions = Compartment.distribute(dimensions, start, end);

		// correct number of computed y positions
		assertEquals(dimensions.length, startPositions.length);
		
		// start at start
		assertEquals(startPositions[0], start);
		
		// end at end
		assertEquals(startPositions[dimensions.length - 1] + dimensions[dimensions.length  - 1], end);
		
		// distance between cards is equal (+/- 1)
		int minDiff = Integer.MAX_VALUE;
		int maxDiff = Integer.MIN_VALUE;
		int prevstartPos = startPositions[0];
		for (int i = 1; i < dimensions.length; i++) {
			int diff = startPositions[i] - prevstartPos;
			if (diff <= minDiff)
				minDiff = diff;
			if (diff >= maxDiff)
				maxDiff = diff;
			prevstartPos = startPositions[i];
		}
		
		// distance is always positive (i.e., first to last element arranged from top to bottom)
		assertThat(minDiff, OrderingComparison.greaterThanOrEqualTo(0));
		assertThat(maxDiff, OrderingComparison.greaterThanOrEqualTo(0));
		// allow minor rounding errors
		assertThat(maxDiff - minDiff, OrderingComparison.lessThanOrEqualTo(1));
	}

	@ParameterizedTest(name = "{index} Distribute with too little room has all cards starting at Beginning. ({0})")
	@EnumSource(value = IntSequence.class)
	void testNoSpace(final IntSequence ints) {
		final int start = 5;
		final int end = 55;
		final int[] dimensions = ints.getInts();
		
		final int[] startPositions = Compartment.distribute(dimensions, start, end);

		// correct number of computed y positions
		assertEquals(dimensions.length, startPositions.length);
		
		// start at start
		assertEquals(startPositions[0], start);
		
		// all cards start at start		
		
		assertThat(Arrays.asList(ArrayUtils.toObject(startPositions)), Every.everyItem(Is.is(start)));
	}

}
