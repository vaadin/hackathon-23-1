package com.example.application.views.spreadsheet;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.spreadsheet.Spreadsheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

@PageTitle("Spreadsheet")
@Route(value = "hello")
@RouteAlias(value = "")
public class SpreadsheetView extends VerticalLayout {

    public SpreadsheetView() {
        Spreadsheet sheet = new Spreadsheet();

        // Not working if just added:
        // add(sheet);

        // Not working
        // sheet.setWidthFull();
        /// sheet.setHeightFull();

        // Not working:
        // setJustifyContentMode(JustifyContentMode.CENTER);
        // setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        // getStyle().set("text-align", "center");

        // Only working with exact pixel setups for height and width:
        //Note: would be nice to push this to documentation OR fix it to work with percentages width and height.
        sheet.setHeight("600px");
        sheet.setWidth("600px");

        TextArea textArea = new TextArea();
        textArea.setLabel("Website to import from:");
        textArea.setWidth("460px");
        textArea.setHeight("45px");

        Button importBtn = new Button();
        importBtn.setText("Import");

        // a bit weird that we can set Style-s like that (not in an immutable way?), so setStyle() would be better?:
        importBtn.getStyle().set("align-self", "end");

        importBtn.addClickListener(clickEvent -> {
            System.out.println("Button was clicked");
            try {
                File file = downloadFile(textArea.getValue());
                boolean result = loadFileToExcel(file, sheet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(textArea, importBtn);

        add(horizontalLayout, sheet);
    }

    // https://docs.google.com/spreadsheets/d/1ddo2k0WYa0kyPU6jkpPVHXPne4O1Pta1XHHKmjHMld0/export?format=csv
    // curl -L "https://docs.google.com/spreadsheets/d/1XsfK2TN418FuEstNGG2eI9FmEV-4eY-FnndigHWIhk4/export?gid=0&format=xlsx"
    // curl -L "https://docs.google.com/spreadsheets/d/1ddo2k0WYa0kyPU6jkpPVHXPne4O1Pta1XHHKmjHMld0/export?gid=0&format=xlsx"
    private File downloadFile(String fileUrl) throws IOException {
        File file = new File("./src/main/resources/downloaded.xlsx");
        FileUtils.copyURLToFile(
                new URL(fileUrl),
                file,
                1000,
                1000);
        return file;
    }

    private boolean loadFileToExcel(File file, Spreadsheet sheet) throws IOException {
        file = new File("./src/main/resources/Test Hackathon - Sheet1.xlsx");

        sheet.setWorkbook(new XSSFWorkbook(new FileInputStream(file)));

        // sheet.refreshAllCellValues();
        // UI.getCurrent().getPage().reload();
        return true;
    }

}
