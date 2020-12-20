# Ex2

## Authors:
 Shani Mergui
 
![alt text](https://www.pngitem.com/pimgs/m/117-1172768_pokemon-hd-png-download.png)

## About The Project:

The project's subject is about a weighted and directed graph. The source is pointing towards the destination. In between them, on the edgde there is the weight.

## The 2 Parts:

This project is composed of 2 different parts : 

1) In the first part, we have api that is a collection of algorithms on a directed weighted graph.
Api will help us developping the nodes, graph, edges and the connections.
In this part we are also interested in finding the shortest path with api.

2)  In the second part, we are using the information contained in the first part such as a directed and weighted graph to create the game. 
This part is a game client where we enter information such as levels and id, we also inserted/created : the pokemon images, created the move of catching Pokemon and putting them on the graph, created the gui of the graph display.

## The Algorithms

In this project we used 2 different algorithms:

1) Tarjan is to check the bond tying.
2) Dijkstra is to find and to return the shortest path among the graph.

We used functions : shortestpathDist returns a value and shortestpath returns the detailed path.

Tarjan algorithm:
The nodes are placed on a pile in the order of their visit.
When the first depth search recursively visits the v node and its descendants, not all of these nodes are necessarily sucked out of the pile when this recursive call returns. 
The essential unchanging characteristic is that a node remains in a stack after its visit if and only if there is a path in the input graph from it to some node earlier in the stack. 
In other words, it means that in DFS a node is only removed from the stack after all its connected paths have been crossed. When the DFS goes back it will remove the nodes in a single path and return to the root to start a new path.

Dijkstra algorithm:

It gets 2 nodes- src and dest : go from the src node to the destination node with the lowest weight.

We initialize all the nodes' weight to infinity to understand which node was not updated yet. After that we are creating a priority queue q that is responsible to contain the nodes at which we arrive, update their weights. 
In addition, we created the parentNodes shamp that will eventually contain the updated nodes through which we passed the shortest trajectory in the graph from the vertex src and dest. We enter the first node and initialize its weight to 0, and all the other nodes in the graph are initialized to infinity. 
The current junction will include all of its neighbors and will update its temporary weights. 
The weight of each node is updated according to the parent weight of that node plus the temporary distance between them which is the weight at the end. Then the same junction we started with becomes the father of this junction and leaves the queue, it is already marked that we have already visited it and we will not return to it again. 
Each of the neighbors presented treats him in the same way: Put his neighbors in line and update their weights. 
Each node can have several neighbors and then also some fathers through which they come, so if one of the neighbors is already updated in weight because we reached it through another parent node, we will check through which neighbor it will be the lowest weight node, then we will keep the lower weight. We will remove the node from the queue and return it with the updated weight. 

## Source:

* https://www.youtube.com/watch?v=pVfj6mxhdMw&t=371s 

* https://www.youtube.com/watch?v=TyWtx7q2D7Y.
