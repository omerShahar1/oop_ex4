package run;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
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
        System.out.println(isRunningStr);

        Game game = new Game(agentsStr, pokemonsStr, graphStr);


        client.start();

        while (client.isRunning().equals("true"))
        {
            for(Agent agent: game.getAgents().values())
            {
                client.chooseNextEdge("{\"agent_id\":" + agent.id + ", \"next_node_id\": " + agent.dest + "}");
            }
            boolean stopGame = game.update();

            client.move();
            //System.out.println(client.getAgents());
            for(Agent agent: game.getAgents().values())
            {
                System.out.println("agent: " + agent.id + ", src: " + agent.src + ", dest: " + agent.dest + ", value: " + agent.value);
            }
            System.out.println(client.timeToEnd());

//            Scanner keyboard = new Scanner(System.in);
//            System.out.println("enter the next dest: ");
//            int next = keyboard.nextInt();
//            client.chooseNextEdge("{\"agent_id\":0, \"next_node_id\": " + next + "}");

        }
    }

}