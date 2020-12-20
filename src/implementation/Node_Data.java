package implementation;

import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;

public class Node_Data implements node_data {

    private int key;
    private geo_location location;
    private double weight;
    private String info;
    private int tag;


    public Node_Data() {
        key =key++;
        geo_location location = new Point3D(0,0,0);
        double weight = 0.0;
        String info = "";
        int tag = 0;
    }
    public Node_Data(int i) {
        key = i;
        geo_location location = new Point3D(0,0,0);
        double weight = 0.0;
        String info = "";
        int tag = 0;
    }

    public Node_Data(int key, geo_location location, double weight, String info, int tag) {
        this.key = key;
        this.location = new Point3D((Point3D)location);
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {
        return location;
    }

    @Override
    public void setLocation(geo_location p) {
        location = p;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        weight = w;
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
        tag = t;
    }
}
