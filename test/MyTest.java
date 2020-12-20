import api.*;
import implementation.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyTest {
    directed_weighted_graph graph;
    dw_graph_algorithms algo;
    @BeforeEach
    void before() {
        graph = new DWGraph_DS();
        algo = new DWGraph_Algo();
    }

    @Test
    void graph_algo_all() {
            for (int i = 0; i < 10; i++) {
                graph.addNode(new Node_Data(i));
            }
            graph.connect(0, 1, 0.0);
            graph.connect(0, 2, 0.0);
            graph.connect(0, 3, 0.0);
            graph.connect(0, 4, 0.0);
            graph.connect(0, 5, 0.0);
            graph.connect(0, 6, 0.0);
            graph.connect(0, 7, 0.0);
            graph.connect(0, 8, 0.0);
            graph.connect(0, 9, 0.0);
            graph.connect(1, 2, 0.0);
            graph.connect(2, 3, 0.0);
            graph.connect(3, 4, 0.0);
            graph.connect(4, 5, 0.0);
            graph.connect(5, 6, 0.0);
            graph.connect(6, 7, 0.0);
            graph.connect(7, 8, 0.0);
            graph.connect(8, 9, 0.0);
            graph.connect(9, 1, 0.0);
            assertEquals(graph.getMC(), 28 );
            assertEquals(graph.edgeSize(), 18 );
            assertEquals(graph.nodeSize(), 10);

            graph.removeEdge(9,1);
            assertEquals(graph.getMC(), 29 );
            assertEquals(graph.edgeSize(), 17 );
            assertEquals(graph.nodeSize(), 10);
            graph.removeNode(9);
            assertEquals(graph.getMC(), 32 );
            assertEquals(graph.edgeSize(), 15 );
            assertEquals(graph.nodeSize(), 9);

            graph.addNode(new Node_Data(9));
            graph.addNode(new Node_Data(9));
           assertEquals(graph.getMC(), 33);
           assertEquals(graph.edgeSize(), 15 );
          assertEquals(graph.nodeSize(), 10);

          directed_weighted_graph copy_g=new DWGraph_DS(graph);

        assertEquals(copy_g.getMC(), 25);
        assertEquals(copy_g.edgeSize(), 15 );
        assertEquals(copy_g.nodeSize(), 10);

        node_data n9=copy_g.getNode(9);

        assertNotEquals(n9,null);

        node_data n10=copy_g.getNode(10);

        assertEquals(n10,null);

        edge_data edge_0_9 = copy_g.getEdge(0,9);
        assertEquals(edge_0_9,null);

        edge_data edge_0_1 = copy_g.getEdge(5,6);
        assertNotEquals(edge_0_1,null);

        Collection<node_data> v= copy_g.getV();

        assertEquals(v.size(),10);

        Collection<edge_data> e= copy_g.getE(5);

        assertEquals(e.size(),1);
    }
    @Test
    void graph_ds_all() {
        for (int i = 0; i < 3; i++) {
            graph.addNode(new Node_Data(i));
        }
        graph.connect(0, 1, 2);
        graph.connect(1, 2, 2);

        algo.init(graph);

        assertFalse(algo.isConnected());
        graph.connect(2, 0, 6);
        assertTrue(algo.isConnected());
        graph.addNode(new Node_Data(3));
        graph.connect(1, 3, 5);
        graph.connect(2, 3, 2);
        algo.init(graph);
        assertEquals(algo.shortestPathDist(0,3),6);

        List<node_data> path = algo.shortestPath(0,3);

        assertEquals(path.size(),4);

        assertEquals(path.get(2).getKey(),2);

        algo.save("data\\test");

        algo.load("data\\test");
        algo.save("data\\test1");

        algo.load("data\\test1");


        assertEquals(algo.shortestPathDist(0,3),6);

        List<node_data> path1 = algo.shortestPath(0,3);

        assertEquals(path1.size(),4);

        assertEquals(path1.get(2).getKey(),2);
    }



}

