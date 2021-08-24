module ru.pvv {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires aspose.cells;
    //requires annotations;
    requires org.jetbrains.annotations;

    opens ru.PVV to javafx.fxml;
    exports ru.PVV;
}