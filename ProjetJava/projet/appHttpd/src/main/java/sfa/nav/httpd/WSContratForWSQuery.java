package sfa.nav.httpd;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class WSContratForWSQuery {
	@SerializedName(value = "query")
	public JsonObject query;

	@SerializedName(value = "option")
	public JsonObject option;
}
