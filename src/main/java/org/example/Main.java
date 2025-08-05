package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public JLabel status;

    public static void main(String[] args) {
        Main m = new Main();
        m.run();
    }

    private void run() {
        //--- CREATE GUI ---

        JFrame frame = new JFrame("JavaVFV");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        //--- CREATE GUI END ---

        //--- CREATE TEXT AREA ---

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //--- CREATE TEXT AREA END ---

        //--- CREATE OPEN FILE BUTTON AND STATUS TEXT ---

        status = new JLabel("Status: ");

        frame.add(status, BorderLayout.SOUTH);

        changeStatusText("Select a file to open.");

        JButton openButton = getJButton(frame, textArea);

        //--- CREATE OPEN FILE BUTTON AND STATUS TEXT END ---

        //--- CREATE PANELS ---

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(openButton);

        //--- CREATE PANELS END ---

        //--- CREATE CLEAR BUTTON ---

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener((ActionEvent _) -> {
            textArea.setText("");
            changeStatusText("Viewer cleared");
        });
        topPanel.add(clearButton);

        //--- CREATE CLEAR BUTTON END

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

        //--- CREATE TOP CONTROLLER PANEL END ---
    }

    private void changeStatusText(String newText) {
        status.setText(newText);
    }

    private JButton getJButton(JFrame frame, JTextArea textArea) {
        JButton openButton = new JButton("Open File");
        openButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    textArea.setText("");

                    SimpleDateFormat sdf = new SimpleDateFormat();
                    String formattedDate = sdf.format(new Date(selectedFile.lastModified()));
                    changeStatusText("File loaded: " + selectedFile.getName() + "      File Size: " + getFileSize(selectedFile) + "      Last Modified: " + formattedDate);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        textArea.append(line + "\n");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Error reading file: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    changeStatusText("Error while opening file: " + ex.getMessage());
                }
            } else {
                changeStatusText("Open file cancelled");
            }

        });
        return openButton;
    }

    public String getFileSize(File f) {
        long s = f.length();
        if (s < 1024) { // If file is less than 1KB
            return s + " B";
        } else if (s > 1024 && s < (1048576)) { // If file is more than 1KB and less than 1MB
            return (double) (s / 1024) + "KB";
        } else if (s > 1048576 && s < 1073741824) { // If file is more than 1MB and less than 1GB
            return (double) (s / 1048576) + "MB";
        } else { // If file is more than 1GB
            return (double) (s / 1073741824) + "GB";
        }
    }


}