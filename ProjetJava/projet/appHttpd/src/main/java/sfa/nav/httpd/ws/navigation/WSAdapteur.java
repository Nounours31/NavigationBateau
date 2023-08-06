package sfa.nav.httpd.ws.navigation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import sfa.nav.httpd.WSContratForWSQuery;

public abstract class WSAdapteur {
	private static final Logger logger = LoggerFactory.getLogger(WSAdapteur.class);

	protected WSContratForWSQuery query = null;
	
	protected WSAdapteur(WSContratForWSQuery q) {
		query = q;
	}

	protected Object parseQuery(Class class1) {
		JsonObject obj = query.query;
		Object o = null;
		try {
			o = new Gson().fromJson(obj, class1);
		}
		catch (Exception e) {
			logger.error("gson.fromJson", e);
		}
		logger.debug("Query received: {}", o);
		return o;
	}

}
