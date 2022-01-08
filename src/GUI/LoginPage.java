package GUI;
import run.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage implements ActionListener
{

    JFrame frame = new JFrame();
    JButton loginButton = new JButton("Login");
    JTextField userIdField = new JTextField();
    JLabel userIdLabel = new JLabel("ID number: ");
    JLabel messageLabel = new JLabel();
    Game game;

    public LoginPage(Game game)
    {
        this.game = game;
        userIdLabel.setBounds(50, 100, 75, 25);
        messageLabel.setBounds(125, 250, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 25));
        userIdField.setBounds(125, 100, 200, 25);
        loginButton.setBounds(125, 200, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);
        frame.add(userIdLabel);
        frame.add(messageLabel);
        frame.add(userIdField);
        frame.add(loginButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == loginButton)
        {
            game.getClient().login(userIdField.getText());
            this.frame.dispose();
        }
    }
}
