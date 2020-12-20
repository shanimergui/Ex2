package implementation;

import api.edge_data;

public class Edge_Data implements edge_data {


private int src;
private double weight;
private String info;
private int dest;
private int tag;

public Edge_Data(int src , double weight, String info, int dest ,int tag)
{
   this.src = src;
   this.weight =weight;
   this.info = info;
   this.dest=dest;
   this.tag =tag;
}

public Edge_Data (int src ,int dest, double weight){
    this.src = src;
    this.weight=weight;
    this.dest = dest;

    this.info="";
    this.tag=0;
}

    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
    info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
    tag=t;
    }
}
