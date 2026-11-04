package sfa.nav.httpd.ws.infra;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class WSContratForWSQuery {
	// -----------------------------------
	// contrat je dois recevoir
	//   {
	//      query: {...}, option: {...}
	//   }
	// -----------------------------------

	@SerializedName(value = "query")
	public JsonObject query;

	@SerializedName(value = "option")
	public JsonObject option;
}
