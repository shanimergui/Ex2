package gameClient;

import Server.Agent_Graph_Algo;
import Server.Game_Server_Ex2;
import api.*;
//import Server.DWGraph;
import implementation.DWGraph_Algo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ex2 implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private int scenario_num = 0;
	private int id = 0;

	public void setId(int id) {
		this.id = id;
	}

	public void setScenario_num(int scenario_num) {
		this.scenario_num = scenario_num;
	}

	public static void main(String[] a) {

		if(a.length==0) {
			EntryGui gui = new EntryGui();
			gui.print_entry();
		}else if(a.length==2){
			Ex2 ex2=new Ex2();
			ex2.setId(Integer.parseInt(a[0]));
			ex2.setScenario_num(Integer.parseInt(a[1]));
			Thread client = new Thread(ex2);
			client.start();

		}
	}
	@Override
	public void run() {
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
		game.login(id);
		String g = game.getGraph();
		String pks = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		init(game);
		
		game.startGame();
		_win.setTitle("Ex2 - Game");
		int ind= (int) (game.timeToEnd()/100);
		long dt=100;
		
		while(game.isRunning()) {
			moveAgents(game, gg);
			try {
				if(ind%1==0) {_win.repaint();}
				Thread.sleep(dt);
				ind++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			_win.frame.setTime(game.timeToEnd());
		}
		String res = game.toString();

		System.out.println(res);
		System.exit(0);
	}
	/** 
	 * Moves each of the robots along the edge, 
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgents(game_service game, directed_weighted_graph gg) {
		String lg = game.move();
		List<CL_Agent> log = Agent_Graph_Algo.getAgents(lg, gg);
		_ar.setAgents(log);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs =  Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);
		for(CL_Pokemon pokemon:ffs) { //pokemons
			System.out.println(pokemon.get_edge());
		}
		for(int i=0;i<log.size();i++) { //agents
			CL_Agent robot = log.get(i);
			int id = robot.getID();
			int dest = robot.getNextNode();
			int src = robot.getSrcNode();// agent
			double v = robot.getValue();
			if(dest==-1) {
				//dest = nextNode(gg, src);
				//init graph algorithms
				dw_graph_algorithms al = new DWGraph_Algo();
				al.init(gg);
				//choose pokemon cloosest
				double minDist=Double.MAX_VALUE;
				CL_Pokemon poke = null;
				for(CL_Pokemon pokemon:ffs){ //pokemons
					double dist = al.shortestPathDist(src,pokemon.get_edge().getSrc());
					if(dist<minDist){
					minDist = dist;
					poke = pokemon;// נשמור את הפוקימון אם הדרך הקצרה ביותר
					}
				}

				List<node_data> path_to_move =  al.shortestPath(src,poke.get_edge().getSrc());
				if(null==path_to_move ){
					game.chooseNextEdge(robot.getID(), poke.get_edge().getDest());
				}else{
					for(node_data node: path_to_move){
						game.chooseNextEdge(robot.getID(), node.getKey());
					}
					game.chooseNextEdge(robot.getID(), poke.get_edge().getDest());
				}
			}
		}
	}

	private void init(game_service game) {
		
		String g = game.getGraph();
		String fs = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Agent_Graph_Algo.json2Pokemons(fs));
		_win = new MyFrame(" Ex2");
		_win.update(_ar);
		_win.setSize(1000, 700);
	
		_win.show();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the fruits
			ArrayList<CL_Pokemon> cl_fs = Agent_Graph_Algo.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++) { Agent_Graph_Algo.updateEdge(cl_fs.get(a),gg);}
			for(int a = 0;a<rs;a++) {
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
				
				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		
	}
}
