module com.explore.inventorymanagementsystem {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires mssql.jdbc;
    requires java.sql;
    requires org.slf4j;
    requires org.controlsfx.controls;
    requires de.jensd.fx.glyphs.fontawesome;
    requires mysql.connector.j;
    requires kernel;
    requires layout;
    // requires com.sun.javafx.event;
    //requires de.jensd.fx.fontawesomefx.fontawesome;

    opens com.explore.inventorymanagementsystem to javafx.fxml;
    opens com.explore.inventorymanagementsystem.controllers to javafx.fxml;
    exports com.explore.inventorymanagementsystem;
    opens com.explore.inventorymanagementsystem.models to javafx.base;
    // exports com.sun.javafx.event to org.controlsfx.controls;
}