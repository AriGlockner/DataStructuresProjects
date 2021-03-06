import org.junit.*;

import java.util.Arrays;

public class TestWeightedGraph
{
	/**
	 * Tests reading from a file. Also tests adding and removing
	 */
	@Test
	public void testFile()
	{
		WeightedGraph graph = WeightedGraph.readWeightedGraph("weightedgraph.txt");
		graph.printWeightedGraph();
		Assert.assertEquals("A 2 B 1 D\nB 3 D 10 E\nC 4 A 5 F\nD 2 C 2 E 8 F 4 G\nE 6 G\nF\nG 1 F", graph.toString());

		// Test before
		Assert.assertEquals("[A, D, F]", Arrays.toString(graph.BFS("A", "F", "alphabetical")));
		Assert.assertEquals("[A, D, F]", Arrays.toString(graph.BFS("A", "F", "reverse")));
		Assert.assertEquals("[A, B, D, C, F]", Arrays.toString(graph.DFS("A", "F", "alphabetical")));
		Assert.assertEquals("[A, D, G, F]", Arrays.toString(graph.DFS("A", "F", "reverse")));
		Assert.assertEquals("[A, D, G, F]", Arrays.toString(graph.shortestPath("A", "F")));
		Assert.assertEquals("[A, D, F]", Arrays.toString(graph.secondShortestPath("A", "F")));

		// Remove a node
		Assert.assertTrue(graph.removeNode("D"));

		// Test after removal - the only path from A to F is [A, B, E, G, F]
		Assert.assertEquals("[A, B, E, G, F]", Arrays.toString(graph.BFS("A", "F", "alphabetical")));
		Assert.assertEquals("[A, B, E, G, F]", Arrays.toString(graph.BFS("A", "F", "reverse")));
		Assert.assertEquals("[A, B, E, G, F]", Arrays.toString(graph.DFS("A", "F", "alphabetical")));
		Assert.assertEquals("[A, B, E, G, F]", Arrays.toString(graph.DFS("A", "F", "reverse")));
		Assert.assertEquals("[A, B, E, G, F]", Arrays.toString(graph.shortestPath("A", "F")));
		Assert.assertEquals("[]", Arrays.toString(graph.secondShortestPath("A", "F")));

		// Remove multiple nodes - no path exists from A to F
		Assert.assertTrue(graph.removeNodes(new String[] {"B", "E", "G"}));
		Assert.assertEquals("[]", Arrays.toString(graph.BFS("A", "F", "alphabetical")));
		Assert.assertEquals("[]", Arrays.toString(graph.BFS("A", "F", "reverse")));
		Assert.assertEquals("[]", Arrays.toString(graph.DFS("A", "F", "alphabetical")));
		Assert.assertEquals("[]", Arrays.toString(graph.DFS("A", "F", "reverse")));
		Assert.assertEquals("[]", Arrays.toString(graph.shortestPath("A", "F")));
		Assert.assertEquals("[]", Arrays.toString(graph.secondShortestPath("A", "F")));

		// Remove should fail
		Assert.assertFalse(graph.removeNode("E"));
		Assert.assertFalse(graph.removeNodes(new String[] {"B", "C", "F", "G", "A"}));

		// Add Nodes
		Assert.assertTrue(graph.addNodes(new String[] {"A", "B", "C", "D", "E", "F", "G"}));
		Assert.assertFalse(graph.addNodes(new String[] {"A", "B", "D", "E", "G"}));
		Assert.assertFalse(graph.addNode("B"));

		// Add Edges
		Assert.assertTrue(graph.addWeightedEdges("A", new String[] {"B", "D"}, new int[] {2, 1}));
		Assert.assertTrue(graph.addWeightedEdges("B", new String[] {"D", "E"}, new int[] {3, 10}));
		Assert.assertTrue(graph.addWeightedEdges("C", new String[] {"A", "F"}, new int[] {4, 5}));
		Assert.assertTrue(graph.addWeightedEdges("D", new String[] {"C", "E", "F", "G"}, new int[] {2, 2, 8, 4}));
		Assert.assertTrue(graph.addWeightedEdge("E", "G", 6));
		Assert.assertTrue(graph.addWeightedEdge("G", "F", 1));

		// Check after add
		Assert.assertEquals("[A, D, F]", Arrays.toString(graph.BFS("A", "F", "alphabetical")));
		Assert.assertEquals("[A, D, F]", Arrays.toString(graph.BFS("A", "F", "reverse")));
		Assert.assertEquals("[A, B, D, C, F]", Arrays.toString(graph.DFS("A", "F", "alphabetical")));
		Assert.assertEquals("[A, D, G, F]", Arrays.toString(graph.DFS("A", "F", "reverse")));
		Assert.assertEquals("[A, D, G, F]", Arrays.toString(graph.shortestPath("A", "F")));
		Assert.assertEquals("[A, D, F]", Arrays.toString(graph.secondShortestPath("A", "F")));
	}

