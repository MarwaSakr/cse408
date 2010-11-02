import magick.PixelPacket;

public class Bluesort  {
	private static PixelPacket[] numbers;
	private static int number;

	public static void sort(PixelPacket[] values) {
		// Check for empty or null array
		if (values ==null || values.length==0){
			return;
		}
		numbers = values;
		number = values.length;
		quicksort((int) (number * 0.75), number - 1);
	}

	private static void quicksort(int low, int high) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		PixelPacket pivot = numbers[low + (high-low)/2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller then the pivot
			// element then get the next element from the left list
			while (numbers[i].getBlue() < pivot.getBlue()) {
				i++;
			}
			// If the current value from the right list is larger then the pivot
			// element then get the next element from the right list
			while (numbers[j].getBlue() > pivot.getBlue()) {
				j--;
			}

			// If we have found a values in the left list which is larger then
			// the pivot element and if we have found a value in the right list
			// which is smaller then the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
	}

	private static void exchange(int i, int j) {
		PixelPacket temp = numbers[i];
		numbers[i] = numbers[j];
		numbers[j] = temp;
	}
}
