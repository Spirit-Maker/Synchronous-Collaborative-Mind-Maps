/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindmap.project;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import mindmap.project.MainFrame;

/**
 *
 * @author Shani Bhai
 */
public class FYP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        createSplashScreen();
        MainFrame frame = new MainFrame("Synchronus Collaborative Mind Maps");
    }

    public static void createSplashScreen() {
        JWindow window = new JWindow();
        window.setBackground(new Color(0, 0, 0, 0));
        window.getContentPane().add(
                new JLabel("", new ImageIcon("src/resources/images/SplashLogo.gif"), SwingConstants.CENTER));
        window.setBounds(500, 120, 560, 355);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        window.setVisible(false);
        window.dispose();
    }
}
