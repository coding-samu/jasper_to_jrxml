import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Jasper → JRXML Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 250);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(4, 3, 5, 5));

        JTextField jasperPathField = new JTextField();
        JButton browseJasperButton = new JButton("Scegli .jasper");

        JTextField destDirField = new JTextField();
        JButton browseDirButton = new JButton("Scegli cartella");

        JTextField fileNameField = new JTextField("report_output");

        JButton convertButton = new JButton("Converti");
        JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);

        // Sfoglia file .jasper
        browseJasperButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setDialogTitle("Seleziona file .jasper");
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                jasperPathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        browseDirButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogTitle("Seleziona cartella di destinazione");
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                destDirField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        convertButton.addActionListener(e -> {
            String jasperPath = jasperPathField.getText().trim();
            String destDir = destDirField.getText().trim();
            String fileName = fileNameField.getText().trim();

            if (jasperPath.isEmpty() || destDir.isEmpty() || fileName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                File jasperFile = new File(jasperPath);
                JasperReport report = (JasperReport) JRLoader.loadObject(jasperFile);

                String jrxmlPath = destDir + File.separator + fileName + ".jrxml";
                JRXmlWriter.writeReport(report, jrxmlPath, "UTF-8");

                statusLabel.setText("✅ Conversione completata: " + jrxmlPath);
            } catch (JRException ex) {
                statusLabel.setText("❌ Errore durante la conversione: " + ex.getMessage());
            }
        });

        panel.add(new JLabel("File .jasper:"));
        panel.add(jasperPathField);
        panel.add(browseJasperButton);

        panel.add(new JLabel("Cartella di destinazione:"));
        panel.add(destDirField);
        panel.add(browseDirButton);

        panel.add(new JLabel("Nome file JRXML (senza estensione):"));
        panel.add(fileNameField);
        panel.add(new JLabel(""));

        frame.add(panel, BorderLayout.CENTER);
        frame.add(convertButton, BorderLayout.SOUTH);
        frame.add(statusLabel, BorderLayout.NORTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
