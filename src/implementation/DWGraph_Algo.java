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
        directed_weighted_graph _g = new DWGraph_DS(g);// השתמשנו בבנאי מעתיק שיצרנו בגרף DS
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
        PriorityQueue<node_data> q = new PriorityQueue<node_data>(new Comparator<node_data>() // יצרנו טור עדיפויות וקראנו לו q
        {
            @Override
            public int compare(node_data o1, node_data o2) {// עושה השוואה בין כל שני נודים שניתן לו
                return -Double.compare(o1.getWeight(),o2.getWeight());
            }
        });
        //tag - visit 0 unvisit 1 visit
        //weight - weihted node
        node_data s=g.getNode(src); // עשינו המרה ל node data מ int
        node_data d=g.getNode(dest); // עשינו המרה ל node data מ int

        s.setWeight(0);
        q.add(s);
//
//         v1= src
//        v2 = dist , v3, v4 ...

        while(!q.isEmpty()){ // הקוש של דיאקסטרה
            node_data v1 = q.poll();// src =v1
            for(edge_data e : this.g.getE(v1.getKey())){ // e זה הצלע בין src dist והלאה
                node_data v2=this.g.getNode(e.getDest()); // dist = v2 והלאה
                double weight= v1.getWeight() + e.getWeight(); // חיברנו את המשקל
                if(weight<v2.getWeight()){
                    v2.setWeight(weight);

                    q.remove(v2);// נחמוק ונסיף מחדש לתור
                    q.add(v2);
                }
            }

        }
        double anser=d.getWeight();
        if(anser==Double.POSITIVE_INFINITY){// אין לי דרך להגיע ל dust ולכן נחזיר -1
            return -1;
        }
        return anser;
    }

    private void init_nodes() { // נאתחל כל פעם מחדש לפני כל פונקציה
        for(node_data n:this.g.getV()){// יתן לי את כל הקודקודים
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
        PriorityQueue<node_data> q = new PriorityQueue<node_data>(new Comparator<node_data>() // יצרנו טור עדיפויות וקראנו לו q
        {
            @Override
            public int compare(node_data o1, node_data o2) {// עושה השוואה בין כל שני נודים שניתן לו
                return -Double.compare(o1.getWeight(),o2.getWeight());
            }
        });
        //tag - visit 0 unvisit 1 visit
        //weight - weihted node
        node_data s=g.getNode(src); // עשינו המרה ל node data מ int
        node_data d=g.getNode(dest); // עשינו המרה ל node data מ int

        s.setWeight(0);
        q.add(s);
//
//         v1= src
//        v2 = dist , v3, v4 ...

        while(!q.isEmpty()){ // הקוש של דיאקסטרה
            node_data v1 = q.poll();// src =v1
            for(edge_data e : this.g.getE(v1.getKey())){ // e זה הצלע בין src dist והלאה
                node_data v2=this.g.getNode(e.getDest()); // dist = v2 והלאה
                double weight= v1.getWeight() + e.getWeight(); // חיברנו את המשקל
                if(weight<v2.getWeight()){// רק אם זה יותר קטן
                    v2.setWeight(weight);// נעדכן את המשקל
                    v2.setInfo(Integer.toString(v1.getKey()));//נעדכן את המפתח שממנו הגעתי ב info
                    q.remove(v2);// נחמוק ונסיף מחדש לתור
                    q.add(v2);
                }
            }
        }
        double anser=d.getWeight();
        if(anser==Double.POSITIVE_INFINITY){// אין לי דרך להגיע ל dust ולכן נחזיר -1
            return null;
        }
        List<node_data> ans =new ArrayList<>();// הגדרנו רשימה
        ans.add(d);// נתחיל מdist כי זה המספר הראשון ברבריס
        String start = d.getInfo();// האבא של dist
        while(!start.equals("")){//כל עוד start לא שווה לריק. אם כן שווה לריק אז הגענו למפתח האחרון
            node_data t=g.getNode(Integer.parseInt(start));// המרנו את start להיות int והמרנו את int להיות node datd
            ans.add(t);// הכנסנו \ הוספנו את t
            start= t.getInfo();// ביקשנו את האבא הקודם שלי המינימלי ביותר, וככה אנחנו יודעים שאנחנו במסלול הנכון
        }
        Collections.reverse(ans);
        return ans;
    }


    @Override
    public boolean save(String file) {
        //{"src":10,"w":1.1761238717867548,"dest":0} - object json
        //{"pos":"35.18753053591606,32.10378225882353,0.0","id":0} - object json
        JsonObject graph = new JsonObject();// יצרנו אובייקט מסוג גרף

        JsonArray edges = new JsonArray();// יצרנו שני מערכים מסוג jsonArray
        JsonArray nodes = new JsonArray();//יצרנו שני מערכים מסוג jsonArray

        for(node_data vertex: this.g.getV()){// עברנו בפור על הכרף האמיתי
            JsonObject node=new JsonObject();// בכל גרף יצרנו אובייקט מסוג node
            if(vertex.getLocation()!=null) {
                node.addProperty("pos", vertex.getLocation().toString());// בתוך node שמנו את pos
            }else{
                node.addProperty("pos", "0.0,0.0,0.0");
            }
            node.addProperty("id",vertex.getKey());// בתוך node שמנו את id

            nodes.add(node);// הוספנו ל nodes

            for(edge_data edge:this.g.getE(vertex.getKey())){
               JsonObject edge_obj=new JsonObject();// בכל גרף יצרנו אובייקט מסוג edge
               edge_obj.addProperty("src",edge.getSrc());
               edge_obj.addProperty("w",edge.getWeight());
               edge_obj.addProperty("dest",edge.getDest());

               edges.add(edge_obj);// הוספנו לedges את edge_obj
           }
        }


        graph.add("Edges",edges);// בסוף את edges הספנו לגרף
        graph.add("Nodes",nodes);// בסוף את nodes הוספנו לגרף

        try {
            Gson gs=new Gson();
            File obj_file =new File(file);// הפכנו אותו לקובץ
            PrintWriter writer = new PrintWriter(obj_file);// אנחנו מצהירים שאנחנו הולכים לרשום לקובץ
            writer.write(gs.toJson(graph));// רלקחנו את האובייקט והפכנו אותו באמצעות gson  לקובץ json
            writer.close();
        }  catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }



        return false;
    }

    @Override
    public boolean load(String file) { // לוקחים קובץ טקסק והופכים אותו לאובייקט מסוג גרף;
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
