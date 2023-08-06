package sfa.nav.httpd;

import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class WSContratForWSResponse {
	class ErrorMessage {
		public ErrorMessage(String s) {
			message = s;
		}
		@SerializedName(value = "message")
		String message;
		
		@Override
		public String toString() {
			return "message: " + message;
		}
	}
	
	@SerializedName(value = "data")
	public JsonObject data = null;

	@SerializedName(value = "errors")
	public ArrayList<ErrorMessage> errors = null;

	@SerializedName(value = "status")
	public int status = 0;
	
	public void addError (String s) {
		if (errors == null) {
			errors = new ArrayList<>();
		}
		errors.add(new ErrorMessage(s));
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("WSContratForWSResponse [data=" + data + ", errors=");
		for (ErrorMessage errorMessage : errors) {
			sb.append(errorMessage.toString());
		}
		sb.append(", status=" + status + "]");
		return sb.toString();
	}
	
}
