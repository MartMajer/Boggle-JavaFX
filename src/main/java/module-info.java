module com.boggle.game.boggle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens com.boggle.game.boggle to javafx.fxml;
    exports com.boggle.game.boggle;
    exports com.boggle.game.rmi to java.rmi;

}