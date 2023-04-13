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
    	logger.debug("Start loading the UI");
        try {
        	logger.debug("get monUI.fxml");
            URL url = getClass().getResource("/monUI.fxml");
        	logger.debug("monUI.fxml: {}", url.getFile());

        	logger.debug("Load the scene");
        	FXMLLoader fxmlLoader = new FXMLLoader(url);
            VBox root = (VBox) fxmlLoader.load();
        	logger.debug("VBox: {}x{}", root.getPrefWidth(), root.getPrefHeight());
            Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
            stage.setScene(scene);
            stage.setTitle("Nav UI");
            stage.show();
        	logger.debug("Scene: {}", scene);
        
        	logger.debug("Set version info");
            Controller controller = (Controller) fxmlLoader.getController();
            controller.setVersion ();
        } catch (IOException ex) {
            System.err.println("Erreur au chargement: " + ex);
        }
    	logger.debug("End loading the UI");
    }

    public void startUI() {
    	logger.debug("ok logger - start the app !");
        launch();
    }
 
} 

