package run;

import api.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Game
{
    private ArrayList<Pokemon> pokemons;
    private HashMap<Integer, Agent> agents;
    private DirectedWeightedGraphAlgorithms algo;
    private final Client client;
    private boolean stop_the_game;

    public Game(Client client)
    {
        this.algo = null;
        this.agents = new HashMap<>();
        this.pokemons = new ArrayList<>();
        this.client = client;
        stop_the_game = false;
    }

    public boolean isStop_the_game() {
        return stop_the_game;
    }

    public void setStop_the_game(boolean stop_the_game) {
        this.stop_the_game = stop_the_game;
    }

    public DirectedWeightedGraphAlgorithms getAlgo() {
        return algo;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public HashMap<Integer, Agent> getAgents() {
        return agents;
    }

    public void setGraph(String jsonStr) {
        this.algo = new Algo(jsonStr);
    }


    public Client getClient() {
        return client;
    }


    public void updateAgent(String jsonStr)
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
            agents.put(id, new Agent(id, src, dest, speed, new Location(x,y,z)));
        }
    }


    public void updatePokemons(String jsonStr)
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


    /**
     * find the edge for a given pokemon location.
     * @param pos pokemon location as GeoLocation object
     * @param type pokemon type as integer
     * @return the correct edge
     */
    public EdgeData findEdgeOfPokemon(GeoLocation pos, int type)
    {
        Iterator<EdgeData> edgesIter = algo.getGraph().edgeIter();
        while (edgesIter.hasNext()) //go over all the edges
        {
            EdgeData edge = edgesIter.next();
            if((type < 0 && edge.getSrc() < edge.getDest()) || (type > 0 && edge.getSrc() > edge.getDest())) //if the type doesn't fit the edge then skip it.
                continue;

            GeoLocation edgeSrc = algo.getGraph().getNode(edge.getSrc()).getLocation();
            GeoLocation edgeDest = algo.getGraph().getNode(edge.getDest()).getLocation();

            double distSrcDest = Math.sqrt(Math.pow((edgeSrc.x() - edgeDest.x()), 2) + Math.pow((edgeSrc.y() - edgeDest.y()), 2)); //distance from src to dest
            double distSrcPok = Math.sqrt(Math.pow((edgeSrc.x() - pos.x()), 2) + Math.pow((edgeSrc.y() - pos.y()), 2)); // distance from src to the pokemon
            double distDestPok = Math.sqrt(Math.pow((edgeDest.x() - pos.x()), 2) + Math.pow((edgeDest.y() - pos.y()), 2)); // distance from dest to the pokemon

            if (Math.abs(distSrcDest - (distDestPok + distSrcPok)) < 0.000001)
            {
                return edge;
            }
        }
        return null;
    }


    /**
     * determine for a given pokemon the best agent to assign.
     * @param pokemon pokemon object
     * @return the selected agent
     */
    public Agent chooseAgent(Pokemon pokemon)
    {
        Agent selectedAgent = null;
        double minTime = Integer.MAX_VALUE;

        for (Agent agent: agents.values()) // go over all the agents
        {
            double weight = algo.shortestPathDist(agent.getSrc(), pokemon.getEdge().getSrc()) + pokemon.getEdge().getWeight();
            double time = weight / agent.getSpeed(); //calculated time (distance/speed = time).
            if(time < minTime)
            {
                minTime = time;
                selectedAgent = agent;
            }
        }
        return selectedAgent;
    }


    /**
     * plan for the next moves of the agents
     */
    public void planNext()
    {

        for (Pokemon pokemon: pokemons)
        {
            Agent agent = chooseAgent(pokemon);
            if(agent == null)
                continue;

            if(agent.getSrc() == pokemon.getEdge().getSrc())
            {
                agent.setDest(pokemon.getEdge().getDest());
                continue;
            }

            LinkedList<NodeData> tempPath = algo.shortestPath(agent.getSrc(), pokemon.getEdge().getSrc());
            agent.setDest(tempPath.get(1).getKey());
        }
    }

}

