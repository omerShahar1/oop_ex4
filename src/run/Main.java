package run;

import java.io.IOException;
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
        //String playerID = "207689621";
        boolean firstTime = true;
        String turnData;
        //client.login("111");
        client.start();
        int time=Integer.parseInt(client.timeToEnd());

        while (client.isRunning().equals("true"))
        {
            if (firstTime || Integer.parseInt(client.timeToEnd()) < time - 3000)
            {
                firstTime = false;
                time= Integer.parseInt(client.timeToEnd());
                game.agentsUpdate();
                client.move();
            }
            else
                continue;



            for (Agent agent: game.agents.values())
                if (agent.dest == -1)
                    game.calculate(agent);

            for(Agent agent: game.agents.values())
                client.chooseNextEdge("{\"agent_id\":"+agent.id+", \"next_node_id\": "+agent.dest+"}");

            System.out.println(game.counterPoints);
            System.out.println(client.getAgents());

        }
    }

}