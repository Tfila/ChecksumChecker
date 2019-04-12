import org.apache.commons.codec.binary.Hex;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Maria Gallus
 */
public class ChecksumChecker extends JFrame {

    private MessageDigest md;
    private JButton fileChooserButton, compareButton;
    private JTextArea resultText, compareText;
    private JLabel comparationResultLabel, filePathLabel;
    private JComboBox list;
    private String resultString, filePathString, compareString;
    private File file;
    private final int WIDTH = 900;
    private final int HEIGHT = 400;

    public ChecksumChecker() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("ChecksumChecker");
        this.setIconImage(new ImageIcon("src\\main\\resources\\ico_good.png").getImage());
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
        this.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - WIDTH) / 2,
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - HEIGHT) / 2);

        list = new JComboBox();
        list.addItem("Choose algorithm...");
        list.addItem("MD5");
        list.addItem("SHA1");
        list.addItem("SHA256");
        list.addItem("SHA512");
        list.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = list.getSelectedIndex();
                try {
                    switch (index) {
                        case 1:
                            md = MessageDigest.getInstance("MD5");
                            break;
                        case 2:
                            md = MessageDigest.getInstance("SHA1");
                            break;
                        case 3:
                            md = MessageDigest.getInstance("SHA-256");
                            break;
                        case 4:
                            md = MessageDigest.getInstance("SHA-512");
                            break;
                        default:
                            md = null;
                            JOptionPane.showMessageDialog(null, "Choose algorithm", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                } catch (NoSuchAlgorithmException ex) {
                    JOptionPane.showMessageDialog(null, "No such algorithm exception: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        resultText = new JTextArea();
        filePathLabel = new JLabel();
        filePathLabel.setFont(new Font("Tahoma", 0, 12));
        fileChooserButton = new JButton("Choose a file");
        fileChooserButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(fileChooserButton);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                }
                if (null != file && null != md) {
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        byte[] input = new byte[fis.available()];
                        fis.read(input);
                        md.update(input);
                        byte[] output = md.digest();
                        resultString = Hex.encodeHexString(output);
                        resultText.setText(resultString);
                        resultText.setForeground(Color.BLUE);
                        filePathLabel.setText(file.getPath());

                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, "File not found: " + ex, "Błąd", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "IOException: " + ex, "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        compareText = new JTextArea();
        compareText.setEditable(true);
        compareText.setText("Enter value to compare...");
        comparationResultLabel = new JLabel();
        compareButton = new JButton("Compare");
        compareButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!"Enter value to compare...".equals(compareText.getText()) && null != compareText.getText()) {
                    compareString = compareText.getText();
                    if (compareString.equals(resultString)) {
                        comparationResultLabel.setText("correct");
                        comparationResultLabel.setForeground(Color.GREEN);
                    } else {
                        comparationResultLabel.setText("incorrect");
                        comparationResultLabel.setForeground(Color.RED);
                    }
                }
            }
        });
        this.setBackground(Color.LIGHT_GRAY);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(gbl);
        gbc.weighty = 1;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = GridBagConstraints.REMAINDER;
        gbl.setConstraints(list, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = GridBagConstraints.REMAINDER;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbl.setConstraints(fileChooserButton, gbc);
        gbl.setConstraints(resultText, gbc);
        gbl.setConstraints(compareText, gbc);
        gbl.setConstraints(compareButton, gbc);
        gbl.setConstraints(comparationResultLabel, gbc);
        gbl.setConstraints(filePathLabel, gbc);
        this.add(list);
        this.add(fileChooserButton);
        this.add(resultText);
        this.add(compareText);
        this.add(compareButton);
        this.add(comparationResultLabel);
        this.add(filePathLabel);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChecksumChecker();
            }
        });
    }
}