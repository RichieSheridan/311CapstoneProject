package com.explore.inventorymanagementsystem.utils;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportGeneratorUtil {

    public static void generateDatabaseReport(String tableName, String reportTitle, String fileName) {
        List<String> columns = new ArrayList<>();
        List<List<Object>> rows = new ArrayList<>();

        // Fetch column names and data from the database
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Retrieve column names
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, null)) {
                while (columnsResultSet.next()) {
                    columns.add(columnsResultSet.getString("COLUMN_NAME"));
                }
            }

            // Retrieve data for each row
            String sql = "SELECT * FROM " + tableName;
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    for (String column : columns) {
                        row.add(rs.getObject(column));
                    }
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching data from the database: " + e.getMessage());
            return;
        }

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder to Save Report");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory == null) {
            return;
        }

        File file = new File(selectedDirectory, fileName.endsWith(".pdf") ? fileName : fileName + ".pdf");

        try (PdfWriter writer = new PdfWriter(file.getAbsolutePath());
             PdfDocument pdf = new PdfDocument(writer)) {

            // Set landscape orientation and larger margins
            pdf.setDefaultPageSize(PageSize.A4.rotate());
            Document document = new Document(pdf);
            document.setMargins(20, 20, 20, 20);

            // Add report title with proper spacing
            Paragraph title = new Paragraph(reportTitle)
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            document.add(new Paragraph("\n")); // Add spacing after title

            // Calculate relative column widths based on content
            float[] columnWidths = new float[columns.size()];
            for (int i = 0; i < columns.size(); i++) {
                columnWidths[i] = 1f; // Start with equal widths
            }

            // Create table with calculated widths
            Table pdfTable = new Table(UnitValue.createPercentArray(columnWidths));
            pdfTable.setWidth(UnitValue.createPercentValue(100));
            pdfTable.setKeepTogether(false);

            // Add styled header cells
            for (String column : columns) {
                Cell headerCell = new Cell()
                        .add(new Paragraph(column)
                                .setBold()
                                .setFontSize(9))
                        .setTextAlignment(TextAlignment.CENTER);
                pdfTable.addHeaderCell(headerCell);
            }

            // Add data rows with proper styling
            for (List<Object> row : rows) {
                for (Object cellValue : row) {
                    Cell cell = new Cell()
                            .add(new Paragraph(cellValue != null ? cellValue.toString() : "")
                                    .setFontSize(8)
                                    .setMultipliedLeading(1.2f)) // Increased line height
                            .setTextAlignment(TextAlignment.CENTER)
                            .setPadding(4);
                    pdfTable.addCell(cell);
                }
            }

            // Add the table to the document
            document.add(pdfTable);

            System.out.println("Report generated successfully: " + file.getAbsolutePath());

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error creating report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}