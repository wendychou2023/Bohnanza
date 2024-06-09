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

public class CenterTest {
	
	private enum IntSequence {
		IS_1(new int[] {80}),
		IS_2(new int[] {80, 80}),
		IS_3(new int[] {80, 120}),
		IS_4(new int[] {75, 125});
		
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
	
	@DisplayName("Center properly guards conditions for arguments.")
	@Test
	void testIllegalArguments() {		
		assertThrows(IllegalArgumentException.class, () -> {
			Compartment.center(new int[] {-10}, 10, 10);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			Compartment.center(new int[] {10}, -10, 10);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Compartment.center(new int[] {10}, 10, -10);
		});
	}
	
	@DisplayName("Center uses smaller value as start.")
	@Test
	void testSwap() {		
		final int start = 65;
		final int end = 5;
		
		
		final int[] startPositions = Compartment.center(new int[] {80}, start, end);

		assertNotNull(startPositions);
		
		assertThat(ArrayUtils.toObject(startPositions), IsArrayWithSize.arrayWithSize(1));
		
		assertEquals(end, startPositions[0]);
	}
	
	@DisplayName("Center for null array.")
	@Test
	void testNull() {
		final int start = 5;
		final int end = 395;
		
		final int[] startPositions = Compartment.center(null, start, end);

		assertNotNull(startPositions);
		
		assertThat(ArrayUtils.toObject(startPositions), IsArrayWithSize.emptyArray());
	}
	
	@DisplayName("Center for empty array.")
	@Test
	void testEmpty() {
		final int start = 5;
		final int end = 395;
		
		final int[] startPositions = Compartment.center(new int[0], start, end);

		assertNotNull(startPositions);
		
		assertThat(ArrayUtils.toObject(startPositions), IsArrayWithSize.emptyArray());
	}

	@ParameterizedTest(name = "{index} Center cards of dimension smaller than range. ({0})")
	@EnumSource(value = IntSequence.class)
	void testEnoughSpace(final IntSequence ints) {
		final int start = 5;
		final int end = 135;
		final int[] dimensions = ints.getInts();
		
		final int[] startPositions = Compartment.center(dimensions, start, end);

		assertNotNull(startPositions);
		
		assertThat(ArrayUtils.toObject(startPositions), IsArrayWithSize.arrayWithSize(dimensions.length));
		
		for (int i = 0; i < dimensions.length; i++) {
			int leftSpace = startPositions[i] - start;
			int rightSpace = end - (startPositions[i] + dimensions[i]);
			assertThat(leftSpace - rightSpace, OrderingComparison.lessThanOrEqualTo(1));
		}
	}

	@ParameterizedTest(name = "{index} Center cards of dimension larger than range. ({0})")
	@EnumSource(value = IntSequence.class)
	void testRangeTooSmall(final IntSequence ints) {
		final int start = 5;
		final int end = 75;
		final int[] dimensions = ints.getInts();
		
		final int[] startPositions = Compartment.center(dimensions, start, end);

		assertNotNull(startPositions);
		
		assertThat(ArrayUtils.toObject(startPositions), IsArrayWithSize.arrayWithSize(dimensions.length));
		
		assertThat(Arrays.asList(ArrayUtils.toObject(startPositions)), Every.everyItem(Is.is(start)));
	}

}
