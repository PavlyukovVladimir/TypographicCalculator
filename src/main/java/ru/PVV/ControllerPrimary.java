package ru.PVV;

import java.io.IOException;
import javafx.fxml.FXML;

public class ControllerPrimary {

    @FXML
    private void switchToOfset() throws IOException {
        App.setRoot("ofset");
    }
    @FXML
    private void switchToRiso() throws IOException {
        App.setRoot("riso");
    }
}
