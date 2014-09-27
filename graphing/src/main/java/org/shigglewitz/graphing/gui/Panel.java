package org.shigglewitz.graphing.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.RandomStringUtils;
import org.shigglewitz.equations.exceptions.InvalidEquationException;
import org.shigglewitz.utils.ImageMaker;
import org.shigglewitz.graphing.ColorGrapher;
import org.shigglewitz.graphing.Grapher;

public class Panel implements ActionListener {
    private static final String LABEL_DEFAULT = "--";

    private JPanel equationPane;
    private JPanel drawPane;
    private JPanel savePane;
    private JPanel labelPane;
    private JButton draw;
    private JButton save;
    private final JTextField redEquation = new JTextField("X");
    private final JTextField greenEquation = new JTextField("Y");
    private final JTextField blueEquation = new JTextField("X^2+Y^2");
    private final JTextField saveFile = new JTextField(
            RandomStringUtils.randomAlphabetic(15));
    private JLabel imageDisplay;
    private final JLabel redTextLabel = new JLabel();
    private final JLabel greenTextLabel = new JLabel();
    private final JLabel blueTextLabel = new JLabel();
    private final JLabel redEquationLabel = new JLabel(LABEL_DEFAULT);
    private final JLabel greenEquationLabel = new JLabel(LABEL_DEFAULT);
    private final JLabel blueEquationLabel = new JLabel(LABEL_DEFAULT);
    private final JLabel saveLabel = new JLabel("File name:");
    private BufferedImage image;

    private ColorGrapher cg;

    private static final Color[] labelColors = { Color.RED, Color.GREEN,
            Color.BLUE };

    // dimensions
    // global
    private static final int GLOBAL_PADDING = 5;
    private static final int INTERNAL_PADDING = 10;

    // interior components
    private static final int TEXT_WIDTH = 100;
    private static final int TEXT_HEIGHT = 25;
    private static final int BUTTON_HEIGHT = TEXT_HEIGHT;
    private static final int BUTTON_WIDTH = 80;
    private static final int LABEL_WIDTH = TEXT_WIDTH;
    private static final int INITIAL_LABEL_WIDTH = 20;
    private static final int LABEL_HEIGHT = TEXT_HEIGHT;

    // panels
    private static final int EQUATION_PANE_WIDTH = INITIAL_LABEL_WIDTH
            + GLOBAL_PADDING + TEXT_WIDTH;
    // 3 is the number of jtextfields, increase if alpha is added
    private static final int EQUATION_PANE_HEIGHT = (INTERNAL_PADDING + TEXT_HEIGHT)
            * 3 + BUTTON_HEIGHT;
    private static final int GRAPH_WIDTH = ColorGrapher.DEFAULT_WIDTH;
    private static final int GRAPH_HEIGHT = ColorGrapher.DEFAULT_HEIGHT;
    private static final int SAVE_PANE_WIDTH = TEXT_WIDTH * 2;
    private static final int SAVE_PANE_HEIGHT = TEXT_HEIGHT * 3
            + INTERNAL_PADDING * 2;
    private static final int LABEL_PANE_WIDTH = EQUATION_PANE_WIDTH
            + GRAPH_WIDTH + SAVE_PANE_WIDTH + (GLOBAL_PADDING * 4);
    private static final int LABEL_PANE_HEIGHT = TEXT_HEIGHT;

    public JPanel createContentPane() {
        // We create a bottom JPanel to place everything on.
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);

        this.initializeEquationPane(totalGUI);
        this.initializeDrawPane(totalGUI);
        this.initializeSavePane(totalGUI);
        this.initializeLabelPane(totalGUI);

