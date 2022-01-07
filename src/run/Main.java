package run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main
{
    public static void main(String[] args)
    {
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String graphStr = client.getGraph(); //get the graph
        System.out.println(graphStr);

        String pokemonsStr = client.getPokemons();
        System.out.println(pokemonsStr);

        int amountAgent = Game.updateAmountAgent(client.getInfo());
        for (int i = 0; i < amountAgent; i++)
            client.addAgent("{\"id\":" + i + "}");
        String agentsStr = client.getAgents();
        System.out.println(agentsStr);

        String isRunningStr = client.isRunning();
        System.out.println(isRunningStr);

        Game game = new Game(agentsStr, pokemonsStr, graphStr);
        boolean firstTime = true;
        client.start();
        int time = Integer.parseInt(client.timeToEnd())/1000; //time in seconds
        System.out.println("start in: " + time);

        while (client.isRunning().equals("true"))
        {
            if (firstTime || Integer.parseInt(client.timeToEnd())/1000 < time - 3)
            {
                game.pokemons = new ArrayList<>();
                game.addPokemon(client.getPokemons());
                game.set_edges_for_pokemon();
                System.out.println(client.getPokemons());

                firstTime = false;
                client.move();
                for (Agent agent: game.agents.values())
                {
                    game.calculate(agent);
                    System.out.println(client.getAgents());
                    client.chooseNextEdge("{\"agent_id\":"+agent.id+", \"next_node_id\": "+agent.dest+"}");
                    System.out.println("agent_id: "+agent.id + " src: " + agent.src + ", dest: " + agent.dest);
                    System.out.println(client.getAgents());
                    System.out.println();
                }
                time = Integer.parseInt(client.timeToEnd())/1000;
                game.agentsUpdate();
            }
        }
    }

}