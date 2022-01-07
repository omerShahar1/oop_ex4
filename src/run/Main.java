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



        Game game = new Game();
        game.setGraph(client.getGraph());
        game.loadPokemons(client.getPokemons());
        int firstNode = game.getAlgoGraph().center().getKey();
        int amountAgent = game.updateAmountAgent(client.getInfo());
        for (int i = 0; i < amountAgent; i++)
            client.addAgent("{\"id\":"+firstNode+"}");
        game.loadAgent(client.getAgents());


        client.start();
        int time=0;


        System.out.println(client.getPokemons());
        System.out.println(client.getAgents());
        System.out.println(client.getGraph());
        System.out.println();
        while (client.isRunning().equals("true")) {
            if (time == 0){
                time = Integer.parseInt(client.timeToEnd());
            }
            if (Integer.parseInt(client.timeToEnd()) < time - 100){
                time= Integer.parseInt(client.timeToEnd());
                client.move();
            }
            game.updatePkemons(client.getPokemons()); ///////////////maybe
            game.updateAgent(client.getAgents());
            for (int i = 0; i < game.getPokemons().size(); i++) {
                if (!game.getPokemons().get(i).isTargeted()) {
                    int agentAssign = game.whichAgent(game.getPokemons().get(i));
                    if (agentAssign != -1) {
                        game.addToAgentPath(agentAssign, game.getPokemons().get(i));
                        game.getPokemons().get(i).setTargeted(true);
                    }
                }
            }
            for (Agent agent: game.getAgents().values()) {
                if (agent.path.peek()!= null && agent.getSrc() == agent.path.peek() && agent.getDest()==-1){
                    agent.path.poll();
                }
                if (!agent.path.isEmpty() && agent.getDest()==-1)
                {
                    System.out.println(client.getAgents());
                    client.chooseNextEdge("{\"agent_id\":"+agent.getId()+", \"next_node_id\": " + agent.path.peek() + "}");
                    System.out.println(client.getAgents());
                    System.out.println(client.getPokemons());
                    System.out.println(client.getInfo());
                    System.out.println();
                }
            }
            if (game.stopOrNot){
                client.stop();
            }
        }
    }
}


//    {
//        Client client = new Client();
//        try {
//            client.startConnection("127.0.0.1", 6666);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String graphStr = client.getGraph(); //get the graph
//        System.out.println(graphStr);
//
//        String pokemonsStr = client.getPokemons();
//        System.out.println(pokemonsStr);
//
//        int amountAgent = Game.updateAmountAgent(client.getInfo());
//        for (int i = 0; i < amountAgent; i++)
//            client.addAgent("{\"id\":" + i + "}");
//        String agentsStr = client.getAgents();
//        System.out.println(agentsStr);
//
//        String isRunningStr = client.isRunning();
//        System.out.println(isRunningStr);
//
//        Game game = new Game(agentsStr, pokemonsStr, graphStr);
//        boolean firstTime = true;
//        client.start();
//        int time = Integer.parseInt(client.timeToEnd())/1000; //time in seconds
//        System.out.println("start in: " + time);
//
//        while (client.isRunning().equals("true"))
//        {
//            if (firstTime || Integer.parseInt(client.timeToEnd())/1000 < time - 3)
//            {
//                game.pokemons = new ArrayList<>();
//                game.addPokemon(client.getPokemons());
//                game.set_edges_for_pokemon();
//                System.out.println(client.getPokemons());
//
//                firstTime = false;
//                client.move();
//                for (Agent agent: game.agents.values())
//                {
//                    game.calculate(agent);
//                    System.out.println(client.getAgents());
//                    client.chooseNextEdge("{\"agent_id\":"+agent.id+", \"next_node_id\": "+agent.dest+"}");
//                    System.out.println("agent_id: "+agent.id + " src: " + agent.src + ", dest: " + agent.dest);
//                    System.out.println(client.getAgents());
//                    System.out.println();
//                }
//                time = Integer.parseInt(client.timeToEnd())/1000;
//                game.agentsUpdate();
//            }
//        }
//    }
