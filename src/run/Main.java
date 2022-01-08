package run;

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



        Game game = new Game();
        game.setGraph(client.getGraph());

        int firstNode = game.getAlgoGraph().center().getKey();
        JSONObject info = new JSONObject(client.getInfo());
        JSONObject amountInfo = info.getJSONObject("GameServer");
        int amountAgent = amountInfo.getInt("agents");
        for (int i = 0; i < amountAgent; i++)
            client.addAgent("{\"id\":"+firstNode+"}");

        client.start();
        long time=0;

        while (client.isRunning().equals("true"))
        {
            if (time == 0)
            {
                time = Long.parseLong(client.timeToEnd());
            }
            if (Long.parseLong(client.timeToEnd()) < time - 100)
            {
                time= Long.parseLong(client.timeToEnd());
                client.move();
            }
            game.updatePokemons(client.getPokemons());
            game.updateAgent(client.getAgents());
            game.planNext(); //plan the next moves and change the agents dest ant time values.
            int count = 0; //counter for the chooseNextEdge. we can only do 10 at once.

            for (Agent agent: game.getAgents().values())
            {
                client.chooseNextEdge("{\"agent_id\":"+agent.getId()+", \"next_node_id\": " + agent.getDest() + "}");
                count++;
                if(count > 10)
                    break;
            }
        }
    }

}