package run;
import GUI.Frame;
import org.json.JSONObject;
import java.io.IOException;


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


        Game game = new Game(client); //new game
        game.setGraph(client.getGraph()); //set the graph
        int firstNode = game.getAlgo().center().getKey(); //find the best starting node
        JSONObject info = new JSONObject(client.getInfo());
        JSONObject amountInfo = info.getJSONObject("GameServer");
        int amountAgent = amountInfo.getInt("agents");
        for (int i = 0; i < amountAgent; i++)
            client.addAgent("{\"id\":" + firstNode + "}"); //add all the new agents to the client

        Frame gui = new Frame(game); //create the gui
        client.start(); //start the game
        long time = Long.parseLong(client.timeToEnd()); //to determine time

        try {
            while (client.isRunning().equals("true") && !game.isStop_the_game())
            {
                if (Long.parseLong(client.timeToEnd()) < time - 100)
                {
                    time = Long.parseLong(client.timeToEnd());
                    client.move();
                }
                gui.update(game);
                game.updatePokemons(client.getPokemons());
                game.updateAgent(client.getAgents());
                game.planNext(); //plan the next moves and change the agents dest ant time values.
                int count = 0; //counter for the chooseNextEdge. we can only do 10 at once.

                for (Agent agent : game.getAgents().values()) {
                    client.chooseNextEdge("{\"agent_id\":" + agent.getId() + ", \"next_node_id\": " + agent.getDest() + "}");
                    count++;
                    if (count > 10)
                        break;
                }
            }
            System.out.println("game over");
            client.stop();
        }
        catch (Exception e)
        {
            System.out.println("game over");
            gui.close();
        }
    }
}