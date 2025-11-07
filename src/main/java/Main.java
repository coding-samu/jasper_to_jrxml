import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import java.io.File;

public class Main extends Application {

    private TextField jasperPathField;
    private TextField destDirField;
    private TextField fileNameField;
    private Label statusLabel;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Jasper → JRXML Converter");

        jasperPathField = new TextField();
        destDirField = new TextField();
        fileNameField = new TextField("report_output");
        statusLabel = new Label("");

        Button browseJasperButton = new Button("Sfoglia...");
        Button browseDirButton = new Button("Sfoglia...");
        Button convertButton = new Button("Converti");

        browseJasperButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleziona file .jasper");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("File Jasper (*.jasper)", "*.jasper")
            );
            File selected = fileChooser.showOpenDialog(stage);
            if (selected != null) {
                jasperPathField.setText(selected.getAbsolutePath());
            }
        });

        browseDirButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Seleziona cartella di destinazione");
            File selectedDir = directoryChooser.showDialog(stage);
            if (selectedDir != null) {
                destDirField.setText(selectedDir.getAbsolutePath());
            }
        });

        convertButton.setOnAction(e -> convertReport());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("File .jasper:"), 0, 0);
        grid.add(jasperPathField, 1, 0);
        grid.add(browseJasperButton, 2, 0);

        grid.add(new Label("Cartella destinazione:"), 0, 1);
        grid.add(destDirField, 1, 1);
        grid.add(browseDirButton, 2, 1);

        grid.add(new Label("Nome file JRXML:"), 0, 2);
        grid.add(fileNameField, 1, 2);

        VBox root = new VBox(15, statusLabel, grid, convertButton);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);

        root.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 14px; ");
        convertButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; ");

        statusLabel.setStyle("-fx-text-fill: #444;");

        Scene scene = new Scene(root, 520, 260);
        stage.setScene(scene);
        stage.show();
    }

    private void convertReport() {
        String jasperPath = jasperPathField.getText().trim();
        String destDir = destDirField.getText().trim();
        String fileName = fileNameField.getText().trim();

        if (jasperPath.isEmpty() || destDir.isEmpty() || fileName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Compila tutti i campi!");
            return;
        }

        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(new File(jasperPath));
            String jrxmlPath = destDir + File.separator + fileName + ".jrxml";
            JRXmlWriter.writeReport(report, jrxmlPath, "UTF-8");
            statusLabel.setText("✅ Conversione completata: " + jrxmlPath);
        } catch (JRException ex) {
            statusLabel.setText("❌ Errore: " + ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
