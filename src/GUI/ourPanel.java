package GUI;

import api.*;
import util.*;
import run.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class runs every graphical thing in the game.
 */
public class ourPanel extends JPanel
{
    Game game;
    Range2Range range;
    final int r = 7;
    double grade = 0;
    int moves = 0;

    /**
     * a constructor.
     *
     * @param game the game.
     */
    public ourPanel(Game game)
    {
        this.game = game;
        importPictures();
    }

    /**
     * paint in each run-step all graphics contexts.
     *
     * @param g The Graphics class is the abstract base class for all graphics contexts.
     */
    public void paint(Graphics g)
    {
        int width = this.getWidth();
        int height = this.getHeight();
        resize();
        g.drawImage(background, 0, 0, width, height, this);
        drawGraph((Graphics2D) g);
        drawPokemon((Graphics2D) g);
        drawAgents((Graphics2D) g);
        long seconds = Long.parseLong(game.getClient().timeToEnd())/1000;
        g.setColor(Color.black);
        g.drawImage(blur, -5, 0, 170, 150, this);
        g.drawImage(logo, 5, 0, 150, 80, this);
        g.setColor(Color.white);
        g.drawString("Time: 00:" + seconds, 25, 90);
        g.drawString("Grade: " + grade, 25, 110);
        g.drawString("Moves: " + moves, 25, 130);
    }

    /**
     * This method is used to resize the frame if it happen.
     */
    private void resize() {
        Range rx = new Range(120, this.getWidth() - 120);
        Range ry = new Range(this.getHeight() - 70, 170);
        Range2D frame = new Range2D(rx, ry);
        DirectedWeightedGraphAlgorithms graph = game.getAlgo();
        range = w2f(graph, frame);
    }

    /**
     * draws the pokemons and their stats in the panel.
     *
     * @param g - This Graphics2D class extends the Graphics class to provide more sophisticated control.
     */
    private void drawPokemon(Graphics2D g)
    {
        java.util.List<Pokemon> fs = game.getPokemons();
        if (fs != null)
        {
            for (Pokemon f : fs)
            {
                GeoLocation c = f.getPos();
                Color color = Color.green;
                if (f.getType() < 0)
                {
                    color = Color.orange;
                }
                if (c != null)
                {
                    GeoLocation fp = this.range.world2frame(c);
                    if (f.getValue() < 6) {
                        pikachu(g, fp, f, color);
                    } else if (f.getValue() < 11) {
                        charizard(g, fp, f, color);
                    } else {
                        mewtwo(g, fp, f, color);
                    }
                }
            }
        }
    }

