package com.github.axet.starjeweled.ui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.github.axet.starjeweled.core.Capture;

public class Bounds2Pane extends JPanel {

    private static final long serialVersionUID = -775581665000921461L;

    ImageIcon icon;

    MainFrame frame;

    ImagePane image = new ImagePane();
    JPanel controls = new JPanel();

    Checkbox debug = new Checkbox("Debug");
    JButton capture = new JButton("Capture");
    JButton step = new JButton("Step");
    JButton validate = new JButton("Validate");
    JButton slowRun = new JButton("Slow Run");
    JLabel scaleLabel = new JLabel("Scale");
    JTextField scaleField = new JTextField("1.0");

    JLabel xLabel = new JLabel("X:");
    JTextField xField = new JTextField("0");

    JLabel yLabel = new JLabel("Y:");
    JTextField yField = new JTextField("0");
    private final JTextPane output = new JTextPane();
    private final JLabel lblOutput = new JLabel("Output:");
    private final JButton btnNewButton = new JButton("Load");

    public Bounds2Pane() {
        super(new BorderLayout());
        GridBagLayout gbl_controls = new GridBagLayout();
        gbl_controls.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
        gbl_controls.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 0.0 };
        controls.setLayout(gbl_controls);

        GridBagConstraints gbc_debug = new GridBagConstraints();
        gbc_debug.anchor = GridBagConstraints.WEST;
        gbc_debug.insets = new Insets(0, 0, 5, 5);
        gbc_debug.gridx = 1;
        gbc_debug.gridy = 0;
        debug.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                debug(arg0.getStateChange() == ItemEvent.SELECTED);
            }
        });
        controls.add(debug, gbc_debug);
        GridBagConstraints gbc_capture = new GridBagConstraints();
        gbc_capture.fill = GridBagConstraints.BOTH;
        gbc_capture.insets = new Insets(0, 0, 5, 5);
        gbc_capture.gridx = 3;
        gbc_capture.gridy = 0;
        controls.add(capture, gbc_capture);

        capture.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                image.setImage(frame.capture.getImage());
                image.invalidate();
                validate();
            }
        });

        GridBagConstraints gbc_lblOutput = new GridBagConstraints();
        gbc_lblOutput.insets = new Insets(0, 0, 5, 0);
        gbc_lblOutput.gridx = 4;
        gbc_lblOutput.gridy = 0;
        controls.add(lblOutput, gbc_lblOutput);

        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.fill = GridBagConstraints.BOTH;
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton.gridx = 3;
        gbc_btnNewButton.gridy = 1;
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(Bounds2Pane.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    image.setImage(Capture.load(file));
                    debug.setState(true);
                    debug(debug.getState());
                }
            }
        });
        controls.add(btnNewButton, gbc_btnNewButton);

        GridBagConstraints gbc_scaleLabel = new GridBagConstraints();
        gbc_scaleLabel.fill = GridBagConstraints.BOTH;
        gbc_scaleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_scaleLabel.gridx = 1;
        gbc_scaleLabel.gridy = 2;
        controls.add(scaleLabel, gbc_scaleLabel);
        GridBagConstraints gbc_scaleField = new GridBagConstraints();
        gbc_scaleField.fill = GridBagConstraints.HORIZONTAL;
        gbc_scaleField.anchor = GridBagConstraints.NORTH;
        gbc_scaleField.insets = new Insets(0, 0, 5, 5);
        gbc_scaleField.gridx = 2;
        gbc_scaleField.gridy = 2;
        controls.add(scaleField, gbc_scaleField);

        add(image, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        GridBagConstraints gbc_output = new GridBagConstraints();
        gbc_output.weighty = 1.0;
        gbc_output.weightx = 1.0;
        gbc_output.gridheight = 4;
        gbc_output.fill = GridBagConstraints.BOTH;
        gbc_output.gridx = 4;
        gbc_output.gridy = 2;
        controls.add(output, gbc_output);
        GridBagConstraints gbc_xLabel = new GridBagConstraints();
        gbc_xLabel.anchor = GridBagConstraints.WEST;
        gbc_xLabel.insets = new Insets(0, 0, 5, 5);
        gbc_xLabel.gridx = 1;
        gbc_xLabel.gridy = 3;
        controls.add(xLabel, gbc_xLabel);
        GridBagConstraints gbc_xField = new GridBagConstraints();
        gbc_xField.anchor = GridBagConstraints.NORTHWEST;
        gbc_xField.insets = new Insets(0, 0, 5, 5);
        gbc_xField.gridx = 2;
        gbc_xField.gridy = 3;
        controls.add(xField, gbc_xField);
        GridBagConstraints gbc_yLabel = new GridBagConstraints();
        gbc_yLabel.anchor = GridBagConstraints.WEST;
        gbc_yLabel.insets = new Insets(0, 0, 5, 5);
        gbc_yLabel.gridx = 1;
        gbc_yLabel.gridy = 4;
        controls.add(yLabel, gbc_yLabel);
        GridBagConstraints gbc_yField = new GridBagConstraints();
        gbc_yField.anchor = GridBagConstraints.NORTHWEST;
        gbc_yField.insets = new Insets(0, 0, 5, 5);
        gbc_yField.gridx = 2;
        gbc_yField.gridy = 4;
        controls.add(yField, gbc_yField);

        GridBagConstraints gbc_validate = new GridBagConstraints();
        gbc_validate.fill = GridBagConstraints.BOTH;
        gbc_validate.insets = new Insets(0, 0, 0, 5);
        gbc_validate.gridx = 1;
        gbc_validate.gridy = 5;
        controls.add(validate, gbc_validate);
        GridBagConstraints gbc_step = new GridBagConstraints();
        gbc_step.fill = GridBagConstraints.HORIZONTAL;
        gbc_step.anchor = GridBagConstraints.NORTH;
        gbc_step.insets = new Insets(0, 0, 0, 5);
        gbc_step.gridx = 2;
        gbc_step.gridy = 5;
        controls.add(step, gbc_step);
        GridBagConstraints gbc_slowRun = new GridBagConstraints();
        gbc_slowRun.fill = GridBagConstraints.HORIZONTAL;
        gbc_slowRun.anchor = GridBagConstraints.NORTH;
        gbc_slowRun.insets = new Insets(0, 0, 0, 5);
        gbc_slowRun.gridx = 3;
        gbc_slowRun.gridy = 5;
        controls.add(slowRun, gbc_slowRun);

        debug(debug.getState());
    }

    void debug(boolean b) {
        Component[] cc = new Component[] { capture, step, validate, slowRun, scaleLabel, scaleField, xField };
        for (Component c : cc) {
            c.setEnabled(b);
        }
    }

    public void setImage(BufferedImage image) {
        if (debug.getState())
            return;

        this.image.setImage(image);

        repaint();
    }

}
