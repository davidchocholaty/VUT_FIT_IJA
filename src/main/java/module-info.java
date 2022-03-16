module com.example.vut_fit_ija_projekt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.vut_fit_ija_projekt to javafx.fxml;
    exports com.example.vut_fit_ija_projekt;
}