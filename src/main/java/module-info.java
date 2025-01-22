module org.example.trylma {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens org.example.trylma to javafx.fxml;
    exports org.example.trylma;
}