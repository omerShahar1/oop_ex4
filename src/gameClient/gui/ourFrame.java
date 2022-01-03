package gameClient.gui;

import gameClient.Arena;

import javax.swing.*;

/**
 * this class is expending JFrame.
 * ourFrame is basically the frame of the GUI game.
 * the frame holds a panel which is running every graphic object inside it.
 */
public class ourFrame extends JFrame {
    Arena ManageGame;
    ourPanel panel;

    /**
     * a default constructor.
     */
    public ourFrame() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * init the frame and the panel.
     * also this is the connector between the logical and the graphical.
     *
     * @param ar the Arena.
     */
    public void initFrame(Arena ar) {
        ManageGame = ar;
        initPanel();
    }

    /**
     * init the panel.
     */
    public void initPanel() {
        panel = new ourPanel(ManageGame);
        this.add(panel);
    }

    /**
     * @return ourPanel type.
     */
    public ourPanel getPanel() {
        return panel;
    }

}