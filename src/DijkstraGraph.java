// Name: Usman Mohammed
// Email: <umohammed2@wisc.edu>

import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     * <p>
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new PlaceholderMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // If there's no start or end then a path can't be made
        if (start == null || end == null) {
            throw new NoSuchElementException();
        }
        // Creates map to hold the nodes in the path
        PlaceholderMap<NodeType, SearchNode> map = new PlaceholderMap();
        // Creates queue to hold the search nodes
        PriorityQueue<SearchNode> queue = new PriorityQueue();
        // Creates node for starting node on path
        SearchNode startNode = new SearchNode(nodes.get(start), 0, null);
        // Adds starting node to queue
        queue.add(startNode);

        while (!queue.isEmpty()) {
            // Returns and removes head of queue
            SearchNode currNode = queue.poll();
            // If current node is already in the map, then we continue on to next node
            if (map.containsKey(currNode.node.data)) {
                continue;
            }
            // Adds current node to the map
            map.put(currNode.node.data, currNode);
            // If current node is the end node, its returned
            if (currNode.node.data.equals(end)) {
                return currNode;
            }

            for (Edge newEdge : currNode.node.edgesLeaving) {
                // Creates variable for the successor of current node
                Node successor = newEdge.successor;
                if (!map.containsKey(successor.data)) {
                    // If successor isn't already in map, we update the cost to get to the new node
                    double newCost = currNode.cost + newEdge.data.doubleValue();
                    // We then add to new node to the queue
                    queue.add(new SearchNode(successor, newCost, currNode));
                }
            }

        }
        // If all else fails, there's no path
        throw new NoSuchElementException("No path found");
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // Creates variable for last node in path
        SearchNode lastNode = computeShortestPath(start, end);
        // Creates linked list to hold the shortest path in map
        LinkedList<NodeType> shortestPath = new LinkedList<>();

        while (lastNode != null) {
            // If the last node isn't null, we add it to the list
            shortestPath.addFirst(lastNode.node.data);
            // Update lastNode to be the predecessor so we can get through entire path
            lastNode = lastNode.predecessor;
        }
        // Return the list that holds the shortest path
        return shortestPath;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        // Gets the cost for the shortest path through the computeShortestPath() method
        return computeShortestPath(start, end).cost;
    }


    @Test
    public void test1() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertEdge("A", "B", 5);
        graph.insertEdge("B", "C", 3);
        graph.insertEdge("A", "C", 10);

        assertEquals(List.of("A", "B", "C"), graph.shortestPathData("A", "C"));
        assertEquals(8, graph.shortestPathCost("A", "C"), 0.001);
    }

    @Test
    public void test2() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        try {
            // This should throw a NoSuchElementException
            graph.shortestPathData("A", "B");
        }catch(NoSuchElementException e){
            System.out.println("Throws NoSuchElementException as expected");
        }
    }

    @Test
    public void test3() {
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        graph.insertNode("X");
        graph.insertNode("Y");
        graph.insertNode("Z");
        graph.insertEdge("X", "Y", 2.0);
        graph.insertEdge("Y", "Z", 4.0);
        graph.insertEdge("X", "Z", 10.0);

        assertEquals(List.of("X", "Y", "Z"), graph.shortestPathData("X", "Z"));
        assertEquals(6.0, graph.shortestPathCost("X", "Z"), 0.001);
    }

}