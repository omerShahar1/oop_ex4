package GUI;
import org.json.JSONObject;
import run.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class contain all the parts of the game
 */
public class Frame implements ActionListener
{
    private final Game game;
    Panel panel;
    JFrame frame;
    JLabel time_label;
    JLabel score_label;
    JPanel TimeScore;
    JButton stop;
    double Score;
    String Time;


    public Frame(Game game)
    {
        this.game = game;
        frame = new JFrame();
        JSONObject info = new JSONObject(game.getClient().getInfo());
        JSONObject amountInfo = info.getJSONObject("GameServer");
        double score = amountInfo.getDouble("grade");
        setScoreTime(game.getClient().timeToEnd(), score);
        frame.setTitle("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.panel = new Panel(game);
        frame.add(panel);
        frame.repaint();
        frame.pack();
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());
    }


    /**
     * This function update the score and the time of the game
     * @param game
     */
    public void update(Game game)
    {
        JSONObject info = new JSONObject(game.getClient().getInfo());
        JSONObject amountInfo = info.getJSONObject("GameServer");
        Score = amountInfo.getDouble("grade");
        Time = "" + Long.parseLong(game.getClient().timeToEnd())/1000;

        TimeScore.setBounds(0,0,frame.getWidth(),100);
        panel.setBounds(0,100,frame.getWidth(),frame.getHeight()-135);
        if (frame.getWidth() > 900)
        {
            score_label.setLocation((frame.getWidth() / 10) - 20, 20);
            time_label.setLocation((frame.getWidth() / 10) * 4 + 40, 20);
            stop.setLocation(frame.getWidth() - 150, 20);
        }

        this.score_label.setText("Score :" + Score);
        this.time_label.setText("Time: "+Time);
        frame.repaint();

    }

    /**
     * This function set the score and the time of the game
     * @param time
     * @param score
     */
    public void setScoreTime(String time,double score)
    {
        TimeScore = new JPanel();
        TimeScore.setLayout(null);
        score_label = new JLabel();
        time_label = new JLabel();
        Time = time;
        Score = score;
        time_label.setForeground(Color.black);
        score_label.setForeground(Color.black);
        score_label.setBounds(5, 20,350,40);
        time_label.setBounds(370,20,400,40);
        time_label.setText("Time :" + Time);
        score_label.setText("Score :" + Score);
        TimeScore.add(score_label);
        TimeScore.add(time_label);
        TimeScore.setBackground(Color.darkGray);
        this.setButtonStop();
        frame.add(TimeScore);
    }


    /**
     * This function create the stop button
     * locate it on the frame
     */
    public void setButtonStop()
    {
        stop = new JButton("Stop");
        stop.addActionListener(this);
        stop.setHorizontalTextPosition(JButton.CENTER);
        stop.setSize(20,20);
        stop.setForeground(Color.black);
        stop.setBounds(500,20,100,40);
        TimeScore.add(stop,BorderLayout.AFTER_LINE_ENDS);
    }

    public void close()
    {
        frame.dispose();
    }


    /**
     * This function listen to the stop button
     * if the button clicked the game stops
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == stop)
        {
            game.setStop_the_game(true);
            frame.dispose();
        }
    }
}