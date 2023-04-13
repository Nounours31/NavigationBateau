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
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import sfa.nav.model.Angle;
import sfa.nav.model.Latitude;
import sfa.nav.model.Longitude;
import sfa.nav.model.PointGeographique;
import sfa.nav.mylittlemath.CalculsAngulaires;
import sfa.nav.mylittlemath.HandlerOnCapDistance;

public class Controller implements Initializable {
	private static Logger logger = LoggerFactory.getLogger(Controller.class);
	
    /*==============================================
     * Le Menu
     ===============================================*/
    @FXML
    private MenuBar idMenuBar;

    /*==============================================
     * La NAV 
     ===============================================*/
    /* Inputs */
    @FXML
    private TextField idLatitudeFrom;
    @FXML
    private TextField idLongitudeFrom;
    @FXML
    private TextField idLatitudeTo;
    @FXML
    private TextField idLongitudeTo;

    /* Outputs */
    @FXML
    private TextField textNavCap;
    @FXML
    private TextField textNavOrthodromie;
    @FXML
    private TextField textNavLoxodromie;
    
    /* debug zone debug des angle a virer ? */
    @FXML
    private TextField TextLatitudeFromDecode;
    @FXML
    private TextField TextLongitudeFromDecode;
    @FXML
    private TextField TextLatitudeToDecode;
    @FXML
    private TextField TextLongitudeToDecode;
    
    /*==============================================
     * La Version 
     ===============================================*/
    @FXML
    private TextField versionTextVersion;

    
    
    // Interne
    private String action = "";
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }


    @FXML
    private void onMouseClickMenuBar (Event event) {
    	action = "onMouseClickMenuBar"; 
    	try {
    		logger.debug("Start {}", action);
    	}
    	catch (Exception e) {
    		logger.error("{} error:", action,  e);
    	}
    }
    

    @FXML
    private void onKeyTypedLatitudeFrom (Event event) {
    	action = "latitudeOnKeyPressed"; 
    	try {
    		logger.debug("Start {}", action);
    		parseAngleAndEchoDebug(event, TextLatitudeFromDecode);
    	}
    	catch (Exception e) {
    		logger.error("{} error:", action,  e);
    	}    	
    }


    @FXML
    private void onKeyTypedLongitudeFrom (Event event) {
    	action = "onActionLongitudeFrom"; 
    	try {
    		logger.debug("Start {}", action);
    		parseAngleAndEchoDebug(event, TextLongitudeFromDecode);
    	}
    	catch (Exception e) {
    		logger.error("{} error:", action,  e);
    	}    	
    }

    @FXML
    private void onKeyTypedlatitudeTo (Event event) {
    	action = "onActionLatitudeTo"; 
    	try {
    		logger.debug("Start {}", action);
    		parseAngleAndEchoDebug(event, TextLatitudeToDecode);
    	}
    	catch (Exception e) {
    		logger.error("{} error:", action,  e);
    	}    	
    }

    @FXML
    private void onKeyTypedLongitudeTo (Event event) {
    	action = "onActionLongitudeTo"; 
    	try {
    		logger.debug("Start {}", action);
    		parseAngleAndEchoDebug(event, TextLongitudeToDecode);
    	}
    	catch (Exception e) {
    		logger.error("{} error:", action,  e);
    	}    	
    }

	private void parseAngleAndEchoDebug(Event event, TextField echoDebug) {
		if (echoDebug != null) {
			if (event == null) {
				echoDebug.setText("No event denined");
				return;
			}
			
			String content = UITools.getTextBoxContent(event.getSource());
			Angle a = Angle.fromString(content);
			echoDebug.setText(a.toString());
		}
	}


    @FXML
    private void onOKNavClick (Event event) {
    	action = "onOKNavClick"; 
    	try {
    		logger.debug("Start {}", action);
    		boolean isOK = (idLatitudeFrom != null);
    		isOK = isOK && (idLongitudeFrom != null);
    		isOK = isOK && (idLatitudeTo != null);
    		isOK = isOK && (idLongitudeTo != null);
    		
    		if (isOK) {
    			PointGeographique From = new PointGeographique();
    			PointGeographique To = new PointGeographique();
    		
    			String content = UITools.getTextBoxContent(idLatitudeFrom);
    			Angle a = Angle.fromString(content);
    			Latitude l = Latitude.fromAngle(a);

    			content = UITools.getTextBoxContent(idLongitudeFrom);
    			a = Angle.fromString(content);
    			Longitude L = Longitude.fromAngle(a);
    			From.latitude(l);
    			From.longitude(L);
    		
        		
    			content = UITools.getTextBoxContent(idLatitudeTo);
    			a = Angle.fromString(content);
    			l = Latitude.fromAngle(a);

    			content = UITools.getTextBoxContent(idLongitudeTo);
    			a = Angle.fromString(content);
    			L = Longitude.fromAngle(a);
    			To.latitude(l);
    			To.longitude(L);
    			
    			CalculsAngulaires c = new CalculsAngulaires();
    			HandlerOnCapDistance ortho = c.getOrthoDromieCapDistanceEntreDeuxPoints(From, To);
    	    	HandlerOnCapDistance loxo = c.getLoxoDromieCapDistanceEntreDeuxPoints(From, To);
    	    	
    	        textNavCap.setText("Ortho: " + ortho._cap + "  | Loxo: " + loxo._cap);
    	        textNavOrthodromie.setText(ortho._distance.toString());
    	        textNavLoxodromie.setText(loxo._distance.toString());
    		}
    	}
    	catch (Exception e) {
    		logger.error("{} error:", action,  e);
    	}    	
    }


    
    
	public void setVersion() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        String s = "Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".";
        versionTextVersion.setPromptText(s);
	}
}