    /**
     * draws the agents and their stats in the panel.
     *
     * @param g - This Graphics2D class extends the Graphics class to provide more sophisticated control.
     */
    private void drawAgents(Graphics2D g) {
        List<Agent> rs = ManageGame.getAgents();
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location agent_location = rs.get(i).getLocation();
            String value = String.valueOf((int) rs.get(i).getPoints());
            int r = 8;
            if (agent_location != null) {
                geo_location fp = this.range.world2frame(agent_location);
                g.drawImage(ash, (int) fp.x() - 30, (int) fp.y() - 30, 5 * r, 5 * r, this);
                g.drawImage(blur, (int) fp.x() - 75, (int) fp.y() - 70, 140, 35, this);
                g.setColor(Color.RED);
                g.setFont(new Font("Segoe UI", Font.BOLD, 25));
                g.drawString("Ash", (int) fp.x() - 70, (int) fp.y() - 44);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Segoe UI", Font.BOLD, 15));
                g.drawString("Points:" + value, (int) fp.x() - 20, (int) fp.y() - 7 * r);
                g.drawString("Speed:" + (int) rs.get(i).getSpeed(), (int) fp.x() - 20, (int) fp.y() - 5 * r);
                g.setFont(new Font("Segoe UI", Font.BOLD, 20));
            }
            i++;
        }
    }

    /**
     * draws the graph and its nodes and edges in the panel.
     *
     * @param g - This Graphics2D class extends the Graphics class to provide more sophisticated control.
     */
    private void drawGraph(Graphics2D g) {
        directed_weighted_graph graph = ManageGame.getGraph();
        for (node_data runner : graph.getV()) {
            for (edge_data edge : graph.getE(runner.getKey())) {
                drawEdge(edge, g);
            }
        }
        for (node_data runner : graph.getV()) {
            drawNode(runner, g);
        }
    }

    /**
     * draws the nodes and their keys in the panel.
     *
     * @param n - a node_data type.
     * @param g - This Graphics2D class extends the Graphics class to provide more sophisticated control.
     */
    private void drawNode(node_data n, Graphics2D g) {
        g.setColor(new Color(73, 155, 84));
        geo_location pos = n.getLocation();
        geo_location fp = this.range.world2frame(pos);
        g.drawImage(pokador, (int) fp.x() - 15, (int) fp.y() - 30, 4 * r, 5 * r, this);
        g.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g.setColor(Color.black);
        g.drawString(" " + n.getKey(), (int) fp.x() - 14, (int) fp.y() - 34);
    }

    /**
     * draws the edges and their keys in the panel.
     *
     * @param e - an edge_data type.
     * @param g - This Graphics2D class extends the Graphics class to provide more sophisticated control.
     */
    private void drawEdge(edge_data e, Graphics2D g) {
        directed_weighted_graph gg = ManageGame.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this.range.world2frame(s);
        geo_location d0 = this.range.world2frame(d);
        g.setColor(new Color(173, 122, 68));
        drawArrow(g, (int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
    }

    /**
     * draws an arrow between two given points.
     *
     * @param g1 The Graphics class is the abstract base class for all graphics contexts.
     * @param x1 int type.
     * @param y1 int type.
     * @param x2 int type.
     * @param y2 int type.
     */
    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);
        int ARR_SIZE = 10;
        g.setStroke(new BasicStroke(5));
        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[]{len - 10, len - ARR_SIZE - 20, len - ARR_SIZE - 20, len - 10}, new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }

    /**
     * draws pikachu.
     *
     * @param g     The Graphics class is the abstract base class for all graphics contexts.
     * @param fp    the Pokemon's location.
     * @param f     the Pokemon.
     * @param color this color represents if the pokemon is going up or down.
     */
    private void pikachu(Graphics g, geo_location fp, Pokemon f, Color color) {
        g.drawImage(pikachu, (int) fp.x() - 30, (int) fp.y() - 30, 9 * r, 8 * r, this);
        g.drawImage(blur, (int) fp.x() - 36, (int) fp.y() - 78, 120, 50, this);
        g.setColor(color);
        g.drawString("Pikachu", (int) fp.x() - 12, (int) fp.y() - 60);
        g.setColor(Color.WHITE);
        g.drawString("Value:" + f.getValue(), (int) fp.x() - 18, (int) fp.y() - 39);
    }

    /**
     * draws charizard.
     *
     * @param g     The Graphics class is the abstract base class for all graphics contexts.
     * @param fp    the Pokemon's location.
     * @param f     the Pokemon.
     * @param color this color represents if the pokemon is going up or down.
     */
    private void charizard(Graphics g, geo_location fp, Pokemon f, Color color) {
        g.drawImage(charizard, (int) fp.x() - 30, (int) fp.y() - 30, 10 * r, 12 * r, this);
        g.drawImage(blur, (int) fp.x() - 65, (int) fp.y() - 12 * r, 130, 50, this);
        g.setColor(color);
        g.drawString("Charizard", (int) fp.x() - 45, (int) fp.y() - 9 * r);
        g.setColor(Color.WHITE);
        g.drawString("Value:" + f.getValue(), (int) fp.x() - 42, (int) fp.y() - 6 * r);
    }

    /**
     * draws mewtwo.
     *
     * @param g     The Graphics class is the abstract base class for all graphics contexts.
     * @param fp    the Pokemon's location.
     * @param f     the Pokemon.
     * @param color this color represents if the pokemon is going up or down.
     */
    private void mewtwo(Graphics g, geo_location fp, Pokemon f, Color color) {
        g.drawImage(mewtwo, (int) fp.x() - 40, (int) fp.y() - 30, 8 * r, 8 * r, this);
        g.drawImage(blur, (int) fp.x() - 60, (int) fp.y() - 78, 130, 50, this);
        g.setColor(color);
        g.drawString("Mewtwo", (int) fp.x() - 35, (int) fp.y() - 58);
        g.setColor(Color.WHITE);
        g.drawString("Value:" + f.getValue(), (int) fp.x() - 42, (int) fp.y() - 38);
    }

    ///// import pictures /////
    static BufferedImage background = null;
    static BufferedImage logo = null;
    static BufferedImage blur = null;
    static Image ash = null;
    static BufferedImage pokador = null;
    static Image pikachu = null;
    static Image charizard = null;
    static Image mewtwo = null;

    /**
     * this method is used to efficient the panel by importing the pictures just once and for all.
     */
    public static void importPictures() {
        try {
            background = ImageIO.read(new File("resource/background.jpg"));
            logo = ImageIO.read(new File("resource/logo.png"));
            blur = ImageIO.read(new File("resource/blur.png"));
            ash = Toolkit.getDefaultToolkit().createImage("resource/ash.gif");
            pokador = ImageIO.read(new File("resource/pokador.png"));
            pikachu = Toolkit.getDefaultToolkit().createImage("resource/pikachu.gif");
            charizard = Toolkit.getDefaultToolkit().createImage("resource/charizard.gif");
            mewtwo = Toolkit.getDefaultToolkit().createImage("resource/mewtwo.gif");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method is used to allow set the move in the panel right from the Ex2.
     *
     * @param move the number of current moves in the game.
     */
    public void setMoves(int move) {
        moves = move;
    }

    /**
     * this method is used to allow set the grade in the panel right from the Ex2.
     *
     * @param newGrade the number of current grade in the game.
     */
    public void setGrade(int newGrade) {
        grade = newGrade;
    }





    /**
     * @param g     directed_weighted_graph type.
     * @param frame Range2D type.
     * @return a coordinate system of this graph (Range2Range type).
     */
    public static Range2Range w2f(DirectedWeightedGraphAlgorithms g, Range2D frame) {
        return new Range2Range(GraphRange(g.getGraph()), frame);
    }

    /**
     * generate the coordinates of this graph range.
     *
     * @param g directed_weighted_graph type.
     * @return Range2D type.
     */
    private static Range2D GraphRange(DirectedWeightedGraph g) {
        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        boolean first = true;
        for (NodeData runner : g.getV()) {
            geo_location p = runner.getLocation();
            if (first) {
                x0 = p.x();
                x1 = x0;
                y0 = p.y();
                y1 = y0;
                first = false;
            } else {
                if (p.x() < x0) x0 = p.x();
                if (p.x() > x1) x1 = p.x();
                if (p.y() < y0) y0 = p.y();
                if (p.y() > y1) y1 = p.y();
            }
        }
        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }
}