module com.example.assignment2dgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.mediaEmpty;
    requires javafx.media;


    opens com.example.assignment2dgame to javafx.fxml;
    exports com.example.assignment2dgame;
}