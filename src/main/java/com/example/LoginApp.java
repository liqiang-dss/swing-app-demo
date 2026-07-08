package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class LoginApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(320, 180);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("用户名:"), c);
        c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.WEST;
        JTextField username = new JTextField(15);
        username.setName("username");
        panel.add(username, c);

        c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("密码:"), c);
        c.gridx = 1; c.gridy = 1; c.anchor = GridBagConstraints.WEST;
        JPasswordField password = new JPasswordField(15);
        password.setName("password");
        panel.add(password, c);

        JButton loginButton = new JButton("登录");
        loginButton.setName("loginButton");
        c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, c);

        frame.add(panel, BorderLayout.CENTER);

        final BackendApiClient apiClient = new BackendApiClient("http://localhost:8000");

        loginButton.addActionListener((ActionEvent e) -> {
            String user = username.getText();
            String pass = new String(password.getPassword());
            // call backend /login via BackendApiClient in background thread
            loginButton.setEnabled(false);
            new Thread(() -> {
                try {
                    BackendApiClient.LoginResponse loginResp = apiClient.login(user, pass);
                    SwingUtilities.invokeLater(() -> {
                        loginButton.setEnabled(true);
                        if (loginResp != null && loginResp.isSuccess()) {
                            GlobalContext.setCurrentUser(user);
                            frame.dispose();
                            SwingUtilities.invokeLater(HomePageApp::createAndShowGUI);
                        } else {
                            String message = "验证失败";
                            if (loginResp != null && loginResp.message != null) {
                                message = message + "： " + loginResp.message;
                            }
                            JOptionPane.showMessageDialog(frame, message);
                        }
                    });
                } catch (IOException | InterruptedException ex) {
                    SwingUtilities.invokeLater(() -> {
                        loginButton.setEnabled(true);
                        JOptionPane.showMessageDialog(frame, "网络错误: " + ex.getMessage());
                    });
                }
            }).start();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    
}
