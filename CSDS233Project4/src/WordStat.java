import java.util.*;

public class WordStat
{
	/* Words */
	private HashTable wordsByNumInstances;
	private HashTable wordRanks;
	private String[] mostCommonWords;

	/* Word Pairs */
	private HashTable wordPairsByNumInstances;
	private HashTable wordPairRanks;
	private String[] mostCommonWordPairs;

	//
	private final String[] words;

	/**
	 * @param file to compute statistics from
	 */
	public WordStat(String file)
	{
		Tokenizer t = new Tokenizer(file);
		words = t.wordList().toArray(new String[0]);
		initializeAndAssignFields(t);
	}

	/**
	 * @param words list of Strings to compute from
	 */
	public WordStat(String[] words)
	{
		Tokenizer t = new Tokenizer(words);
		this.words = words;
		initializeAndAssignFields(t);
	}

	/**
	 * Helper method for both constructors to initialize the HashTable
	 *
	 * @param t tokenizer
	 */
	private void initializeAndAssignFields(Tokenizer t)
	{
		// List of words in order of how they appear
		ArrayList<String> words = t.wordList();
		int len = words.size();

		/* Single Words */
		// Initialize fields
		wordsByNumInstances = new HashTable(len);
		wordRanks = new HashTable(len);
		mostCommonWords = new String[len];

		// Add items into wordsByNumInstances
		for (String word : words)
			wordsByNumInstances.update(word, wordCount(word) + 1);

		// Add items into a heap
		Heap<HashEntry> heap = new Heap<>();
		for (HashEntry hashEntry : wordsByNumInstances.getTable())
			if (hashEntry != null)
				heap.insert(hashEntry);

		// Remove items from the heap and add items into wordRanks and mostCommonWords
		int index = 0;
		HashEntry lastEntry = null;
		int lastRank = -1;
		while (!heap.isEmpty())
		{
			HashEntry foobar = heap.delete();
			if (lastEntry == null || lastEntry.getValue() != foobar.getValue())
				wordRanks.put(foobar.getKey(), lastRank = index + 1);
			else
				wordRanks.put(foobar.getKey(), lastRank);

			mostCommonWords[index++] = foobar.getKey();
			lastEntry = foobar;
		}
		// Remove null Strings from mostCommonWords
		mostCommonWords = Arrays.stream(mostCommonWords).filter(Objects::nonNull).toArray(String[]::new);

		/* Word Pairs */
		// Initialize fields
		wordPairsByNumInstances = new HashTable(len);
		wordPairRanks = new HashTable(len);
		mostCommonWordPairs = new String[len];

		// Add items into wordPairsByNumInstances
		for (int i = 1; i < words.size(); i++)
			wordPairsByNumInstances.update(words.get(i - 1) + " " + words.get(i),
					1 + wordPairsByNumInstances.get(words.get(i - 1) + " " + words.get(i)));

		//TODO: Continue here:
		// Add items into a heap
		// Clear All shouldn't be necessary. But can't hurt
		heap.clearAll();
		/*
		for (HashEntry hashEntry : wordPairsByNumInstances.getTable())
			if (hashEntry != null)
				heap.insert(hashEntry);
		*/
		for (int i = 1; i < words.size(); i++)
			heap.insert(new HashEntry(words.get(i - 1) + " " + words.get(i),
					1 /* + wordPairRanks.get(words.get(i - 1) + " " + words.get(i))*/ ));
		//System.out.println(heap.size());



		// Word Pair Ranks
		index = 0;
		while (!heap.isEmpty())
		{
			HashEntry foobar = heap.delete();
			wordPairRanks.update(foobar.getKey(), foobar.getValue());
			mostCommonWordPairs[index++] = foobar.getKey();
		}
		// Remove null Strings from mostCommonWordPairs
		mostCommonWordPairs = Arrays.stream(mostCommonWordPairs).filter(Objects::nonNull).toArray(String[]::new);
















		/*
		//TODO: Add each word (and the word prior) starting from index i = 1 to a new String[] called wordPairs
		for (int i = 1; i < words.size(); i++)
			wordPairsByNumInstances.update(words.get(i - 1) + " " + words.get(i),
					1 + wordPairsByNumInstances.get(words.get(i - 1) + " " + words.get(i)));





		// Create a heap to generate the most common words
		Heap<HashEntry> heap = new Heap<>();

		// Add items to the heap
		for (HashEntry hashEntry : wordsByNumInstances.getTable())
			if (hashEntry != null)
				heap.insert(hashEntry);

		// Remove all items from the heap and add them to the mostCommonEntries
		HashEntry[] mostCommonEntries = new HashEntry[heap.size()];
		for (int i = 0; i < mostCommonEntries.length; i++)
			mostCommonEntries[i] = heap.delete();

		mostCommonWords = new String[mostCommonEntries.length];
		for (int i = 0; i < mostCommonWords.length; i++)
			mostCommonWords[i] = mostCommonEntries[i].getKey();





		// Create max heap for most common word pairs
		//Heap<HashEntry> heap = new Heap<>();
		// Heap should already be empty, but doesn't hurt to clear all again just to be safe
		heap.clearAll();

		// Add values to heap
		//for (int i = 1; i < )

		/*
		for (int i = 0; i < mostCommonEntries.length; i++)
			for (int j = i + 1; j < mostCommonEntries.length; j++)
				heap.insert(new HashEntry(mostCommonEntries[i].getKey() + " " + mostCommonEntries[j].getKey(),
						mostCommonEntries[i].getValue() + mostCommonEntries[j].getValue()));
		*
		// Remove HashEntries from the heap and add it to mostCommonWordPairs
		mostCommonWordPairs = new String[heap.size()];
		for (int i = 0; i < mostCommonWordPairs.length; i++)
			mostCommonWordPairs[i] = heap.delete().getKey();

		//setMostCommonWords();
		*/
	}

