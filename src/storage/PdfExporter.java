package storage;

import models.Unit;
import models.Weapon;
import models.Item;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileOutputStream;
import java.util.List;

public class PdfExporter {

    public static void exportUnitWithEquipment(Unit unit, List<Weapon> weapons, List<Item> items, String filename) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            // Titel
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Einheit: " + unit.toString(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Waffenübersicht
            Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            document.add(new Paragraph("Waffen:", sectionFont));
            for (Weapon w : weapons) {
                document.add(new Paragraph(w.toString()));
            }

            document.add(Chunk.NEWLINE);

            // Rüstungsübersicht
            document.add(new Paragraph("Rüstung:", sectionFont));
            for (Item a : items) {
                document.add(new Paragraph(a.toString()));
            }

            document.close();
            System.out.println("PDF erfolgreich erstellt: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
