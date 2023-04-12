package sfa.nav;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage; 

public class MyUI extends Application {
	private static Logger logger = LoggerFactory.getLogger(MyUI.class);
	
    @Override
    public void start(Stage stage) {
        try {
            URL url = getClass().getResource("/monUI.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            VBox root = (VBox) fxmlLoader.load();
            Controller controller = (Controller) fxmlLoader.getController();
            Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefWidth());

            stage.setScene(scene);
            stage.setTitle("Nav UI");
            stage.show();
        
            controller.setVersion ();
        } catch (IOException ex) {
            System.err.println("Erreur au chargement: " + ex);
        }
    }

    public void startUI() {
    	logger.debug("ok logger - start the app !");
        launch();
    }
 
} 

