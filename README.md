# Shortest Path (Dijkstra) - Java

Implementation of Dijkstra’s Shortest Path Algorithm on a weighted directed graph, built from scratch using custom data structures such as
a hashtable in Java.

# Overview
This project demonstrates the use of graph data structures, priority queues, and algorithmic optimization to efficiently compute the shortest path between nodes.

- Language: Java  
- Algorithm: Dijkstra’s
- Data Structures: Custom Hashtable, Graph adjacency list, PriorityQueue  

# Features
- Generic graph implementation supporting any node or edge type  
- Finds the shortest path cost and node sequence between two vertices  
- Detects missing paths and invalid nodes  
- Includes comprehensive JUnit tests  

## ⚙️ Example Usage
```java
DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
graph.insertNode("A");
graph.insertNode("B");
graph.insertNode("C");
graph.insertEdge("A", "B", 5);
graph.insertEdge("B", "C", 3);
graph.insertEdge("A", "C", 10);

System.out.println(graph.shortestPathData("A", "C")); // [A, B, C]
System.out.println(graph.shortestPathCost("A", "C")); // 8.0
