package run;

import api.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

public class Game
{
    public DirectedWeightedGraphAlgorithms algo;
    public HashMap<Integer, Agent> agents;
    public HashMap<Integer, HashMap<Integer, Double>> points; //points of every edge
    public ArrayList<Pokemon> pokemons;
    public double counterPoints; //how many points we made.

    public Game(String agentsStr, String pokemonStr, String graphStr)
    {
        this.algo = new Algo(graphStr); //init the algo and the graph
        this.agents = new HashMap<>();
        this.pokemons = new ArrayList<>();
        points = new HashMap<>();
        counterPoints = 0;

        Iterator<NodeData> nodeIter1 = algo.getGraph().nodeIter();
        Iterator<NodeData> nodeIter2;
        while (nodeIter1.hasNext())
        {
            NodeData node1 = nodeIter1.next();
            points.put(node1.getKey(), new HashMap<>());
            nodeIter2 = algo.getGraph().nodeIter();
            while (nodeIter2.hasNext())
            {
                points.get(node1.getKey()).put(nodeIter2.next().getKey(), 0.0);
            }
        }

        addAgent(agentsStr); //init the list (the hashmap) of agents
        addPokemon(pokemonStr); //init the list (arrayList) of pokemons.
        set_edges_for_pokemon();
    }

    public void addPokemon(String jsonStr) //receive json string of the pokemons and insert them to the game
    {
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
            this.pokemons.add(newPokemon);
        }
    }

    public void addAgent(String jsonStr) //receive json string of the agents and insert them to the game
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
            this.agents.put(id, new Agent(id, src, dest, speed, new Location(x,y,z)));
        }
    }


    public static int updateAmountAgent(String jsonStr)
    {
        JSONObject j = new JSONObject(jsonStr);
        JSONObject gameServer = j.getJSONObject("GameServer");
        return gameServer.getInt("agents");
    }


    /**
     * This method set the edge value in all the game pokemons
     */
    public void set_edges_for_pokemon()
    {
        for(Pokemon pokemon: pokemons)
        {
            EdgeData edge = findEdgeOfPokemon(pokemon.pos, pokemon.type);
            pokemon.edge = edge;
            points.get(edge.getSrc()).put(edge.getDest(), pokemon.value);
        }
    }


    /**
     * calculating the closest Pokemon to an agent (with no target).
     *
     * @param agent: the agent.
     */
    public void calculate(Agent agent)
    {
        if(agent.target != null)
            return;

        double minWeight = Double.MAX_VALUE;
        Pokemon answer = null;
        for (Pokemon pokemon : pokemons)
        {
            if(pokemon.targeted) //check if the pokemon already been targeted.
                continue;

            if(agent.src == pokemon.edge.getSrc())
            {
                agent.dest = pokemon.edge.getDest();
                agent.target = pokemon;
                agent.path = algo.shortestPath(agent.src, pokemon.edge.getDest());
                pokemon.targeted = true;
                return;
            }
            double weight = algo.shortestPathDist(agent.src, pokemon.edge.getSrc());
            if(minWeight > weight)
            {
                minWeight = weight;
                answer = pokemon;
            }
        }

        if (answer == null)
            return;

        agent.path = algo.shortestPath(agent.src, answer.edge.getSrc());
        agent.target = answer;
        agent.dest = agent.path.get(1).getKey();
        answer.targeted = true;
    }


    public void agentsUpdate()
    {
        for (Agent agent: agents.values())
        {
            if(agent.target == null)
                continue;

            counterPoints += (points.get(agent.src).get(agent.dest));
            agent.value += (points.get(agent.src).get(agent.dest));
            points.get(agent.src).put(agent.dest, 0.0);
            agent.pos = algo.getGraph().getNode(agent.dest).getLocation();

            if(agent.src == agent.target.edge.getSrc()) //we are on the target src.
            {
                agent.src = agent.target.edge.getDest();
                agent.dest = -1;
                agent.target = null;
                agent.path = null;
            }
            else //we are not on the target src.
            {
                agent.path.removeFirst();
                agent.src = agent.path.getFirst().getKey();
                if(agent.path.size() == 1)
                    agent.dest = agent.target.edge.getDest();
                else
                    agent.dest = agent.path.get(1).getKey();
            }
        }
    }



    public EdgeData findEdgeOfPokemon(GeoLocation pos, int type) {
        Iterator<EdgeData> iterator = algo.getGraph().edgeIter();
        while (iterator.hasNext())
        {
            EdgeData edge = iterator.next();
            double distSrcDest = findDistance(algo.getGraph().getNode(edge.getSrc()).getLocation(), algo.getGraph().getNode(edge.getDest()).getLocation());
            double distSrcPok = findDistance(algo.getGraph().getNode(edge.getSrc()).getLocation(), pos);
            double distDestPok = findDistance(algo.getGraph().getNode(edge.getDest()).getLocation(), pos);
            if (Math.abs(distSrcDest - (distDestPok + distSrcPok)) < 0.000001)
            {
                if (type < 0 && edge.getSrc() > edge.getDest())
                {
                    return edge;
                }
                else if (type > 0 && edge.getDest() > edge.getSrc())
                {
                    return edge;
                }
            }
        }
        return null;
    }


    public Double findDistance(GeoLocation a, GeoLocation b)
    {
        return Math.sqrt(Math.pow((a.x() - b.x()), 2) + Math.pow((a.y() - b.y()), 2));
    }

}
