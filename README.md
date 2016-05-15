# ShortestPaths

To find Shortest path to all the nodes of a graph from a given source node using different methods depending upon the type of graph input:
* Breadth-First-Search(BFS) - If the graph has equal weight edges
* Topological Ordering(DAG - Directed Acyclic Graph) - If there are no cycles in the graph and weights can be either negative or positive
* Dijkstra's algorithm - If the graph has positive weights and there can be positive cycles.
* Bellman Ford algorithm - If all the above method fails, this can be used.

If there is any negative weight cycle, then none of the above methods can be used.

Table showing runtimes of different algorithms:

No. of Vertices|No. of Edges|Runtime(in ms)|Algorithm    |
---------------|:----------:|:------------:|------------ |
100000	       |  999896	  |   11	       |    DAG      |
10000	         |  796759	  |   131	       | Bellman Ford|
10000	         |  796853	  |   90	       |  Dijkstra   |
10000	         |  796772	  |   51	       |    BFS      |
