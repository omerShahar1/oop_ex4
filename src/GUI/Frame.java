package GUI;

import api.Algo;
import api.DirectedWeightedGraphAlgorithms;
import api.Graph;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import run.Game;

public class Frame extends JFrame {

    DirectedWeightedGraphAlgorithms algo;
    Game game;

    public Frame(DirectedWeightedGraphAlgorithms algo, Game game) {
        this.algo = algo;
        this.game = game;

        this.setTitle("Omer & Zadok's Pockemon Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1400, 800);
        this.getContentPane().setBackground(new Color(94, 87, 87));
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));

        ///////////////////////////////////
        // head label
        JLabel head = new JLabel("Pockemon Game");
        head.setFont(new Font("MV Boli", Font.PLAIN, 40));
        head.setBackground(new Color(94, 87, 87));
        head.setForeground(Color.red);
        head.setOpaque(true);
        // End of head label
        ///////////////////////////////////

        Panel panel = new Panel(this.algo, this.game);
        this.add(head);
        this.add(panel);

        this.setVisible(true);
//        LoginPage logIn = new LoginPage();
    }

    public static void main(String[] args) {
//        DirectedWeightedGraphAlgorithms algo = null;
//
//        try {
//            String json = new String(Files.readAllBytes(Paths.get("data/A0")));
//            Graph g = new Graph(json);
//            algo = new Algo(g);
//            Game game = new Game();
//            Frame frame = new Frame(algo, game);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
