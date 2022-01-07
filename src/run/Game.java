package run;

import api.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

public class Game
{
    private ArrayList<Pokemon> pokemons;
    private HashMap<Integer, Agent> agents;
    private DirectedWeightedGraphAlgorithms algo;
    boolean stopOrNot;

    public Game()
    {
        this.algo = null;
        this.agents = new HashMap<>();
        this.pokemons = new ArrayList<>();
        stopOrNot = false;
    }

    public DirectedWeightedGraphAlgorithms getAlgoGraph() {
        return algo;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public Agent getAgent(int id)
    {
        return agents.get(id);
    }


    public void updateAgent(String jsonStr)
    {
        JSONObject j = new JSONObject(jsonStr);
        JSONArray agentsArray = j.getJSONArray("Agents");

        for(int i = 0; i < agentsArray.length(); i++)
        {
            JSONObject currentAgent = agentsArray.getJSONObject(i).getJSONObject("Agent");
            int id = currentAgent.getInt("id");
            double value = currentAgent.getDouble("value");
            int src = currentAgent.getInt("src");
            int dest = currentAgent.getInt("dest");
            double speed = currentAgent.getDouble("speed");
            String[] pos = currentAgent.getString("pos").split(",");
            double x = Double.parseDouble(pos[0]);
            double y = Double.parseDouble(pos[1]);
            double z = Double.parseDouble(pos[2]);

            if (agents.containsKey(id))
            {
                Agent agent = agents.get(id);
                agent.setDest(dest);
                agent.setPos(new Location(x,y,z));
                agent.setValue(value);
                agent.setSpeed(speed);
                agent.setSrc(src);
            }
            else
            {
                agents.put(id, new Agent(id, src, dest, speed, new Location(x,y,z)));
            }
        }



    }

    public HashMap<Integer, Agent> getAgents() {
        return agents;
    }

    public void setGraph(String jsonStr) {
        this.algo = new Algo(jsonStr);
    }


    public void loadAgent(String jsonStr)
    {
        agents.clear();
        JSONObject j = new JSONObject(jsonStr);
        JSONArray agentsArray = j.getJSONArray("Agents");

        for(int i = 0; i < agentsArray.length(); i++)
        {
            JSONObject currentAgent = agentsArray.getJSONObject(i).getJSONObject("Agent");
            int id = currentAgent.getInt("id");
            double value = currentAgent.getDouble("value");
            int src = currentAgent.getInt("src");
            int dest = currentAgent.getInt("dest");
            double speed = currentAgent.getDouble("speed");
            String[] pos = currentAgent.getString("pos").split(",");
            double x = Double.parseDouble(pos[0]);
            double y = Double.parseDouble(pos[1]);
            double z = Double.parseDouble(pos[2]);
            this.agents.put(id, new Agent(id, src, dest, speed, new Location(x,y,z)));
        }
    }


    public void loadPokemons(String jsonStr)
    {
        pokemons.clear();
        JSONObject j = new JSONObject(jsonStr);
        JSONArray pockemonArray = j.getJSONArray("Pokemons");

        for(int i = 0; i < pockemonArray.length(); i++)
        {
            JSONObject currentPockemon = pockemonArray.getJSONObject(i).getJSONObject("Pokemon");
            double value = currentPockemon.getDouble("value");
            int type = currentPockemon.getInt("type");
            String[] pos = currentPockemon.getString("pos").split(",");
            double x = Double.parseDouble(pos[0]);
            double y = Double.parseDouble(pos[1]);
            double z = Double.parseDouble(pos[2]);
            Pokemon newPokemon = new Pokemon(value, type,  new Location(x,y,z));
            EdgeData edge = findEdgeOfPokemon(newPokemon.getPos(), newPokemon.getType());
            newPokemon.setEdge(edge);
            this.pokemons.add(newPokemon);
        }
    }

    public void updatePkemons(String json){
        List<Pokemon> sort = new ArrayList<>();
        try {
            // parsing file "JSONExample.json"
            Object obj = new JSONParser().parse(json);
            // typecasting obj to JSONObject
            org.json.simple.JSONObject jo = (org.json.simple.JSONObject) obj;
            // getting Nodes
            org.json.simple.JSONArray ja = (org.json.simple.JSONArray) jo.get("Pokemons");
            Iterator AgentIterator = ja.iterator();
            while (AgentIterator.hasNext()) {
                Iterator<Map.Entry> Pokemon = ((Map) AgentIterator.next()).entrySet().iterator();
                while (Pokemon.hasNext()) {
                    obj = new JSONParser().parse((String) Pokemon.next().getValue().toString());
                    jo = (org.json.simple.JSONObject) obj;
                    int type = (int) ((long) jo.get("type"));
                    double value = (double) jo.get("value");
                    String s = (String) jo.get("pos");
                    String[] pos = s.split(",");
                    GeoLocation loc = new Location(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]));
                    EdgeData edge = this.findEdgeOfPokemon(loc, type);
                    Pokemon a = new Pokemon(value, type, loc);
                    a.setEdge(edge);
                    sort.add(a);
                }
            }
            for (int i = this.pokemons.size()-1;i>=0;i--) {
                boolean delete = true;
                Pokemon old = this.pokemons.get(i);
                for (Pokemon curr : sort) {
                    if (old.equals(curr)) {
                        delete = false;
                        break;
                    }
                }
                if (delete == true) {
                    this.pokemons.remove(old);
                }
            }
            for (Pokemon curr : sort){
                boolean addornot = true;
                for(Pokemon old: this.pokemons){
                    if (curr.equals(old)){
                        addornot =false;
                    }
                }
                if(addornot == true) {
                    this.pokemons.add(curr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public int updateAmountAgent(String jsonStr)
    {
        JSONObject j = new JSONObject(jsonStr);
        JSONObject gameServer = j.getJSONObject("GameServer");
        return gameServer.getInt("agents");
    }


    public EdgeData findEdgeOfPokemon(GeoLocation pos, int type)
    {
        Iterator<EdgeData> iterator = algo.getGraph().edgeIter();
        while (iterator.hasNext())
        {
            EdgeData edge = iterator.next();
            double distSrcDest = distanceBetween2Points(algo.getGraph().getNode(edge.getSrc()).getLocation(), algo.getGraph().getNode(edge.getDest()).getLocation());
            double distSrcPok = distanceBetween2Points(algo.getGraph().getNode(edge.getSrc()).getLocation(), pos);
            double distDestPok = distanceBetween2Points(algo.getGraph().getNode(edge.getDest()).getLocation(), pos);
            if (Math.abs(distSrcDest - (distDestPok + distSrcPok)) < 0.000001)
            {
                if ((type < 0 && edge.getSrc() > edge.getDest()) || (type > 0 && edge.getDest() > edge.getSrc()))
                {
                    return edge;
                }
            }
        }
        return null;
    }

    public Double distanceBetween2Points(GeoLocation a, GeoLocation b) {
        return Math.sqrt(Math.pow((a.x() - b.x()), 2) + Math.pow((a.y() - b.y()), 2));
    }

    public int whichAgent(Pokemon pokemon)
    {
        int bestAgent = -1;
        double minTime = Integer.MAX_VALUE;
        for (int i = 0; i < agents.size(); i++)
        {
            if (agents.get(i).path.isEmpty())
            {
                if (timeToPokemon(pokemon, agents.get(i)) < minTime)
                {
                    minTime = timeToPokemon(pokemon, agents.get(i));
                    bestAgent = i;
                }
            }
        }
        return bestAgent;
    }

    public double timeToPokemon(Pokemon pokemon, Agent agent)
    {
        int srcA = agent.getSrc();
        int srcP= pokemon.getEdge().getSrc();
        double speedA= agent.getSpeed();
        double weight = algo.shortestPathDist(srcA,srcP);
        return (weight/speedA);
    }

    public void addToAgentPath(int agentId, Pokemon pokemon)
    {
        if(agents.get(agentId).getSrc() == pokemon.getEdge().getSrc())
        {
            agents.get(agentId).path.add(agents.get(agentId).getSrc());
            agents.get(agentId).path.add(pokemon.getEdge().getSrc());
            return;
        }
        LinkedList<NodeData> path = algo.shortestPath(agents.get(agentId).getSrc(), pokemon.getEdge().getSrc());
        for (NodeData node: path)
        {
            agents.get(agentId).path.add(node.getKey());
        }
        agents.get(agentId).path.add(pokemon.getEdge().getDest());
    }


    public void setStop(boolean ans){
        this.stopOrNot=ans;
    }
}

