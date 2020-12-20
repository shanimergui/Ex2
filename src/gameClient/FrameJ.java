package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FrameJ extends JPanel {

    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private Image pok;
    private Image agent;
    private Image backgraund;
    private Image node;
    public long timegame=0;

    public void setTime(long t){
        this.timegame=t;
    }
    public FrameJ (){
        super();

        pok = new ImageIcon("data//image//pokemov.png").getImage();
        backgraund =  new ImageIcon("data//image//bg.jpg").getImage();
        agent = new ImageIcon("data//image//agent.png").getImage();
        node =  new ImageIcon("data//image//node.png").getImage();
    }
    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }
    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }
    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        g.drawImage(backgraund ,0, 0, w, h,this);
        updateFrame();
        drawGraph(g);
        drawPokemons(g);
        drawAgents(g);


        long seconds = TimeUnit.MILLISECONDS.toSeconds(this.timegame);
        g.drawString("Time: "+seconds,50,50);
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n,10,g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.black);
                drawEdge(e, g);
            }
        }
    }
    private void drawPokemons(Graphics g) {
        java.util.List<CL_Pokemon> fs = _ar.getPokemons();
        if(fs!=null) {
            Iterator<CL_Pokemon> itr = fs.iterator();
            while(itr.hasNext()) {
                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r=20;
                if(c!=null) {
                    geo_location fp = this._w2f.world2frame(c);
                    g.drawImage(pok,(int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r,null);
                }
            }
        }
    }
    private void drawAgents(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        int i=0;
        while(rs!=null && i<rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r=20;
            String s="V: "+rs.get(i).getValue();
            i++;
            if(c!=null) {
                g.setColor(Color.red);
                Font font = new Font("Courier New", Font.BOLD, 20);
                g.setFont(font);
                geo_location fp = this._w2f.world2frame(c);
                g.drawImage(agent,(int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r,this);
                g.drawString(s, (int)fp.x(), (int)fp.y()-4*r);
            }
        }
    }
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.drawImage(node,(int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r,this);
        Font font = new Font("Courier New", Font.BOLD, 20);
        g.setFont(font);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);

    }
    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
    }
}
