package run;

import api.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class Game
{
    public DirectedWeightedGraphAlgorithms algo;
    public HashMap<Integer, Agent> agents;
    public ArrayList<Pokemon> pokemons;
    public static final double EPS1 = 0.001, EPS2 = EPS1 * EPS1;

    public Game(String agentsStr, String pokemonStr, String graphStr)
    {
        this.algo = new Algo(graphStr); //init the algo and the graph
        this.agents = new HashMap<>();
        this.pokemons = new ArrayList<>();

        addAgent(agentsStr); //init the list (the hashmap) of agents
        addPokemon(pokemonStr); //init the list (arrayList) of pokemons.
        set_edges_for_pokemon();
    }

    public HashMap<Integer, Agent> getAgents() {
        return agents;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public void addPokemon(String jsonStr)
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
            this.pokemons.add(new Pokemon(value, type,  new Location(x,y,z)));
        }
    }

    public void addAgent(String jsonStr)
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
            this.agents.put(id, new Agent(id, src, dest, speed));
        }
    }


    private void set_edges_for_pokemon()
    {
        for(Pokemon p: pokemons)
        {
            Iterator<EdgeData> iter = algo.getGraph().edgeIter();
            while (iter.hasNext())
            {
                EdgeData edge = iter.next();
                if(algo.getGraph().isOnEdge(edge, p.pos, p.type))
                {
                    p.edge = edge;
                    break;
                }
            }
        }
    }

//    /**
//     * This method checks whether pokemon object is on the edge
//     *
//     * @param p    the location of the pokemon by coordinate
//     * @param e    the edge that the pokemon stays on
//     * @param type the status of the location of the pokemon (if his moving up or down)
//     * @return true if and only if this pokemon object are in the edge
//     */
//    private boolean isOnEdge(GeoLocation p, EdgeData e, int type)
//    {
//        int src = e.getSrc();
//        int dest = e.getDest();
//        if (type < 0 && dest > src)
//            return false;
//
//        if (type > 0 && dest < src)
//            return false;
//
//        GeoLocation srcLocation = algo.getGraph().getNode(src).getLocation();
//        GeoLocation destLocation = algo.getGraph().getNode(dest).getLocation();
//        boolean ans = false;
//        double dist = srcLocation.distance(destLocation);
//        double d1 = srcLocation.distance(p) + p.distance(destLocation);
//        if (dist > d1 - EPS2)
//            ans = true;
//        return ans;
//    }

    /**
     * calculating the closest Pokemon to an agent (with no target).
     *
     * @param agent: the agent.
     */
    public void calculate(Agent agent)
    {
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

        agent.path = algo.shortestPath(agent.src, answer.edge.getSrc());
        agent.target = answer;
        agent.dest = agent.path.get(1).getKey();
        answer.targeted = true;
    }

    public void update()
    {
        for (Agent agent: agents.values())
        {
            if(agent.dest == -1)
                calculate(agent);
            agent.update();
        }
    }
}
