module com.example.tcpchat_interface_v {
    requires javafx.controls;
    requires javafx.fxml;
    requires aquafx;


    opens com.example.controlador to javafx.fxml;
    exports com.example.controlador;
}