package gui;

import data.management.FileSaver;
import links.parsing.*;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLHandshakeException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawlerGUI extends JFrame {

    private JPanel rootPanel;
    private JTextField urlTextField;
    private JButton runButton;
    private JButton clearButton;
    private JLabel urlLabel;
    private JTextArea htmlTextArea;
    private JLabel titleLabel;
    private JLabel webPageTitle;
    private JScrollPane scrollPane;
    private JTable titlesTable;
    private JTextField exportUrlTextField;
    private JButton saveButton;
    private JLabel exportLabel;
    private JProgressBar progressBar;

    static String url;
    static String trimmedURL;
    static String protocol;

    ArrayList<String> rawLinksList = new ArrayList<>();
    ArrayList<String> relativeLinksList = new ArrayList<>();
    ArrayList<String> absoluteLinksList = new ArrayList<>();
    ArrayList<Object> validLinks = new ArrayList<>();
    ArrayList<String> validURLs = new ArrayList<>();
    ArrayList<String> validTitles = new ArrayList<>();

    public static String getFullLink(String rawLink) {
        String cleanURL = "";

        if (SlashChecker.check(rawLink)) {
            Pattern p1 = Pattern.compile(protocol + "//.*");
            Matcher m1 = p1.matcher(rawLink);

            Pattern p2 = Pattern.compile("//.*");
            Matcher m2 = p2.matcher(rawLink);

            Pattern p3 = Pattern.compile("/.*");
            Matcher m3 = p3.matcher(rawLink);


            if (m1.find()) {
                cleanURL = rawLink;
            }
            else if (m2.find()) {
                cleanURL = protocol + rawLink;
            }
            else if (m3.find()) {
                cleanURL = trimmedURL + rawLink;
            }
        } else {
            cleanURL = trimmedURL + "/" + rawLink;
        }

        return cleanURL;
    }

    public WebCrawlerGUI() {
        Color background = new Color(59, 59, 59);
        Color myBlue = new Color(43, 125, 159);

        add(rootPanel);
        setTitle("Web Crawler");
        setSize(800, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        urlLabel.setFont(new Font("myFont", Font.PLAIN, 12));
        titleLabel.setFont(new Font("myFont", Font.PLAIN, 12));

        urlTextField.setFont(new Font("myFont", Font.PLAIN, 12));
        urlTextField.setBorder(new LineBorder(myBlue));
        urlTextField.setForeground(myBlue);

        exportUrlTextField.setFont(new Font("myFont", Font.PLAIN, 12));
        exportUrlTextField.setBorder(new LineBorder(myBlue));
        exportUrlTextField.setForeground(myBlue);

        DefaultTableModel tableModel = new DefaultTableModel();
        titlesTable.setModel(tableModel);
        titlesTable.setRowHeight(20);
        titlesTable.setFont(new Font("myFont", Font.PLAIN, 12));

        JTableHeader jTableHeader = titlesTable.getTableHeader();
        jTableHeader.setOpaque(false);
        jTableHeader.setBackground(Color.gray);
        jTableHeader.setForeground(Color.WHITE);
        jTableHeader.setFont(new Font("myFont", Font.PLAIN, 14));
        jTableHeader.setVisible(true);

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) titlesTable.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(0);

        String[] columnsNames = {"URL", "Title"};
        tableModel.setColumnIdentifiers(columnsNames);

        progressBar.setString("");
        progressBar.setMinimum(0);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_C);

        JMenu newMenuItem = new JMenu("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem closeMenuItem = new JMenuItem("Close");
        JMenuItem closeAllMenuItem = new JMenuItem("Close All");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        JMenuItem textFileMenuItem = new JMenuItem("Text File");
        JMenuItem imgFileMenuItem = new JMenuItem("Image File");
        JMenuItem folderMenuItem = new JMenuItem("Folder");

        // you can rewrite it with a lambda if you prefer this
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        newMenuItem.add(textFileMenuItem);
        newMenuItem.add(imgFileMenuItem);
        newMenuItem.add(folderMenuItem);

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(closeMenuItem);
        fileMenu.add(closeAllMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        //Create a file chooser
        FileNameExtensionFilter ff = new FileNameExtensionFilter("Text File *.txt", ".txt");
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
        fileChooser.setFileFilter(ff);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setSelectedFile(new File("myFile.txt"));


        ActionListener activateParsing = e -> {
            runButton.setEnabled(false);
            //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //turn on the waith cursor
            DataParser dataParser = new DataParser(tableModel);
            dataParser.execute();
        };

        ActionListener tableClearer = e -> {
            urlTextField.setText("");
            webPageTitle.setText("");
            tableModel.setRowCount(0);
            progressBar.setValue(0);
            progressBar.setString("");
        };

        ActionListener dataExporter = f -> {
            saveButton.setEnabled(false);
            int returnVal = fileChooser.showDialog(null, "Save");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String fileName = fileChooser.getSelectedFile().toString();
                if (!fileName.endsWith(".txt")) {
                    fileName = fileChooser.getSelectedFile().toString() + ".txt";
                    fileChooser.setSelectedFile(new File(fileName));
                }
            }

            try {
                FileSaver fileSaver = new FileSaver(validURLs, validTitles, fileChooser);
                fileSaver.writeToFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            saveButton.setEnabled(true);
        };

        runButton.addActionListener(activateParsing);
        clearButton.addActionListener(tableClearer);
        saveButton.addActionListener(dataExporter);

    }

    class DataParser extends SwingWorker<Void, Integer> {
        DefaultTableModel tableModel;

        public DataParser(DefaultTableModel tableModel) {
            this.tableModel = tableModel;
        }

        @Override
        public Void doInBackground() throws Exception {
            url = urlTextField.getText();
            url = url.substring(0, url.length()-1);
            Pattern urlPattern = Pattern.compile("https?://.*\\..*");
            Matcher urlMatcher = urlPattern.matcher(urlTextField.getText());

            if (urlMatcher.find()) {
                url = urlTextField.getText();
                protocol = ProtocolSetter.setProtocol(url);
                trimmedURL = LinkTrimming.trimming(url);
            } else {
                urlTextField.setText("Introduced URL is incorrect! Please try again!");
            }

            //Collect all the links present in current HTML page.
            try {
                URL siteURL = new URL(url);
                URLConnection urlConnection = siteURL.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");

                if (urlConnection.getContentType().startsWith("text/html")) {
                    System.out.println(urlConnection.getContentType());

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    String siteText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                    Pattern p1 = Pattern.compile(".*href=['|\"].+['|\"][\\s>].*");
                    Matcher m1 = p1.matcher(siteText);
                    while (m1.find()) {
                        rawLinksList.add(m1.group());
                    }

                    for (String str : rawLinksList) {
                        Pattern rawLink = Pattern.compile("(?<=href=['\"])[\\d\\w\\S]+(?=['\"])");
                        Matcher matcher = rawLink.matcher(str);
                        if (matcher.find()) {
                            relativeLinksList.add(matcher.group());
                        }

                    }
                }
            } catch (MalformedURLException m) {
                urlTextField.setText("Introduced URL is incorrect! Please try again!");
            } catch (IOException ioException) {
                urlTextField.setText("Data request from this url could not be performed! Please check the url validity!");
            }


            for (String s : relativeLinksList) {
                absoluteLinksList.add(getFullLink(s));
            }

            progressBar.setMaximum(absoluteLinksList.size()-1);

            for (int i = 0; i < absoluteLinksList.size(); i++) {
                publish(i);
                String title = "";
                if (LinkValidityChecker.checker(absoluteLinksList.get(i))) {
                    title = LinkTitleReturner.returner(absoluteLinksList.get(i));
                    if (!title.equals("")) {
                        validLinks.add(new Object[]{absoluteLinksList.get(i), title});
                        validURLs.add(absoluteLinksList.get(i));
                        validTitles.add(title);
                    } else {
                        title = "No title";
                        validLinks.add(new Object[]{absoluteLinksList.get(i), title});
                        validURLs.add(absoluteLinksList.get(i));
                        validTitles.add(title);
                    }
                }
            }
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            //System.out.println(chunks.get(chunks.size()-1));
            //System.out.println("Links array size is: " + absoluteLinksList.size());
            progressBar.setValue(chunks.get(chunks.size()-1));
            progressBar.setString("Links parsed: " + chunks.get(chunks.size()-1) + "/" + (absoluteLinksList.size()-1));

        }

        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            //setCursor(null); //turn off the wait cursor
            progressBar.setValue(0);

            for (Object obj : validLinks) {
                tableModel.addRow((Object[]) obj);
            }

            runButton.setEnabled(true);
            webPageTitle.setText(WebsiteTitleGetter.getTitle(url));
            rawLinksList.clear();
            relativeLinksList.clear();
            absoluteLinksList.clear();
            validLinks.clear();
        }
    }

}
