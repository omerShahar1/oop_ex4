package gameClient;

import Server.Game_Server_Ex2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameClient.gui.ourFrame;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.util.*;

public class Ex2 implements Runnable {
    private static ourFrame Frame;
    private static Arena ManageGame;
    private static long playerID;
    private static int num_level;
    private static directed_weighted_graph graph;
    private static final dw_graph_algorithms graph_algo = new DWGraph_Algo();
    static long dt;
    static int movesCounter = 0;
    static HashMap<Integer, Integer> attack;

    public static void main(String[] a) {
        try {
            playerID = Long.parseLong(a[0]);
            num_level = Integer.parseInt(a[1]);
        } catch (Exception e) {
            playerID = -1;
            num_level = Integer.MAX_VALUE;
        }
        Thread client = new Thread(new Ex2());
        client.run();
    }

    /**
     * a login system, gets the input of ID and game level from the user and set it to the server.
     * is something is wrong, it shows an error and send the player to the default game level which is 0.
     */
    private static void login() {
        if ((playerID == -1 && num_level == Integer.MAX_VALUE) || (Game_Server_Ex2.getServer(num_level) == null)) {
            try {
                String id = JOptionPane.showInputDialog("Enter your ID", "Your ID");
                String level = JOptionPane.showInputDialog("Enter level number", "Insert a scenario ");
                playerID = Long.parseLong(id);
                num_level = Integer.parseInt(level);
                if (Game_Server_Ex2.getServer(num_level) == null) {
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(new ourFrame(), "Invalid input.\nPlease enter a scenario that exists in our server." +
                                "\nplaying a default mode: scenario number 0.", "Error!",
                        JOptionPane.ERROR_MESSAGE);
                num_level = 0;
            }
        }
    }


    /**
     * runs the game, gets the data of this game level from the server, init the game and the GUI frame and runs till the
     * time of the game is end.
     * in each dt time, moveAgents gets a status and update from the server and update the data in each right section.
     */
    @Override
    public void run() {
        login();
        game_service game = Game_Server_Ex2.getServer(num_level); // you have [0,23] games
        game.login(playerID);
        loadGraph(game.getGraph());
        init(game);
        game.startGame();
        Frame.setTitle("Ex2 - OOP: Pokemons! ,  Game Number: " + num_level);
        while (game.isRunning()) {
            movesCounter++;
            dt = 100;
            moveAgents(game);
            try {
                Frame.getPanel().setMoves(movesCounter);
                Frame.repaint();
                Thread.sleep(dt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    /**
     * loads the graph from the server (str = JSON string).
     *
     * @param str a String type.
     */
    private void loadGraph(String str) {
        try {
            GsonBuilder builder = new GsonBuilder()
                    .registerTypeAdapter(directed_weighted_graph.class, new graphDeserialization());
            Gson gson = builder.create();
            graph = gson.fromJson(str, directed_weighted_graph.class);
        } catch (Exception f) {
            f.printStackTrace();
        }
    }

    /**
     * init the game in the first run.
     * gets the Pokemons and Agents from the server, place the Agents in the Arena.
     * and init the GUI system as well.
     * the Agents are placed near the most evaluated node, which is where the more evaluated Pokemons are (a strategy).
     *
     * @param game a game_service type.
     */
    private void init(game_service game) {
        attack = new HashMap<>();
        String fs = game.getPokemons();
        ManageGame = new Arena();
        ManageGame.setGraph(graph);
        ManageGame.setPokemons(Arena.getPokemons(fs));
        ManageGame.setGame(game);
        /// frame init
        Frame = new ourFrame();
        Frame.setSize(1000, 700);
        Frame.initFrame(ManageGame);
        Frame.setVisible(true);
        String info = game.toString();
        try {
            // info about the game scenario
            JSONObject line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int num_of_agents = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            Arena.getPokemons(game.getPokemons());
            LinkedList<Integer> strongest = ManageGame.getBestValue();
            for (int a = 0; a < num_of_agents; a++) {
                boolean flag = false;
                for (Integer key : strongest) {
                    if (!flag) {
                        LinkedList<Pokemon> pokemonsOnEdge = ManageGame.getPokemonsOnEdge(key);
                        Pokemon p = pokemonsOnEdge.getFirst();
                        int pos_on_graph = key;
                        if (pokemonsOnEdge.getFirst().getType() < 0)
                            pos_on_graph = p.getEdges().getSrc();
                        if (!attack.containsValue(pos_on_graph)) {
                            flag = true;
                            attack.put(a, pos_on_graph);
                            game.addAgent(pos_on_graph);
                        }
                    }
                }
            }
            for (int a = 0; a < num_of_agents; a++) {
                if(!attack.containsKey(a)){
                    for(node_data node : graph.getV()) {
                        if(!attack.containsValue(node.getKey())) {
                            attack.put(a,node.getKey());
                            game.addAgent(node.getKey());
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * import the Agents and their status from the game server and update the Agents in the Arena.
     * and by some parameters, it choose where the Agents needs to go to their next move.
     *
     * @param game a game_service type.
     */
    private static void moveAgents(game_service game) {
        String lg = game.move();
        List<Agent> log = Arena.getAgents(lg, graph);
        ManageGame.setAgents(log);
        String fs = game.getPokemons();
        List<Pokemon> ffs = Arena.getPokemons(fs);
        ManageGame.setPokemons(ffs);
        graph_algo.init(graph);
        int grade = 0;
        for (Agent ag : log) {
            int id = ag.getID();
            double v = ag.getPoints();
            grade += (int) v;
            int dest = ag.getNextNode();
            if (dest == -1) {
                if (ag.getSrcNode() == attack.get(ag.getID())) {
                    attack.put(ag.getID(), -1);
                }
                    int nextNode = nextNode(ag, ffs);
                    game.chooseNextEdge(ag.getID(), nextNode);
                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + nextNode);
            }
        }
        Frame.getPanel().setGrade(grade);
    }
    /**
     * calculating the closest Pokemon to this Agent (bond).
     * It iterate all the Pokemons in the game, if a Pokemon isn't targeted by some other Agent,
     * So by Dijkstra Algorithm (shortestPathDist) it calculate each available Pokemon's distance from this Pokemon to this Agent.
     * for each calculate, the pokemon is sent to a PriorityQueue of a priority of the closest Pokemon to this Agent.
     * After the whole calculation, the PriorityQueue polls the closest Pokemon to this Agent.
     * When its done, it calculates by Dijkstra (shortestPath) the shortest path between this Agent to its closest Pokemon and return
     * A list of the path, this method will return the first node (next move) of this path to the Pokemon,
     * and so on in each iteration till the Agent catch it.
     * This method is also fix the "stuckProblem" of the Agents when it gets to speed 5, so if an Agent is standing
     * on the same edge more than 8 times in a row it means it stuck because of the FPS, so the dt is changed to 30 for a while.
     *
     * @param bond     an Agent.
     * @param pokemons a List of the Pokemons in the game.
     * @return the key of a node to the next move of the Agent.
     */
    private static int nextNode(Agent bond, List<Pokemon> pokemons) {
        PriorityQueue<Pokemon> closest = new PriorityQueue<>(new ComparatorDist());
        for (Pokemon p : pokemons) {
            Arena.updateEdge(p, graph);
            if (!attack.containsValue(p.getEdges().getDest()) || attack.get(bond.getID()) == p.getEdges().getDest()) {
                double distance = graph_algo.shortestPathDist(bond.getSrcNode(), p.getEdges().getDest());
                p.setDistance(distance);
                closest.add(p);
            }
        }
        ArrayList<node_data> path = null;
        if (!closest.isEmpty()) {
            Pokemon target = closest.poll();
            attack.put(bond.getID(), target.getEdges().getDest());
            if (ManageGame.computeStuckProblem(bond, target) >= 8)
                dt = 30;

            if (bond.getSrcNode() == target.getEdges().getDest()) {
                return target.getEdges().getSrc();
            } else {
                path = new ArrayList<>(graph_algo.shortestPath(bond.getSrcNode(), target.getEdges().getDest()));
            }
        }
        if (path == null || path.isEmpty()) {
            LinkedList<edge_data> edgeData = new LinkedList<>(graph.getE(bond.getSrcNode()));
            return edgeData.getFirst().getDest();
        }
        return path.get(1).getKey();
    }

    /**
     * the comparator of the closest Pokemon to a specific Agent.
     */
    public static class ComparatorDist implements Comparator<Pokemon> {
        @Override
        public int compare(Pokemon o1, Pokemon o2) {
            return Double.compare(o1.getDistance(), o2.getDistance());
        }
    }

}