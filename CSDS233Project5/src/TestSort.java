import org.junit.*;

import java.util.*;

/**
 * This class tests the Sort class
 */
public class TestSort
{
	private final long[][] nanoTimes = new long[9][9];

	/**
	 * Tests the sort class. Calls generateRandomArray method with sizes of n = 10, 20, 50, 100, 200, 500, 1000, 2000,
	 * and 5000. Averages out the runtime over 100 times and prints it out in a table
	 */
	@Test
	public void testSort()
	{
		// size of unsorted arrays to compute
		int[] size = new int[] {10, 20, 50, 100, 200, 500, 1000, 2000, 5000};
		// number of times to run each test - used for generating an average
		int numTimesRun = 100;
		// stores every nanoTime result
		long[][][] allNanoTimes = new long[numTimesRun][9][9];

		// run 100 random tests of each type and add store each nanoTime in allNanoTimes
		for (int i = 0; i < numTimesRun; i++)
		{
			// Generate nanoTimes and test
			int index = 0;
			for (int k : size)
				generateRandomArray(k, index++);

			allNanoTimes[i] = nanoTimes.clone();
		}

		// Average out the data and store it in nanoTimes
		for (int j = 0; j < 9; j++)
		{
			for (int k = 0; k < 9; k++)
			{
				int sum = 0;
				for (int i = 0; i < numTimesRun; i++)
					sum += allNanoTimes[i][j][k] / numTimesRun;
				nanoTimes[j][k] = sum;
			}
		}

		// Benchmarking - print average nanoTime
		System.out.println("\nRuntime using Java's System.nanoTime():");
		System.out.println("\t\t\t\tAPI:\t\tInsertion:\tQuick:\t\tMerge:\t\tBucket:\t\tHeap:\t\tSelection:\tBubble:\t\tShell");
		for (int i = 0; i < nanoTimes.length; i++)
		{
			System.out.print("size = " + size[i] + ":\t");
			// format
			if (size[i] < 1000)
				System.out.print("\t");

			for (int j = 0; j < nanoTimes[i].length; j++)
			{
				System.out.print(nanoTimes[i][j] + "\t");
				if (nanoTimes[i][j] < 10000000)
				{
					System.out.print("\t");
					if (nanoTimes[i][j] < 1000)
						System.out.print("\t");
				}
			}
			System.out.println();
		}
	}

	/**
	 * Generates an array of random integers of size n between the numbers 0 and n * 2. Calls other methods to test
	 * the sorting algorithms
	 *
	 * @param n random input size
	 */
	private void generateRandomArray(int n, int i)
	{
		// Generate random arrays
		int[] unsortedArray = Sort.randomArray(n, 0, n * 2);

		// Use api sort as a default sort to compare other sorts to
		int[] sortedArray = apiSort(unsortedArray.clone(), i);

		// call methods that test sorts
		testInsertionSort(unsortedArray.clone(), sortedArray, i);
		testQuickSort(unsortedArray.clone(), sortedArray, i);
		testMergeSort(unsortedArray.clone(), sortedArray, i);
		testBucketSort(unsortedArray.clone(), sortedArray, i);
		testHeapSort(unsortedArray.clone(), sortedArray, i);
		testBubbleSort(unsortedArray.clone(), sortedArray, i);
		testSelectionSort(unsortedArray.clone(), sortedArray, i);
		testShellSort(unsortedArray.clone(), sortedArray, i);
	}

	/**
	 * Tests runtime length of java's API sort method
	 *
	 * @param array unsorted array to sort
	 * @param n     first index to slot change in nanoTime into
	 * @return the sorted array
	 */
	private int[] apiSort(int[] array, int n)
	{
		long lastTime = System.nanoTime();
		Arrays.sort(array);
		long deltaTime = System.nanoTime() - lastTime;
		nanoTimes[n][0] = deltaTime;
		return array;
	}

	/**
	 * Tests insertion sort algorithm. Compares already sorted array to unsorted array after calling sort
	 *
	 * @param unsorted array to sort
	 * @param sorted   already sorted array
	 * @param n        first index to slot change in nanoTime into
	 */
	private void testInsertionSort(int[] unsorted, int[] sorted, int n)
	{
		long lastTime = System.nanoTime();
		Sort.insertionSort(unsorted);
		long deltaTime = System.nanoTime() - lastTime;
		nanoTimes[n][1] = deltaTime;
		Assert.assertArrayEquals(unsorted, sorted);
	}

