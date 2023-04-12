package sfa.nav;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import sfa.nav.model.Angle;

public class Controller implements Initializable {
	private static Logger logger = LoggerFactory.getLogger(Controller.class);
	
    @FXML
    private Button goToWebButton;
    @FXML
    private WebView browser;
    @FXML
    private AnchorPane proxyConfiguration;
    @FXML
    private TextField versionTextVersion;
    @FXML
    private TextField NavTextLatitudeDecode;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void onActionLatitude (Event event) {
    	logger.debug("onActionLatitude");
    }

    @FXML
    private void latitudeOnKeyPressed (KeyEvent event) {
		logger.debug("latitudeOnKeyPressed");
    	try {
	    	String content = UITools.getTextBoxContent(event.getSource());
	    	Angle a = Angle.fromString(content);
	    	
	    	if (NavTextLatitudeDecode != null)
	    		NavTextLatitudeDecode.setText(a.toString());
    	}
    	catch (Exception e) {
    		logger.error("Error in latitudeOnKeyPressed:", e);
    	}
    }
    
    @FXML
    private void onActionLongitude (Event event) {
    	logger.debug("onActionLongitude");	
    }

    
    
	public void setVersion() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        String s = "Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".";
        versionTextVersion.setPromptText(s);
	}
}