	/**
	 * Helper method for initializeHashTable that sets the value associated with each word to the number of instances
	 * of the word and sets the most common words as an array
	 * <p>
	 * private void setMostCommonWords()
	 * {
	 * // Create a heap to generate the most common words
	 * Heap<HashEntry> heap = new Heap<>();
	 * <p>
	 * // Add items to the heap
	 * for (HashEntry hashEntry : words)
	 * if (hashEntry != null)
	 * heap.insert(hashEntry);
	 * <p>
	 * // Remove all items from the heap and add them to the mostCommonEntries
	 * HashEntry[] mostCommonEntries = new HashEntry[heap.size()];
	 * for (int i = 0; i < mostCommonEntries.length; i++)
	 * mostCommonEntries[i] = heap.delete();
	 * <p>
	 * mostCommonWords = new String[mostCommonEntries.length];
	 * for (int i = 0; i < mostCommonWords.length; i++)
	 * mostCommonWords[i] = mostCommonEntries[i].getKey();
	 * <p>
	 * setMostCommonWordPairs(mostCommonEntries);
	 * }
	 * <p>
	 * // TODO: Do most common word pairs for word pairs, not all possible combinations
	 * private void setMostCommonWordPairs(HashEntry[] mostCommonEntries)
	 * {
	 * // Create max heap for most common word pairs
	 * Heap<HashEntry> heap = new Heap<>();
	 * <p>
	 * // Add values to heap
	 * for (int i = 0; i < mostCommonEntries.length; i++)
	 * for (int j = i + 1; j < mostCommonEntries.length; j++)
	 * heap.insert(new HashEntry(mostCommonEntries[i].getKey() + " " + mostCommonEntries[j].getKey(),
	 * mostCommonEntries[i].getValue() + mostCommonEntries[j].getValue()));
	 * <p>
	 * // Remove HashEntries from the heap and add it to mostCommonWordPairs
	 * mostCommonWordPairs = new String[heap.size()];
	 * for (int i = 0; i < mostCommonWordPairs.length; i++)
	 * mostCommonWordPairs[i] = heap.delete().getKey();
	 * }
	 * <p>
	 * /**
	 *
	 * @param word to search for
	 * @return the count of the word argument. Return 0 if the word is not in the table
	 */
	public int wordCount(String word)
	{
		// get number of instances in table
		int count = wordsByNumInstances.get(word);
		// return count if in table, otherwise, return 0
		return count != -1 ? count : 0;
	}

	/**
	 * @param w1 word 1
	 * @param w2 word 2
	 * @return the sum of the return of wordCount for w1 and w2
	 */
	public int wordPairCount(String w1, String w2)
	{
		// get number of instances in table
		int count = wordPairsByNumInstances.get(w1 + " " + w2);
		// return count if in table, otherwise, return 0
		return count != -1 ? count : 0;
	}

	/**
	 * @param word the word to search for
	 * @return the rank of word, where rank 1 is the most common word
	 */
	public int wordRank(String word)
	{
		// Get rank of word
		int rank = wordRanks.get(word);
		// if word is in table return word rank, otherwise, return 0
		return rank != -1 ? rank : 0;
	}

	/**
	 * @param w1 word 1
	 * @param w2 word 2
	 * @return the rank of the word pair w1 w2. Return 0 if the word pair is not in the table
	 */
	public int wordPairRank(String w1, String w2)
	{
		// get number of instances in table
		int count = wordPairRanks.get(w1 + " " + w2);
		// return count if in table, otherwise, return 0
		return count != -1 ? count : 0;
	}

