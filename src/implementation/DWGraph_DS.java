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

        for(node_data n :g.getV()){ //  עברנו על כל הקודקודים של g
           this.addNode(n);// מוסחפה את כל הקודקודים של g ל nodes
        }
        for(node_data n :g.getV()) // עוברים על כל הקודקודים של nodes
        {
            for(edge_data ni :g.getE(n.getKey())) // רצים על השכנים על ידי ה getE
            {
                this.edges.put(n, new HashMap<Integer,edge_data>()); // בונים את האשמאפ
                this.connect(n.getKey(),ni.getDest(),ni.getWeight());// מכניסה את כל הצלעות
            }
        }

    }
    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if(getNode(src)== null || getNode(dest)== null){// נבדוק האם הקודקודים בכלל קימיים
            return null;
        }
        node_data src_node = getNode(src); // ממירה רת המספר src לאובייקט
        HashMap<Integer, edge_data> neig_map = edges.get(src_node); // מוצאים מי השכנים של src ונשים אותם במשתנה neig_map
        if ( neig_map.containsKey(dest)) // נבדוק האם dist שכן של src
            return neig_map.get(dest); // נחזיר את הצלע שמקשרת ביניהם אם הם שכנים
        else return null;

    }

    @Override
    public void addNode(node_data n) {
        if( !nodes.containsKey(n.getKey())){ // בודק האם הקודקוד כבר קיים
            int key = n.getKey();
            nodes.put(key, n); // מוסיף אותו לאוסף קודקודים של הגרף
            HashMap< Integer,edge_data> map = new HashMap < Integer,edge_data>(); //  התחלמן את האשמאפ הפנימי
            edges.put(n,map); // הוספנו את האשמאפ map לתוך האשמאפ edges
            mc++;
        }

    }

    @Override
    public void connect(int src, int dest, double w) {
        if(nodes.containsKey(src) && nodes.containsKey(dest) && w>=0 ){// בדקנו שהקודקודים כבר קיימים בגרף
            HashMap<Integer, edge_data> map_src = edges.get(nodes.get(src));// נסתכל על האשמאפ של השכנים של הsrc
            if(!map_src.containsKey(dest)) { // נבדוק אם לא קיימת ביניהם כבר צלע
                edge_data edge = new Edge_Data(src,dest,w);//בנינו צלע חדשה
                map_src.put(dest ,edge);//הכנסנו אותה לאשמאפ של השכנים של src
                mc++;
                edgeSize++;
            }

        }
    }

    @Override
    public Collection<node_data> getV()
    {// נצטרך להחזיר אוסף של אובייקטים שכל אובייקט הוא מסוג node data
        return nodes.values();// הולכת להאשמאפ של nodes ומחיזרה את node data
    }

    @Override
    public Collection<edge_data> getE(int node_id) {// אנחנו רוצים להחזיר את כל הצלעות שיוצאות מקודקוד שהמפתח שלו זה node id
        node_data node = nodes.get(node_id); // מקבלת את הקודקוד
        if (node != null) {
            HashMap<Integer, edge_data> inner_map = edges.get(node); // מקבלת את האשמאפ הפנימי
            return inner_map.values();//מחזירה את הערכים של האשמאפ הפנימי
//        return edges.get(nodes.get(node_id)).values();//
        }

        return null;
    }

    @Override
    public node_data removeNode(int key) {
        if(nodes.containsKey(key)){// האם קיים מפתח
        Iterator <node_data> it = getV().iterator(); // כדי לעבור על אוסף הקודקודים
        while(it.hasNext()){ // כל עוד יש לי קודקוד
            node_data t=it.next(); // נכניס אותו ל T
            if(edges.get(t).containsKey(key)){// האם קיים ביניהם צלע
                edges.get(t).remove(key);// מחקנו את הצלע שבניהם
                mc++;
                edgeSize--;
            }
        }

            mc+= edges.get(getNode(key)).size();
            edgeSize-=edges.get(getNode(key)).size();

        mc++;
        edges.remove(getNode(key));// מחקנו את האשמאפ
        return nodes.remove(key);// מחקנו את הקודקוד עצמו
        }

        return null;
    }


    @Override
    public edge_data removeEdge(int src, int dest) {
        if (getNode(src) == null || getNode(dest) == null) {// נבדוק האם הקודקודים בכלל קימיים
            return null;
        }
        if(edges.get(getNode(src)).containsKey(dest)){//  האם יש צלע שמחוברת בין שני הקודקודים
        mc++;
        edgeSize--;
          return edges.get(getNode(src)).remove(dest)  ;// למחוק את הצלע הזאת
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
