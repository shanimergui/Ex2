package implementation;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.node_data;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import gameClient.util.Point3D;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph g;

    public DWGraph_Algo(){
        g= new DWGraph_DS();
    }
    @Override
    public void init(directed_weighted_graph g) {
        this.g = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return g;
    }

    @Override
    public directed_weighted_graph copy()
    {
        directed_weighted_graph _g = new DWGraph_DS(g);//We used the copy constructor DS that we built in the graph
        return _g;
    }

    int id=0;
    int  sccCount=0;
    HashMap<node_data,ArrayList<Integer>> data;
    Stack<node_data> stack;
    //tarjan

    @Override
    public boolean isConnected()
    {
        init_nodes();
          id=0;
        sccCount=0;
        data = new  HashMap<node_data,ArrayList<Integer>>();
        stack = new Stack<>();
        for(node_data i: this.g.getV()){
            data.put(i,new ArrayList<>());
            for(int j=0;j<3;j++){
                data.get(i).add(0);
            }
        }

        for(node_data i : this.g.getV()){
            if(data.get(i).get(1)==0){
                dfs(i);
            }
        }
        if(sccCount==1) return true;
        return false;
    }

    private void dfs(node_data at) {
        stack.push(at);
        data.get(at).set(2,1);
        id++;
        data.get(at).set(1,id);
        data.get(at).set(0,id);

        for(edge_data i:this.g.getE(at.getKey())){
            node_data to = this.g.getNode(i.getDest());
            if(data.get(to).get(1)==0) dfs(to);
            if(data.get(to).get(2)==1) data.get(at).set(0,Math.min(data.get(to).get(0),data.get(at).get(0)));
        }

        if(data.get(at).get(1)== data.get(at).get(0)){
            while(!stack.isEmpty()){
                node_data node = stack.pop();
                data.get(node).set(2,0);
                data.get(node).set(0,data.get(node).get(1));
                if(node.getKey()== at.getKey()){
                    break;
                }
            }
            sccCount++;
        }

    }

    @Override
    public double shortestPathDist(int src, int dest) {
        init_nodes();
        if(this.g.getNode(src)==null||this.g.getNode(dest)==null){
            return -1;
        }
        PriorityQueue<node_data> q = new PriorityQueue<node_data>(new Comparator<node_data>() //We used a queue and named it q
        {
            @Override
            public int compare(node_data o1, node_data o2) {// עושה השוואה בין כל שני נודים שניתן לו
                return -Double.compare(o1.getWeight(),o2.getWeight());
            }
        });
        //tag - visit 0 unvisit 1 visit
        //weight - weihted node
        node_data s=g.getNode(src); //We converted node data to int
        node_data d=g.getNode(dest); //We converted node data to int

        s.setWeight(0);
        q.add(s);
//
//         v1= src
//        v2 = dist , v3, v4 ...

        while(!q.isEmpty()){ //The dijkstra code
            node_data v1 = q.poll();// src =v1
            for(edge_data e : this.g.getE(v1.getKey())){ //It is the edge between e and src dist...
                node_data v2=this.g.getNode(e.getDest()); // dist = v2 ...
                double weight= v1.getWeight() + e.getWeight(); //We connected the weight
                if(weight<v2.getWeight()){
                    v2.setWeight(weight);

                    q.remove(v2);//We will erase it and add it to the queue
                    q.add(v2);
                }
            }

        }
        double anser=d.getWeight();
        if(anser==Double.POSITIVE_INFINITY){//We have no way to arrive to dust and that's why we will return -1
            return -1;
        }
        return anser;
    }

    private void init_nodes() { //We initiate each time from the beginning before every function
        for(node_data n:this.g.getV()){//Returning all the nodes
            n.setTag(0);
            n.setWeight(Double.POSITIVE_INFINITY);
            n.setInfo("");
        }
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        init_nodes();
        if(this.g.getNode(src)==null||this.g.getNode(dest)==null){
            return null;
        }
        PriorityQueue<node_data> q = new PriorityQueue<node_data>(new Comparator<node_data>() //We created a preference queue and named it q
        {
            @Override
            public int compare(node_data o1, node_data o2) {// עושה השוואה בין כל שני נודים שניתן לו
                return -Double.compare(o1.getWeight(),o2.getWeight());
            }
        });
        //tag - visit 0 unvisit 1 visit
        //weight - weihted node
        node_data s=g.getNode(src); //We converted int into node data
        node_data d=g.getNode(dest); // We converted int into node data

        s.setWeight(0);
        q.add(s);
//
//         v1= src
//        v2 = dist , v3, v4 ...

        while(!q.isEmpty()){ //The dijkstra code
            node_data v1 = q.poll();// src =v1
            for(edge_data e : this.g.getE(v1.getKey())){ //It is the edge between e and src dist...
                node_data v2=this.g.getNode(e.getDest()); // dist = v2 ...
                double weight= v1.getWeight() + e.getWeight(); // We connected the weight
                if(weight<v2.getWeight()){//Just if it is inferior to
                    v2.setWeight(weight);//Updating the weight
                    v2.setInfo(Integer.toString(v1.getKey()));//Updating the key that we arrived to in info
                    q.remove(v2);//Erasing amd connecting another time to the queue
                    q.add(v2);
                }
            }
        }
        double anser=d.getWeight();
        if(anser==Double.POSITIVE_INFINITY){//We have no way to arrive to dust and so we return -1
            return null;
        }
        List<node_data> ans =new ArrayList<>();//Returning list
        ans.add(d);//Starting from dist because it is the first number in reverse
        String start = d.getInfo();// dist father
        while(!start.equals("")){//Until start doesn't equall to null. If yes we reached the last number
            node_data t=g.getNode(Integer.parseInt(start));//We converted start to be int and int to be node data
            ans.add(t);//We entered/added t
            start= t.getInfo();//We asked for the old minimum father, like that we know that we are on the right path
        }
        Collections.reverse(ans);
        return ans;
    }


    @Override
    public boolean save(String file) {
        //{"src":10,"w":1.1761238717867548,"dest":0} - object json
        //{"pos":"35.18753053591606,32.10378225882353,0.0","id":0} - object json
        JsonObject graph = new JsonObject();//We created an object from grph type

        JsonArray edges = new JsonArray();//We created 2 arrays from type jsonArray
        JsonArray nodes = new JsonArray();//We created 2 arrays from type jsonArray

        for(node_data vertex: this.g.getV()){// We passed with for on the real graph
            JsonObject node=new JsonObject();// On each graph we created an object from node type
            if(vertex.getLocation()!=null) {
                node.addProperty("pos", vertex.getLocation().toString());//We put pos into node
            }else{
                node.addProperty("pos", "0.0,0.0,0.0");
            }
            node.addProperty("id",vertex.getKey());//We put id into node

            nodes.add(node);// We added to nodes

            for(edge_data edge:this.g.getE(vertex.getKey())){
               JsonObject edge_obj=new JsonObject();//In each graph we created an object from edge type
               edge_obj.addProperty("src",edge.getSrc());
               edge_obj.addProperty("w",edge.getWeight());
               edge_obj.addProperty("dest",edge.getDest());

               edges.add(edge_obj);//We added to edge_obj to edges
           }
        }


        graph.add("Edges",edges);//To the graph we added edges
        graph.add("Nodes",nodes);//To the graph we added nodes

        try {
            Gson gs=new Gson();
            File obj_file =new File(file);//We changed it into a document
            PrintWriter writer = new PrintWriter(obj_file);//We are declaring that we are turning into a document
            writer.write(gs.toJson(graph));//We took the object and added it to a json document with the help of gson
            writer.close();
        }  catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }



        return false;
    }

    @Override
    public boolean load(String file) { //Taking the text document and turning it into an object from graph type ;
        try {
            FileInputStream INPUT = new FileInputStream(file);
            JsonReader read = new JsonReader(new InputStreamReader(INPUT));
            JsonObject js_obj= JsonParser.parseReader(read).getAsJsonObject();

            directed_weighted_graph temp =new DWGraph_DS();
            //    //{"pos":"35.18753053591606,32.10378225882353,0.0","id":0} - object json
            for(JsonElement element: js_obj.getAsJsonArray("Nodes")){

                node_data n =new Node_Data(element.getAsJsonObject().get("id").getAsInt());
                String [] pos = element.getAsJsonObject().get("pos").getAsString().split(",");
                n.setLocation(new Point3D(Double.parseDouble(pos[0]),Double.parseDouble(pos[1]),Double.parseDouble(pos[2])));
                temp.addNode(n);

            }
            for(JsonElement element: js_obj.getAsJsonArray("Edges")){
                temp.connect(element.getAsJsonObject().get("src").getAsInt(),element.getAsJsonObject().get("dest").getAsInt(),element.getAsJsonObject().get("w").getAsDouble());
            }
            init(temp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

    public static void main(String[] args) {
        directed_weighted_graph g=new DWGraph_DS();
        g.addNode(new Node_Data(0));
        g.addNode(new Node_Data(1));
        g.addNode(new Node_Data(2));

        g.connect(0,1,100);
        g.connect(0,2,50);
        g.connect(2,1,30);
        g.connect(1,0,30);
        g.getNode(0).setLocation(new Point3D(500,70,0));
        dw_graph_algorithms algo=new DWGraph_Algo();
        algo.init(g);
        System.out.println(algo.isConnected());
//        algo.save("test");
//
//
//        algo.load("test");
//
//        directed_weighted_graph g1=algo.getGraph();
//        System.out.println(g1.nodeSize());
//        System.out.println(algo.shortestPathDist(0,1));
//        List<node_data> list= algo.shortestPath(0,1);
//        for (node_data i: list){
//            System.out.print(i.getKey()+" ");
//        }
    }
}
