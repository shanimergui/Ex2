package implementation;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class DWGraph_DS implements directed_weighted_graph {

    private HashMap < Integer,node_data> nodes ;
    private HashMap < node_data,HashMap < Integer,edge_data>> edges ; //  for each node (object) - we will get the map of his neighbours

    private int mc;
    private int edgeSize;

    public DWGraph_DS (){
        nodes =new HashMap<>();
        edges = new HashMap<>();
        mc = 0;
        edgeSize= 0;

    }
    // copy constructor
    public DWGraph_DS(directed_weighted_graph g) {
        nodes =new HashMap<>();
        edges = new HashMap<>();

        for(node_data n :g.getV()){ //  we passed over all the nodes of g
           this.addNode(n);// We add all the g nodes to "nodes"
        }
        for(node_data n :g.getV()) // We pass over all the nodes of "nodes"
        {
            for(edge_data ni :g.getE(n.getKey())) // We run over the neighbours with getE
            {
                this.edges.put(n, new HashMap<Integer,edge_data>()); // Building the Hashmap
                this.connect(n.getKey(),ni.getDest(),ni.getWeight());// Entering all the edges
            }
        }

    }
    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if(getNode(src)== null || getNode(dest)== null){//Checking the existence of the nodes
            return null;
        }
        node_data src_node = getNode(src); // Converting the src number into an object
        HashMap<Integer, edge_data> neig_map = edges.get(src_node); // We find which are neig_map neighbours and we put them in the src variable
        if ( neig_map.containsKey(dest)) // Checking if src is neighbour to dist
            return neig_map.get(dest); // We return the edge that is connected to both of them if thy are neighbours
        else return null;

    }

    @Override
    public void addNode(node_data n) {
        if( !nodes.containsKey(n.getKey())){ //We are checking if the node already exists
            int key = n.getKey();
            nodes.put(key, n); // We add it to collect nodes from the graph
            HashMap< Integer,edge_data> map = new HashMap < Integer,edge_data>(); //We initiate the intern Hashmap
            edges.put(n,map); //We added the Hashmap edges into the Hashmap map
            mc++;
        }

    }

    @Override
    public void connect(int src, int dest, double w) {
        if(nodes.containsKey(src) && nodes.containsKey(dest) && w>=0 ){//We checked if the nodes already exist in the graph
            HashMap<Integer, edge_data> map_src = edges.get(nodes.get(src));//We are looking at the Hashmap of the neighbours of the src
            if(!map_src.containsKey(dest)) { //Checking if there are no edge between them that already exists
                edge_data edge = new Edge_Data(src,dest,w);//We built a new edge
                map_src.put(dest ,edge);//We put it into the src neighbour's Hashmap
                mc++;
                edgeSize++;
            }

        }
    }

    @Override
    public Collection<node_data> getV()
    {//We need to return an object collection that every object is from type node data
        return nodes.values();//Going into node data Hashmap and returning the nodes
    }

    @Override
    public Collection<edge_data> getE(int node_id) {//We wnat to return all the edges that are getting out from the edge that its key is node id
        node_data node = nodes.get(node_id); //Recieving the node
        if (node != null) {
            HashMap<Integer, edge_data> inner_map = edges.get(node); //Recieving the intern Hashmap
            return inner_map.values();//Returning the values of the intern Hashmap
//        return edges.get(nodes.get(node_id)).values();//
        }

        return null;
    }

    @Override
    public node_data removeNode(int key) {
        if(nodes.containsKey(key)){//Is there already an existing key
        Iterator <node_data> it = getV().iterator(); //To pass over the nodes collection
        while(it.hasNext()){ //Until there is a node
            node_data t=it.next(); //We enter it into T
            if(edges.get(t).containsKey(key)){//Is there between them an edge
                edges.get(t).remove(key);//Returning the edge in between them
                mc++;
                edgeSize--;
            }
        }

            mc+= edges.get(getNode(key)).size();
            edgeSize-=edges.get(getNode(key)).size();

        mc++;
        edges.remove(getNode(key));//Erasing the Hashmap
        return nodes.remove(key);//Erasing the node itself
        }

        return null;
    }


    @Override
    public edge_data removeEdge(int src, int dest) {
        if (getNode(src) == null || getNode(dest) == null) {//Checking if the edges already exist
            return null;
        }
        if(edges.get(getNode(src)).containsKey(dest)){//Is there an edge connected between the two nodes
        mc++;
        edgeSize--;
          return edges.get(getNode(src)).remove(dest)  ;//Erasing this edge
        }
      return null;
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int getMC() {
        return mc;
    }
}
