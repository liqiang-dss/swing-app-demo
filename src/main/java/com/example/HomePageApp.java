package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HomePageApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePageApp::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 200);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        JLabel successLabel = new JLabel("验证成功");
        successLabel.setName("successLabel");
        panel.add(successLabel, c);

        c.gridy = 1;
        String user = GlobalContext.getCurrentUser();
        JLabel welcome = new JLabel("欢迎 " + (user == null ? "" : user));
        welcome.setName("welcomeLabel");
        panel.add(welcome, c);

        c.gridy = 2; c.gridwidth = 1; c.gridx = 0;
        JButton logout = new JButton("登出");
        logout.setName("logoutButton");
        panel.add(logout, c);

        c.gridx = 1;
        JButton close = new JButton("关闭");
        close.setName("closeButton");
        panel.add(close, c);

        frame.add(panel, BorderLayout.CENTER);

        logout.addActionListener((ActionEvent e) -> {
            GlobalContext.clear();
            frame.dispose();
            SwingUtilities.invokeLater(LoginApp::createAndShowGUI);
        });

        close.addActionListener((ActionEvent e) -> {
            frame.dispose();
            System.exit(0);
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