	/**
	 * @param k number of most common words
	 * @return a String array of the k most common words in decreasing order of their count
	 */
	public String[] mostCommonWords(int k)
	{
		return Arrays.copyOf(mostCommonWords, Math.min(Math.abs(k), mostCommonWords.length));
	}

	/**
	 * @param k number of the least common words
	 * @return a String array of the k the least common words in increasing order of their count
	 */
	public String[] leastCommonWords(int k)
	{
		k = Math.min(Math.abs(k), mostCommonWords.length);
		return Arrays.copyOfRange(mostCommonWords, mostCommonWords.length - Math.min(Math.abs(k), mostCommonWords.length), mostCommonWords.length);
	}

	/**
	 * @param k number of words to return
	 * @return the k most common word pair in a String array, where each element is in the form of "word1 word2"
	 * separated by a single space
	 */
	// TODO: Do most common word pairs for word pairs, not all possible combinations
	public String[] mostCommonWordPairs(int k)
	{
		return Arrays.copyOf(mostCommonWordPairs, Math.min(Math.abs(k), mostCommonWords.length));
	}

	/**
	 * @param k        number of words to return
	 * @param baseWord word to check for
	 * @param i        relative position to baseWord
	 * @return the k most common words at a given relative position i to baseWord. These are called "collocations." The
	 * relative position can be either +1 or -1 to indicate words following or preceding the base word. For example,
	 * mostCommonCollocs(10, "crash", -1) would return the 10 most common words that precede "crash"
	 */
	public String[] mostCommonCollocs(int k, String baseWord, int i)
	{
		baseWord = normalize(baseWord);

		if (Math.abs(i) != 1)
			return null;

		String[] words = new String[k];

		int wordPosition = -1;

		for (int index = 0; index < mostCommonWords.length; index++)
			if (mostCommonWords[index].equals(baseWord))
			{
				wordPosition = index;
				break;
			}

		if (wordPosition == -1)
			return null;


		for (int index = 0; k > 0; k--, wordPosition += i, index++)
		{
			if (wordPosition < 0 || wordPosition > mostCommonWords.length)
				return words;
			words[index] = mostCommonWords[wordPosition];
		}

		return words;
	}

	/**
	 * This method has the same functionality as mostCommonCollocs except that it excludes from consideration any words
	 * in the String array exclusions. This provides a means to obtain collocations that exclude common word pairs such
	 * as "of the" or "in a"
	 *
	 * @param k          number of words to return
	 * @param baseWord   word to check for
	 * @param i          relative position to baseWord
	 * @param exclusions list of words to exclude
	 * @return the k most common words excluding words in the parameter exclusions at a given relative position i to
	 * baseWord. These are called "collocations." The relative position can be either +1 or -1 to indicate words
	 * following or preceding the base word. For example, mostCommonCollocs(10, "crash", -1) would return the 10 most
	 * common words that precede "crash"
	 */
	public String[] mostCommonCollocsExc(int k, String baseWord, int i, String[] exclusions)
	{
		baseWord = normalize(baseWord);

		if (Math.abs(i) != 1)
			return null;

		String[] possibleWords = mostCommonCollocs(k + exclusions.length, baseWord, i);
		String[] words = new String[k];

		int position = 0;
		for (int index = 0; index < possibleWords.length; index++)
		{
			// temp must be used. It is not allowed to just put index inside the lambda shortcut because it is not final
			int temp = index;
			if (Arrays.stream(exclusions).noneMatch(foo -> foo.equals(possibleWords[temp])))
			{
				words[position++] = possibleWords[index];
				if (position == k)
					return words;
			}
		}

		return words;
	}

