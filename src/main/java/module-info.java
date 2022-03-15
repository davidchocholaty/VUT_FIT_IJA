module com.example.vut_fit_ija {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.vut_fit_ija to javafx.fxml;
    exports com.example.vut_fit_ija;
}