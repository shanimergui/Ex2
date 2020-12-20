package gameClient;
import api.*;
import gameClient.util.*;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame{

	private Arena _ar;
	private gameClient.util.Range2Range _w2f;
	public FrameJ frame;

	MyFrame(String s) {
		super(s);
	}
	public void update(Arena ar) {// בארנה יש את כל התכונות שאנחנו צריכים שיהיו
		this._ar = ar;
		frame=new FrameJ();
		this.add(frame);
		updateFrame();
	}


	private void updateFrame() { // מגדיננה את ה fram באמצעות ארנה ובאמצעות רוחב ואורך
		Range rx = new Range(20,this.getWidth()-20);
		Range ry = new Range(this.getHeight()-10,150);
		Range2D f = new Range2D(rx,ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,f);
		frame.update(_ar);
		frame.repaint();
	}
	public void paint(Graphics g) {
		updateFrame();
	}
}
