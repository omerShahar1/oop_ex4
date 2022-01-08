//package GUI;
//
//
//import api.Algo;
//import api.DirectedWeightedGraphAlgorithms;
//import javax.swing.*;
//import java.awt.*;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import run.Game;
//
//public class Frame extends JFrame
//{
//    Game game;
//    LoginPage loginPage;
//
//
//    public Frame(Game game)
//    {
//        this.game = game;
//        this.loginPage = new LoginPage(game);
//        this.setTitle("Omer & Zadok's Pockemon Game");
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setSize(1400, 800);
//        this.getContentPane().setBackground(new Color(94, 87, 87));
//        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
//
//        ///////////////////////////////////
//        // head label
//        JLabel head = new JLabel("Pockemon Game");
//        head.setFont(new Font("MV Boli", Font.PLAIN, 40));
//        head.setBackground(new Color(94, 87, 87));
//        head.setForeground(Color.red);
//        head.setOpaque(true);
//        // End of head label
//        ///////////////////////////////////
//
//        Panel panel = new Panel(this.game.getAlgo(), this.game);
//        this.add(head);
//        this.add(panel);
//        this.setVisible(true);
//    }
//
//    public static void main(String[] args)
//    {
//        Game game = new Game();
//        Frame frame = new Frame(game);
//    }
//}


package GUI;
import run.*;
import javax.swing.*;


/**
 * this class is expending JFrame.
 * Frame is basically the frame of the GUI game.
 * the frame holds a panel which is running every graphic object inside it.
 */
public class Frame extends JFrame
{
    Game game;
    Panel panel;

    /**
     * a default constructor.
     */
    public Frame()
    {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * init the frame and the panel.
     * also this is the connector between the logical and the graphical.
     *
     * @param game the Arena.
     */
    public void initFrame(Game game)
    {
        this.game = game;
        initPanel();
    }

    /**
     * init the panel.
     */
    public void initPanel()
    {
        panel = new Panel(this.game);
        this.add(panel);
    }

    /**
     * @return ourPanel type.
     */
    public Panel getPanel()
    {
        return panel;
    }

}