	/**
	 * @param k         number of words to return
	 * @param startWord first word to return
	 * @return a string composed of k words from startWord w2 w3 ... wk. The string is generated by finding w2, the
	 * most common word following the startWord, then w3, the most common word following w2, and so on. Each word
	 * should be separated by a single space
	 */
	public String generateWordString(int k, String startWord)
	{
		startWord = normalize(startWord);

		if (k < 1)
			return "";

		StringBuilder sb = new StringBuilder();
		boolean generateString = false;

		for (String str : mostCommonWords)
			if (generateString || str.equals(startWord))
			{
				generateString = true;
				sb.append(str).append(" ");
				if (--k == 0)
					return sb.substring(0, sb.length() - 1);
			}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * @return the HashTable.toString()
	 */
	@Override
	public String toString()
	{
		return wordsByNumInstances.toString();
	}

	/**
	 * Helper method for both methods to normalize parameters to:
	 * Convert words to lower case
	 * Removes punctuation and spaces
	 */
	private static String normalize(String str)
	{
		// Make String LowerCase
		str = str.toLowerCase();
		// Remove all Punctuation
		str = str.replaceAll("\\p{Punct}", "");
		// Remove Leading/Trailing Spaces
		//str = str.trim();
		// Add every word to the ArrayList
		return str.replaceAll(" ", "");
	}

	/**
	 * Demo
	 *
	 * @param args arguments
	 */
	public static void main(String[] args)
	{
		String[] words = new String[] {"The ", "duck ", "does ", "not ", "eat ", "the ", "bear.", " \\. duck ,; ",
				" du/ck", "duck", "bear"};
		WordStat wordStat1 = new WordStat(words);

		// Word Rank
		System.out.println("Word Rank:");
		System.out.println("Expected:\n2\nActual:\n" + wordStat1.wordRank("the"));
		System.out.println("Expected:\n1\nActual:\n" + wordStat1.wordRank("duck"));
		System.out.println("Expected:\n2\nActual:\n" + wordStat1.wordRank("bear"));
		System.out.println("Expected:\n4\nActual:\n" + wordStat1.wordRank("does"));
		System.out.println("Expected:\n0\nActual:\n" + wordStat1.wordRank("Word Not There"));

		// Word Pair Rank
		System.out.println("\nWord Pair Rank:");
		System.out.println("Expected:\n2\nActual:\n" + wordStat1.wordPairRank("duck", "duck"));
		System.out.println("Expected:\n1\nActual:\n" + wordStat1.wordPairRank("duck", "bear"));

		// Most Common Word
		System.out.println("\nMost Common Word:");
		System.out.println("Expected:\n[duck, bear, the, does, eat, not]\nActual:\n" +
				Arrays.toString(wordStat1.mostCommonWords(Integer.MAX_VALUE)));
		System.out.println("Expected:\n[duck, bear]\nActual:\n" + Arrays.toString(wordStat1.mostCommonWords(2)));

		// Least Common Word
		System.out.println("\nLeast Common Word:");
		System.out.println("Expected:\n0\nActual:\n" +
				Arrays.compare(wordStat1.mostCommonWords(Integer.MAX_VALUE),
						wordStat1.leastCommonWords(Integer.MAX_VALUE), Collections.reverseOrder()));

		// Most Common Word Pairs:
		System.out.println("\nMost Common Word Pairs:");
		System.out.println("Expected:\n[duck bear, duck the, duck does]\nActual:\n" +
				Arrays.toString(wordStat1.mostCommonWordPairs(3)));
		System.out.println("Expected:\n[does not, duck bear, duck does, not eat, the bear, the duck, null]\nActual:\n"
				+ Arrays.toString(wordStat1.mostCommonWordPairs(7)));
		System.out.println("Expected:\n[duck bear]\nActual:\n" + Arrays.toString(wordStat1.mostCommonWordPairs(1)));
		System.out.println("Expected:\n[]\nActual:\n" + Arrays.toString(wordStat1.mostCommonWordPairs(0)));

		// Most Common Collocs Exclusions
		System.out.println("\nMost Common Collocs:");
		System.out.println("Expected:\n[duck, bear]\nActual:\n" +
				Arrays.toString(wordStat1.mostCommonCollocsExc(2, "duck", 1, new String[] {"the"})));
		System.out.println("Expected:\n[duck, null]\nActual:\n" +
				Arrays.toString(wordStat1.mostCommonCollocsExc(2, "duck", -1, new String[] {"the"})));

		System.out.println("Expected:\n[duck, null]\nActual:\n" +
				Arrays.toString(wordStat1.mostCommonCollocsExc(2, "duck", -1, new String[] {})));
		System.out.println("Expected:\n[duck, bear, the, does, eat]\nActual:\n" +
				Arrays.toString(wordStat1.mostCommonCollocsExc(5, "duck", 1, new String[] {})));

		// Generate Word String
		System.out.println("\nGenerate Word String:");
		System.out.println("Expected:\n\nActual:\n" + wordStat1.generateWordString(0, "duck"));
		System.out.println("Expected:\nduck\nActual:\n" + wordStat1.generateWordString(1, "duck"));
		System.out.println("Expected:\nduck bear the\nActual:\n" + wordStat1.generateWordString(3, "duck"));
		System.out.println("Expected:\nduck bear the does eat not\nActual:\n"
				+ wordStat1.generateWordString(10, "duck"));

		// Constructor 2
		System.out.println("\nTest Constructor from file:");
		WordStat wordStat2 = new WordStat("foobar.txt");
		System.out.println("Expected:\n[foobar, bar, foo]\nActual:\n" + Arrays.toString(wordStat2.mostCommonWords(50)));

		System.out.println("\n");
		System.out.println(Arrays.toString(wordStat1.mostCommonWordPairs));
	}
}