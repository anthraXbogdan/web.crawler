import gui.WebCrawlerGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(() -> {
            WebCrawlerGUI webCrawlerGUI = new WebCrawlerGUI();
            webCrawlerGUI.setVisible(true);
            webCrawlerGUI.setLocationRelativeTo(null);
        });

    }
}
