package GUI;

import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import run.Agent;
import run.Game;
import run.Pokemon;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * This class panel is a part of the frame that contains the graph
 */
public class Panel extends JPanel
{
    private final Game game;
    private double xMin;
    private double yMin;
    private double xMax;
    private double yMax;
    private BufferedImage imageAgent;
    private BufferedImage imagePok1;
    private BufferedImage imagePok2;


    public Panel(Game game)
    {
        this.game = game;
        scalingsize();

        setPreferredSize(new Dimension(900, 600));

        try
        {
            imagePok1 = ImageIO.read(new File("images/pika.png"));
            imagePok2 = ImageIO.read(new File("images/balbazor.png"));
            imageAgent = ImageIO.read(new File("images/ash.png"));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        repaint();
    }

    /**
     * This function calculate the bounds of the nodes
     */
    private void scalingsize()
    {
        xMin = Integer.MAX_VALUE;
        yMin = Integer.MAX_VALUE;
        xMax = Integer.MIN_VALUE;
        yMax = Integer.MIN_VALUE;
        Iterator<NodeData> iterator = this.game.getAlgo().getGraph().nodeIter();
        while (iterator.hasNext())
        {
            NodeData node = iterator.next();
            xMin = Math.min(node.getLocation().x(), xMin);
            yMin = Math.min(node.getLocation().y(), yMin);
            xMax = Math.max(node.getLocation().x(), xMax);
            yMax = Math.max(node.getLocation().y(), yMax);
        }
    }


    /**
     * This function calculate the scale x position for specific node
     * to put in the panel
     *
     * @param pos
     * @return
     */
    private int getXScale(GeoLocation pos) {
        return (int) ((((pos.x() - xMin) / (xMax - xMin)) * this.getWidth() * 0.9) + (0.05 * this.getWidth()));
    }


    /**
     * This function calculate the scale y position for specific node
     * to put in the panel
     * @param pos
     * @return
     */
    private int getYScale(GeoLocation pos) {
        return (int) ((((pos.y() - yMin) * (this.getHeight() - 100) / (yMax - yMin)) * 0.9) + (0.05 * (this.getHeight() - 100)));
    }

    /**
     * This function draw the graph on the panel
     * it's draw the nodes, edges, pockemons and agents
     * @param g
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g1 = (Graphics2D) g;
        Iterator<NodeData> nodes = this.game.getAlgo().getGraph().nodeIter();
        while (nodes.hasNext())
        {
            NodeData node = nodes.next();
            g.setColor(Color.red);
            g.fillOval(getXScale(node.getLocation()), getYScale(node.getLocation()), 15, 15);
            g.drawString(node.getKey() + "", getXScale(node.getLocation()), getYScale(node.getLocation()));
        }
        Iterator<EdgeData> Edges = this.game.getAlgo().getGraph().edgeIter();
        while (Edges.hasNext())
        {
            EdgeData currEdge = Edges.next();
            NodeData src = this.game.getAlgo().getGraph().getNode(currEdge.getSrc());
            NodeData dest = this.game.getAlgo().getGraph().getNode(currEdge.getDest());
            g.setColor(Color.BLACK);
            g.drawLine(getXScale(src.getLocation()) + 8, getYScale(src.getLocation()) + 8, getXScale(dest.getLocation()) + 8, getYScale(dest.getLocation()) + 8);
            drawArrow(g1, getXScale(src.getLocation()) + 8, getYScale(src.getLocation()) + 8, getXScale(dest.getLocation()) + 8, getYScale(dest.getLocation()) + 8);
        }

        g.setColor(Color.gray);
        for (Pokemon pokemon: game.getPokemons())
        {
            if(pokemon.getType() > 0)
                g.drawImage(imagePok1, getXScale(pokemon.getPos()) - 8, getYScale(pokemon.getPos()) - 8, 40, 40, null);
            else
                g.drawImage(imagePok2, getXScale(pokemon.getPos()) - 8, getYScale(pokemon.getPos()) - 8, 35, 35, null);
        }
        for (Agent agent: game.getAgents().values())
            g.drawImage(imageAgent, getXScale(agent.getPos()) - 8, getYScale(agent.getPos()) - 8, 35, 35, null);
    }


    /**
     * this function draw an arrow for a specific edge
     * @param g1
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2)
    {
        Graphics2D g = (Graphics2D) g1.create();
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[]{len, len - 5, len - 5, len}, new int[]{0, -5, 5, 0}, 4);
    }


}