	/**
	 * Tests quick sort algorithm. Compares already sorted array to unsorted array after calling sort
	 *
	 * @param unsorted array to sort
	 * @param sorted   already sorted array
	 * @param n        first index to slot change in nanoTime into
	 */
	private void testQuickSort(int[] unsorted, int[] sorted, int n)
	{
		long lastTime = System.nanoTime();
		Sort.quickSort(unsorted);
		long deltaTime = System.nanoTime() - lastTime;
		nanoTimes[n][2] = deltaTime;
		Assert.assertArrayEquals(unsorted, sorted);
	}

	/**
	 * Tests merge sort algorithm. Compares already sorted array to unsorted array after calling sort
	 *
	 * @param unsorted array to sort
	 * @param sorted   already sorted array
	 * @param n        first index to slot change in nanoTime into
	 */
	private void testMergeSort(int[] unsorted, int[] sorted, int n)
	{
		long lastTime = System.nanoTime();
		Sort.mergeSort(unsorted);
		long deltaTime = System.nanoTime() - lastTime;
		nanoTimes[n][3] = deltaTime;
		Assert.assertArrayEquals(unsorted, sorted);
	}

	/**
	 * Tests merge sort algorithm. Compares already sorted array to unsorted array after calling sort
	 *
	 * @param unsorted array to sort
	 * @param sorted   already sorted array
	 * @param n        first index to slot change in nanoTime into
	 */
	private void testBucketSort(int[] unsorted, int[] sorted, int n)
	{
		long lastTime = System.nanoTime();
		Sort.bucketSort(unsorted);
		long deltaTime = System.nanoTime() - lastTime;
		nanoTimes[n][4] = deltaTime;
		Assert.assertArrayEquals(unsorted, sorted);
	}

	/**
	 * Tests heap sort algorithm. Compares already sorted array to unsorted array after calling sort
	 *
	 * @param unsorted array to sort
	 * @param sorted   already sorted array
	 * @param n        first index to slot change in nanoTime into
	 */
	private void testHeapSort(int[] unsorted, int[] sorted, int n)
	{
		long lastTime = System.nanoTime();
		Sort.heapSort(unsorted);
		long deltaTime = System.nanoTime() - lastTime;
		nanoTimes[n][5] = deltaTime;
		Assert.assertArrayEquals(unsorted, sorted);
	}

	/**
	 * Tests selection sort algorithm. Compares already sorted array to unsorted array after calling sort
	 *
	 * @param unsorted array to sort
	 * @param sorted   already sorted array
	 * @param n        first index to slot change in nanoTime into
	 */
	private void testSelectionSort(int[] unsorted, int[] sorted, int n)
	{
		long lastTime = System.nanoTime();
		Sort.selectionSort(unsorted);
		long deltaTime = System.nanoTime() - lastTime;
		nanoTimes[n][6] = deltaTime;
		Assert.assertArrayEquals(unsorted, sorted);
	}

	/**
	 * Tests bubble sort algorithm. Compares already sorted array to unsorted array after calling sort
	 *
	 * @param unsorted array to sort
	 * @param sorted   already sorted array
	 * @param n        first index to slot change in nanoTime into
	 */
	private void testBubbleSort(int[] unsorted, int[] sorted, int n)
	{
		long lastTime = System.nanoTime();
		Sort.bubbleSort(unsorted);
		long deltaTime = System.nanoTime() - lastTime;
		nanoTimes[n][7] = deltaTime;
		Assert.assertArrayEquals(unsorted, sorted);
	}

	/**
	 * Tests shell sort algorithm. Compares already sorted array to unsorted array after calling sort
	 *
	 * @param unsorted array to sort
	 * @param sorted   already sorted array
	 * @param n        first index to slot change in nanoTime into
	 */
	private void testShellSort(int[] unsorted, int[] sorted, int n)
	{
		long lastTime = System.nanoTime();
		Sort.shellSort(unsorted);
		long deltaTime = System.nanoTime() - lastTime;
		nanoTimes[n][8] = deltaTime;
		Assert.assertArrayEquals(unsorted, sorted);
	}
}