        totalGUI.setOpaque(true);
        return totalGUI;
    }

    private void initializeEquationPane(JPanel totalGUI) {
        JTextField[] equationFields = { this.redEquation, this.greenEquation,
                this.blueEquation };
        JLabel[] textLabels = { this.redTextLabel, this.greenTextLabel,
                this.blueTextLabel };
        String[] labelInitials = { "R:", "G:", "B:" };

        // Creation of a Panel to contain the equation inputs
        this.equationPane = new JPanel();
        this.equationPane.setLayout(null);
        this.equationPane.setLocation(GLOBAL_PADDING, GLOBAL_PADDING);
        this.equationPane.setSize(EQUATION_PANE_WIDTH, EQUATION_PANE_HEIGHT);
        totalGUI.add(this.equationPane);

        for (int i = 0; i < textLabels.length; i++) {
            JLabel temp = textLabels[i];
            temp.setText(labelInitials[i]);
            temp.setLocation(0, i * (TEXT_HEIGHT + INTERNAL_PADDING));
            temp.setSize(INITIAL_LABEL_WIDTH, LABEL_HEIGHT);
            temp.setHorizontalAlignment(SwingConstants.LEFT);
            temp.setForeground(labelColors[i]);
            this.equationPane.add(temp);
        }

        for (int i = 0; i < equationFields.length; i++) {
            JTextField temp = equationFields[i];
            temp.setLocation(INITIAL_LABEL_WIDTH + GLOBAL_PADDING, i
                    * (TEXT_HEIGHT + INTERNAL_PADDING));
            temp.setSize(TEXT_WIDTH, TEXT_HEIGHT);
            temp.setHorizontalAlignment(SwingConstants.LEFT);
            this.equationPane.add(temp);
        }

        this.draw = new JButton("Draw");
        this.draw.setMnemonic((int) 'D');
        this.draw.setLocation(
                (this.equationPane.getWidth() - BUTTON_WIDTH) / 2,
                equationFields.length * (TEXT_HEIGHT + INTERNAL_PADDING));
        this.draw.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        this.draw.addActionListener(this);
        this.equationPane.add(this.draw);
    }

    private void initializeDrawPane(JPanel totalGUI) {
        // Creation of a Panel to contain the graph.
        this.drawPane = new JPanel();
        this.drawPane.setLayout(null);
        this.drawPane.setLocation(this.equationPane.getWidth()
                + this.equationPane.getX() + GLOBAL_PADDING, GLOBAL_PADDING);
        this.drawPane.setSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        totalGUI.add(this.drawPane);

        this.imageDisplay = new JLabel();
        this.imageDisplay.setLocation(0, 0);
        this.imageDisplay.setSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        this.displayImage(this.generateRandomImage());
        this.drawPane.add(this.imageDisplay);
    }

    private void initializeSavePane(JPanel totalGUI) {
        // Creation of a Panel to contain save options
        this.savePane = new JPanel();
        this.savePane.setLayout(null);
        this.savePane.setLocation(
                this.drawPane.getWidth() + this.drawPane.getX()
                        + GLOBAL_PADDING, GLOBAL_PADDING);
        this.savePane.setSize(SAVE_PANE_WIDTH, SAVE_PANE_HEIGHT);
        totalGUI.add(this.savePane);

        this.saveLabel.setLocation(0, 0);
        this.saveLabel.setSize(LABEL_WIDTH, LABEL_HEIGHT);
        this.savePane.add(this.saveLabel);

        this.saveFile.setLocation(0, TEXT_HEIGHT + INTERNAL_PADDING);
        this.saveFile.setSize(TEXT_WIDTH * 2, TEXT_HEIGHT);
        this.savePane.add(this.saveFile);

        this.save = new JButton("Save");
        this.save.setMnemonic((int) 'S');
        this.save.setLocation((this.savePane.getWidth() - BUTTON_WIDTH) / 2,
                (INTERNAL_PADDING * 2) + TEXT_HEIGHT + LABEL_HEIGHT);
        this.save.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        this.save.addActionListener(this);
        this.savePane.add(this.save);
    }

    private void initializeLabelPane(JPanel totalGUI) {
        JLabel[] equationLabels = { this.redEquationLabel,
                this.greenEquationLabel, this.blueEquationLabel };

        // Creation of a Panel to contain equation labels
        this.labelPane = new JPanel();
        this.labelPane.setLayout(null);
        this.labelPane.setLocation(GLOBAL_PADDING, this.drawPane.getHeight()
                + this.drawPane.getY() + GLOBAL_PADDING);
        this.labelPane.setSize(LABEL_PANE_WIDTH, LABEL_PANE_HEIGHT);
        totalGUI.add(this.labelPane);

        for (int i = 0; i < equationLabels.length; i++) {
            JLabel temp = equationLabels[i];
            temp.setForeground(labelColors[i]);
            temp.setLocation(
                    i
                            * (((LABEL_PANE_WIDTH - (GLOBAL_PADDING * 2)) / 3) + GLOBAL_PADDING),
                    0);
            temp.setSize((LABEL_PANE_WIDTH - (GLOBAL_PADDING * 2)) / 3,
                    LABEL_HEIGHT);
            temp.setHorizontalAlignment(SwingConstants.CENTER);
            this.labelPane.add(temp);
        }
    }

    private BufferedImage generateRandomImage() {
        return ImageMaker.randomImage(GRAPH_WIDTH, GRAPH_HEIGHT);
    }

    private void displayImage(BufferedImage image) {
        this.image = image;
        this.imageDisplay.setIcon(new ImageIcon(image));
    }

    private void saveImage(String fileName) throws IOException {
        ImageMaker.saveImage(this.image, fileName);
    }

    private static void createAndShowGUI() {

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("[=] Color Graphing [=]");

        // Create and set up the content pane.
        Panel demo = new Panel();
        frame.setContentPane(demo.createContentPane());
        frame.getContentPane().setPreferredSize(
                new Dimension(LABEL_PANE_WIDTH + GLOBAL_PADDING * 2,
                        GLOBAL_PADDING * 3 + GRAPH_HEIGHT + LABEL_PANE_HEIGHT));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == this.draw) {
            this.clickDraw();
        } else if (source == this.save) {
            this.clickSave();
        }
    }

    private void clickDraw() {
        try {
            this.cg = new ColorGrapher(this.redEquation.getText(),
                    this.greenEquation.getText(), this.blueEquation.getText());
            this.displayImage(this.cg.getGraph(Grapher.DEFAULT_WIDTH,
                    Grapher.DEFAULT_HEIGHT));
            this.redEquationLabel.setText(this.cg.getRedEquation());
            this.greenEquationLabel.setText(this.cg.getGreenEquation());
            this.blueEquationLabel.setText(this.cg.getBlueEquation());
        } catch (InvalidEquationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Equation error!", JOptionPane.PLAIN_MESSAGE);
            this.displayImage(this.generateRandomImage());
            this.redEquationLabel.setText(LABEL_DEFAULT);
            this.greenEquationLabel.setText(LABEL_DEFAULT);
            this.blueEquationLabel.setText(LABEL_DEFAULT);
        }
    }

    private void clickSave() {
        try {
            this.saveImage(this.saveFile.getText());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Saving Error",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }
}
