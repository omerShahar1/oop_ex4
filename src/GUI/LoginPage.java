package GUI;

import api.Algo;
import api.DirectedWeightedGraphAlgorithms;
import api.Graph;
import run.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class LoginPage implements ActionListener {

    JFrame frame = new JFrame();
    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JTextField userIdField = new JTextField();
    JLabel userIdLabel = new JLabel("Id: ");
    JLabel messageLabel = new JLabel();

    DirectedWeightedGraphAlgorithms algo;
    Game game;

    LoginPage(DirectedWeightedGraphAlgorithms algo, Game game) {

        this.algo = algo;
        this.game = game;

        userIdLabel.setBounds(50, 100, 75, 25);

        messageLabel.setBounds(125, 250, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 25));

        userIdField.setBounds(125, 100, 200, 25);

        loginButton.setBounds(125, 200, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        resetButton.setBounds(225, 200, 100, 25);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);


        frame.add(userIdLabel);
        frame.add(messageLabel);
        frame.add(userIdField);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == resetButton) {
            userIdField.setText("");
        }

        if (e.getSource() == loginButton) {
            String userID = userIdField.getText();
            Frame f = new Frame(this.algo, this.game);
            this.frame.dispose();

        }

    }

    public static void main(String[] args) {
        DirectedWeightedGraphAlgorithms algo = null;

        try {
            String json = new String(Files.readAllBytes(Paths.get("data/A0")));
            Graph g = new Graph(json);
            algo = new Algo(g);
            Game game = new Game();
            LoginPage loginPage = new LoginPage(algo, game);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
