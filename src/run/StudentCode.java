package run;

import java.io.IOException;
import java.util.Scanner;

public class StudentCode
{
    public static void main(String[] args)
    {
        Client client = new Client();
        try
        {
            client.startConnection("127.0.0.1", 6666);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        String graphStr = client.getGraph();
        System.out.println(graphStr);
        client.addAgent("{\"id\":0}");
        String agentsStr = client.getAgents();
        System.out.println(agentsStr);
        String pokemonsStr = client.getPokemons();
        System.out.println(pokemonsStr);
        String isRunningStr = client.isRunning();

        Game game = new Game(agentsStr, pokemonsStr, graphStr);
        System.out.println(isRunningStr);
        client.start();

        while (client.isRunning().equals("true"))
        {
            client.move();
            System.out.println(client.getAgents());
            System.out.println(client.timeToEnd());

            game.update();

            for (Agent agent: game.agents.values())
            {
                client.chooseNextEdge("{\"agent_id\":" + agent.src + ", \"next_node_id\": " + agent.dest + "}");
            }

        }
    }

}
