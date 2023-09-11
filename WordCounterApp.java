import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;


public class WordCounterApp {
    private JFrame frame;
    private JPanel startPanel;
    private JPanel mainPanel;
    private JTextArea textArea;
    private JLabel wordCountLabel;
    private JLabel letterCountLabel;
    private JLabel lineCountLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WordCounterApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        // initializing the Swing Frame
        frame = new JFrame("Word Counter");
        ImageIcon image = new ImageIcon("2.png");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setIconImage(image.getImage());

        // calling the start panel function
        startPanel = createStartPanel();
        // calling the main panel function
        mainPanel = createMainPanel();

        frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        startPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setBackground(Color.LIGHT_GRAY);

        // adding the panels to the frame
        frame.setLayout(new CardLayout());
        frame.add(startPanel, "start");
        frame.add(mainPanel, "main");

        frame.setVisible(true);
    }

    // initializing the start panel which has the logo and 3 buttons
    private JPanel createStartPanel() {
        JPanel startPanel = new JPanel(new BorderLayout());
        startPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        ImageIcon logoIcon = new ImageIcon("2.png");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        // start button
        JButton startButton = new JButton("Start");
        // after clicking the start button user is directed to the main panel where the counter resides
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "main");
                textArea.requestFocusInWindow();
            }
        });

        // count from file button 
        // this button is used so that the user can count the no. of lines, letters, etc .
        // from a already existing file in the computer
        //  
        JButton countFromFileButton = new JButton("Count from File");
        countFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // initializing the file chooser package which helps to choose the file from the computer
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);

                // after selecting the file the below part of the program is used to print 
                // the contents of the file i the main panel's text area
                if (result == JFileChooser.APPROVE_OPTION) {

                    try {
                        StringBuilder content = new StringBuilder();
                        BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                        reader.close();
                        textArea.setText(content.toString());

                        CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                        cardLayout.show(frame.getContentPane(), "main");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });



         // count from file button 
        // this button is used so that the user can count the no. binary values in the image
        // from a already existing png/jpg file in the computer
        JButton countBinaryButton = new JButton("Count Binary Values");
        countBinaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    countBinaryValues(selectedFile);
                }
            }
        });

        // placing the buttons on the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.add(startButton);
        buttonPanel.add(countFromFileButton);
        buttonPanel.add(countBinaryButton); // Add the Count Binary Values button here

        startPanel.add(logoLabel, BorderLayout.NORTH);
        startPanel.add(buttonPanel, BorderLayout.CENTER);

        return startPanel;
    }



    // initializing the main panel which has the text area and the count function
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // initializig the rext area
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // adding the document listener function to identify the changes made to
        // the text area so that we can update the counter
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCounts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCounts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCounts();
            }
        });

        // adding scroll bar to the text area
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // adding back button to the main panel
        JButton backButton = new JButton("Back to Start");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "start");
                textArea.setText("");
            }
        });

        // JButton countBinaryButton = new JButton("Count Binary Values");
        // countBinaryButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         JFileChooser fileChooser = new JFileChooser();
        //         int result = fileChooser.showOpenDialog(frame);

        //         if (result == JFileChooser.APPROVE_OPTION) {
        //             File selectedFile = fileChooser.getSelectedFile();
        //             countBinaryValues(selectedFile);
        //         }
        //     }
        // });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(backButton);
        // buttonPanel.add(countBinaryButton);

        JPanel countsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        countsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // addind label for the counter
        wordCountLabel = new JLabel("Words: 0");
        letterCountLabel = new JLabel("Letters: 0");
        lineCountLabel = new JLabel("Lines: 0");

        Font countsFont = new Font("Arial", Font.BOLD, 18);
        wordCountLabel.setFont(countsFont);
        letterCountLabel.setFont(countsFont);
        lineCountLabel.setFont(countsFont);

        countsPanel.add(wordCountLabel);
        countsPanel.add(letterCountLabel);
        countsPanel.add(lineCountLabel);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(countsPanel, BorderLayout.NORTH);

        return mainPanel;
    }

    // function for counting the binary values in an image 
    private void countBinaryValues(File imageFile) {
        BufferedImage image = loadImageFromFile(imageFile);
        int binaryCount = 0;

        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;

                    if (red == 0 || red == 255) {
                        binaryCount++;
                    }
                    if (green == 0 || green == 255) {
                        binaryCount++;
                    }
                    if (blue == 0 || blue == 255) {
                        binaryCount++;
                    }
                }
            }
            // dialog box to show the binary value
            JOptionPane.showMessageDialog(frame, "Binary Value Count of the image: " + binaryCount);
        }
    }

    private BufferedImage loadImageFromFile(File imageFile) {
        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // function to update the count of letters,word,lines
    private void updateCounts() {
        String text = textArea.getText();
        int wordCount = text.split("\\s+").length;
        int letterCount = text.replaceAll("\\s+", "").length();
        int lineCount = text.split("\n").length;

        wordCountLabel.setText("Words: " + wordCount);
        letterCountLabel.setText("Letters: " + letterCount);
        lineCountLabel.setText("Lines: " + lineCount);
    }
}
