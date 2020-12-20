package implementation;

import api.edge_data;
import api.edge_location;

public class Edge_Location implements edge_location {
    edge_data e;

    public Edge_Location (int src,int dest,double w){
        e=new Edge_Data(src,dest,w);
    }

    @Override
    public edge_data getEdge() {
        return e;
    }

    @Override
    public double getRatio() {
        return 0;
    }
}
