package ui;

import java.util.List;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import models.*;

public class ExportController {

    public static Button createExportButton(Unit unit, List<Weapon> weapons, List<Armor> armors, Pane contentPane) {
        Button exportButton = new Button("Exportieren");

        exportButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exportiere Einheit");
            
            FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF-Datei (*.pdf)", "*.pdf");
            FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Bilddatei (*.png)", "*.png");
            fileChooser.getExtensionFilters().addAll(pdfFilter, pngFilter);
            fileChooser.setSelectedExtensionFilter(pdfFilter);

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                String path = file.getAbsolutePath();
                try {
                    if (path.endsWith(".pdf")) {
                        PdfExporter.exportUnitWithEquipment(unit, weapons, armors, path);
                    } else {
                        showError("Unbekanntes Format. Bitte .pdf oder .png verwenden.");
                    }
                } catch (Exception ex) {
                    showError("Fehler beim Exportieren: " + ex.getMessage());
                }
            }
        });

        return exportButton;
    }

    private static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
