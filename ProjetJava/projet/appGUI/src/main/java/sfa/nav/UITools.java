package sfa.nav;

import javafx.scene.control.TextField;

public class UITools {

	public static String getTextBoxContent(Object source) {
		if (source instanceof  TextField) {
			TextField t = (TextField)source;
			return t.getText();
		}
		return "";
	}

}
