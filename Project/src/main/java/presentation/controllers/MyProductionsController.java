package presentation.controllers;

import Interfaces.IProduction;
import Interfaces.ISeeCredits;
import domain.CreditsManagement.CreditsSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyProductionsController implements Initializable {

    @FXML
    public Button addProgramBut;

    @FXML
    ListView<IProduction> productionsListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productionsListView.getItems().setAll(CreditsSystem.getInstance().getProductions());
    }

    @FXML
    public void onAddProgramClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/layout/add_production.fxml"));
            Stage window = (Stage) addProgramBut.getScene().getWindow();
            window.setScene(new Scene(root, window.getWidth(), window.getHeight()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
