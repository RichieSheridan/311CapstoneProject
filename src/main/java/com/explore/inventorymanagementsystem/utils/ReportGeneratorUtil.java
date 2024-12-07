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
        // TODO (Efe): Implement report generation functionality
        // 1. Fetch data from specified database table
        // 2. Create PDF document with proper formatting
        // 3. Add headers, data tables, and summary
        // 4. Save report to specified location
        throw new UnsupportedOperationException("Not implemented yet");
    }
}