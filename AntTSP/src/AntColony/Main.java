/****************************************/
/*                                      */
/*		 ELÝF UYAR - 13010011003		*/
/*		SENA ÇAKMAK - 13010011029       */
/*                                      */
/*                                      */
/****************************************/

package AntColony;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // load FXML scene
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        Scene scene = new Scene(root, 600, 500);
        // specify window parameters
        stage.setTitle("Ant Colony Optimization");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}

