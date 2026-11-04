package sfa.nav.httpd.ws.navigation;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import sfa.nav.httpd.MyRequestInfo;
import sfa.nav.httpd.ws.infra.WSContratForWSQuery;
import sfa.nav.httpd.ws.infra.WSContratForWSResponse;
import sfa.nav.ws.CalculAnguilaireRestService;


public class WSCalculsAngulaires {
	private static final Logger logger = LoggerFactory.getLogger(WSCalculsAngulaires.class);
	WSContratForWSQuery q = null;

	public WSCalculsAngulaires(WSContratForWSQuery _q) {
			q= _q;
	}

	public MyRequestInfo response() {
		MyRequestInfo response = new MyRequestInfo();
		CalculAnguilaireRestService x = new CalculAnguilaireRestService();
		WSContratForWSResponse wsResponse = x.calculInterne(q);
		
		response.mimeType(MediaType.APPLICATION_JSON);
		response.setContent(wsResponse);
		response.status(wsResponse.status);
		return response;
	}
}