	/**
	 * Extra credit portion of the assignment that tests a custom real world graph. This graph has a bunch of cities in
	 * the USA and time travel time at one point int time, in minutes, to get from one city to another connected city.
	 * Due to some cities that are being added having spaces in their names, the read method will not work for it, so
	 * they must be added manually through addNode/addNodes/addWeightedEdge/addWeightedEdges
	 */
	@Test
	public void testRealWorldExample()
	{
		// Due to some cities that are being added having spaces in their names, the read method will not work for it,
		// so they must be added manually through addNode/addNodes/addWeightedEdge/addWeightedEdges

		// Create Weighted Graph
		WeightedGraph graph = new WeightedGraph();

		// Add cities
		Assert.assertTrue(graph.addNodes(new String[] {"Los Angeles", "San Francisco", "Seattle", "Cleveland",
				"Chicago", "New York", "Washington DC", "Boston", "Miami", "Dallas", "Houston", "Denver", "Atlanta"}));

		// Add weighted edges
		Assert.assertTrue(graph.addWeightedEdges("Los Angeles",
				new String[] {"San Francisco", "Denver"},
				new int[] {323, 878}));
		Assert.assertTrue(graph.addWeightedEdges("San Francisco",
				new String[] {"Los Angeles", "Seattle"},
				new int[] {323, 755}));
		Assert.assertTrue(graph.addWeightedEdges("Seattle",
				new String[] {"San Francisco", "Denver"},
				new int[] {755, 1099}));
		Assert.assertTrue(graph.addWeightedEdges("Cleveland",
				new String[] {"Chicago", "New York", "Washington DC", "Atlanta"},
				new int[] {306, 419, 341, 598}));
		Assert.assertTrue(graph.addWeightedEdges("Chicago",
				new String[] {"Denver", "Dallas", "Cleveland", "Atlanta"},
				new int[] {826, 806, 306, 632}));
		Assert.assertTrue(graph.addWeightedEdges("New York",
				new String[] {"Washington DC", "Cleveland", "Boston"},
				new int[] {217, 419, 210}));
		Assert.assertTrue(graph.addWeightedEdges("Washington DC",
				new String[] {"Atlanta", "Cleveland", "New York"},
				new int[] {585, 341, 217}));
		Assert.assertTrue(graph.addWeightedEdge("Boston", "New York", 210)); // add weight
		Assert.assertTrue(graph.addWeightedEdge("Miami", "Atlanta", 549));
		Assert.assertTrue(graph.addWeightedEdges("Dallas",
				new String[] {"Denver", "Houston", "Chicago"},
				new int[] {696, 199, 806}));
		Assert.assertTrue(graph.addWeightedEdges("Houston",
				new String[] {"Dallas", "Atlanta"},
				new int[] {199, 665}));
		Assert.assertTrue(graph.addWeightedEdges("Denver",
				new String[] {"Seattle", "Los Angeles", "Dallas", "Chicago"},
				new int[] {1099, 878, 696, 826}));
		Assert.assertTrue(graph.addWeightedEdges("Atlanta",
				new String[] {"Miami", "Houston", "Washington DC", "Chicago", "Cleveland"},
				new int[] {549, 665, 585, 632, 598}));

		// Test print weighted graph / toString
		graph.printWeightedGraph();
		Assert.assertEquals("""
				Los Angeles 323 San Francisco 878 Denver
				San Francisco 323 Los Angeles 755 Seattle
				Seattle 755 San Francisco 1099 Denver
				Cleveland 306 Chicago 419 New York 341 Washington DC 598 Atlanta
				Chicago 826 Denver 806 Dallas 306 Cleveland 632 Atlanta
				New York 217 Washington DC 419 Cleveland 210 Boston
				Washington DC 585 Atlanta 341 Cleveland 217 New York
				Boston 210 New York
				Miami 549 Atlanta
				Dallas 696 Denver 199 Houston 806 Chicago
				Houston 199 Dallas 665 Atlanta
				Denver 1099 Seattle 878 Los Angeles 696 Dallas 826 Chicago
				Atlanta 549 Miami 665 Houston 585 Washington DC 632 Chicago 598 Cleveland""", graph.toString());


		// BFS Alphabetical
		Assert.assertEquals("[Seattle, Denver, Chicago, Atlanta, Miami]",
				Arrays.toString(graph.BFS("Seattle", "Miami", "alphabetical")));
		Assert.assertEquals("[Boston, New York, Cleveland, Chicago, Denver, Los Angeles]",
				Arrays.toString(graph.BFS("Boston", "Los Angeles", "alphabetical")));
		Assert.assertEquals("[San Francisco, Los Angeles, Denver, Chicago, Atlanta, Washington DC]",
				Arrays.toString(graph.BFS("San Francisco", "Washington DC", "alphabetical")));
		Assert.assertEquals("[Cleveland, Atlanta, Houston]",
				Arrays.toString(graph.BFS("Cleveland", "Houston", "alphabetical")));
		Assert.assertEquals("[Denver, Chicago, Atlanta]",
				Arrays.toString(graph.BFS("Denver", "Atlanta", "alphabetical")));

		// BFS Reverse Alphabetical
		Assert.assertEquals("[Seattle, Denver, Chicago, Atlanta, Miami]",
				Arrays.toString(graph.BFS("Seattle", "Miami", "reverse")));
		Assert.assertEquals("[Boston, New York, Cleveland, Chicago, Denver, Los Angeles]",
				Arrays.toString(graph.BFS("Boston", "Los Angeles", "reverse")));
		Assert.assertEquals("[San Francisco, Seattle, Denver, Chicago, Cleveland, Washington DC]",
				Arrays.toString(graph.BFS("San Francisco", "Washington DC", "reverse")));
		Assert.assertEquals("[Cleveland, Atlanta, Houston]",
				Arrays.toString(graph.BFS("Cleveland", "Houston", "reverse")));
		Assert.assertEquals("[Denver, Chicago, Atlanta]",
				Arrays.toString(graph.BFS("Denver", "Atlanta", "reverse")));

		// DFS Alphabetical
		Assert.assertEquals("[Seattle, Denver, Chicago, Atlanta, Miami]",
				Arrays.toString(graph.DFS("Seattle", "Miami", "alphabetical")));
		Assert.assertEquals("[Boston, New York, Cleveland, Atlanta, Chicago, Dallas, Denver, Los Angeles]",
				Arrays.toString(graph.DFS("Boston", "Los Angeles", "alphabetical")));
		Assert.assertEquals("[San Francisco, Los Angeles, Denver, Chicago, Atlanta, Cleveland, New York, Washington DC]",
				Arrays.toString(graph.DFS("San Francisco", "Washington DC", "alphabetical")));
		Assert.assertEquals("[Cleveland, Atlanta, Chicago, Dallas, Houston]",
				Arrays.toString(graph.DFS("Cleveland", "Houston", "alphabetical")));
		Assert.assertEquals("[Denver, Chicago, Atlanta]",
				Arrays.toString(graph.DFS("Denver", "Atlanta", "alphabetical")));

		// DFS Reverse Alphabetical
		Assert.assertEquals("[Seattle, San Francisco, Los Angeles, Denver, Dallas, Houston, Atlanta, Miami]",
				Arrays.toString(graph.DFS("Seattle", "Miami", "reverse")));
		Assert.assertEquals("[Boston, New York, Washington DC, Cleveland, Chicago, Denver, Seattle, San Francisco, Los Angeles]",
				Arrays.toString(graph.DFS("Boston", "Los Angeles", "reverse")));
		Assert.assertEquals("[San Francisco, Seattle, Denver, Dallas, Houston, Atlanta, Washington DC]",
				Arrays.toString(graph.DFS("San Francisco", "Washington DC", "reverse")));
		Assert.assertEquals("[Cleveland, Washington DC, Atlanta, Houston]",
				Arrays.toString(graph.DFS("Cleveland", "Houston", "reverse")));
		Assert.assertEquals("[Denver, Dallas, Houston, Atlanta]",
				Arrays.toString(graph.DFS("Denver", "Atlanta", "reverse")));

		// Shortest Path
		Assert.assertEquals("[Seattle, Denver, Chicago, Atlanta, Miami]",
				Arrays.toString(graph.shortestPath("Seattle", "Miami")));
		Assert.assertEquals("[Boston, New York, Cleveland, Chicago, Denver, Los Angeles]",
				Arrays.toString(graph.shortestPath("Boston", "Los Angeles")));
		Assert.assertEquals("[San Francisco, Los Angeles, Denver, Chicago, Cleveland, Washington DC]",
				Arrays.toString(graph.shortestPath("San Francisco", "Washington DC")));
		Assert.assertEquals("[Cleveland, Atlanta, Houston]",
				Arrays.toString(graph.shortestPath("Cleveland", "Houston")));
		Assert.assertEquals("[Denver, Chicago, Atlanta]",
				Arrays.toString(graph.shortestPath("Denver", "Atlanta")));

		// 2nd Shortest Path
		Assert.assertEquals("[Seattle, Denver, Chicago, Cleveland, Atlanta, Miami]",
				Arrays.toString(graph.secondShortestPath("Seattle", "Miami")));
		Assert.assertEquals("[Boston, New York, Cleveland, Chicago, Denver, Seattle, San Francisco, Los Angeles]",
				Arrays.toString(graph.secondShortestPath("Boston", "Los Angeles")));
		Assert.assertEquals("[San Francisco, Los Angeles, Denver, Chicago, Cleveland, Atlanta, Washington DC]",
				Arrays.toString(graph.secondShortestPath("San Francisco", "Washington DC")));
		Assert.assertEquals("[Cleveland, Atlanta, Chicago, Dallas, Houston]",
				Arrays.toString(graph.secondShortestPath("Cleveland", "Houston")));
		Assert.assertEquals("[Denver, Chicago, Cleveland, Atlanta]",
				Arrays.toString(graph.secondShortestPath("Denver", "Atlanta")));
	}
}
