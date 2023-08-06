package sfa.nav.httpd.ws.navigation;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import sfa.nav.httpd.MyHttpResponse.Status;
import sfa.nav.httpd.MyHttpResponse.eMimeTypes;
import sfa.nav.httpd.MyRequestInfo;
import sfa.nav.httpd.WSContratForWSQuery;
import sfa.nav.httpd.WSContratForWSResponse;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.Longitude;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.nav.calculs.CalculsDeNavigation;


public class WSModeleImpl extends WSAdapteur{
	private static final Logger logger = LoggerFactory.getLogger(WSModeleImpl.class);

	public enum eTypeModeleConnu {
		Angle, Latitude, Longitude;
	}

	public enum eActionConnue {
		Conversion;
	}


	class WSModeleRequest {
		@SerializedName(value = "valeur")
	    String valeur;
		
		@SerializedName(value = "type")
	    eTypeModeleConnu type;		

		@SerializedName(value = "action")
	    eActionConnue action;		
	}

	class WSModeleResponse {
		@SerializedName(value = "valeur")
	    double valeur;		
	}

	public WSModeleImpl(WSContratForWSQuery q) {
		super(q);
	}

	public MyRequestInfo response() {
		MyRequestInfo response = new MyRequestInfo();
		WSModeleRequest myQuery = (WSModeleRequest)parseQuery (WSModeleRequest.class);
		if (myQuery == null) {
			WSContratForWSResponse x = new WSContratForWSResponse();
			x.data = null;
			x.status = 500;
			x.addError("No WSAngleConversionRequest in query"); 
			response.mimeType(eMimeTypes.txt);
			response.setContent(x.toString());
			response.status(Status.INTERNAL_ERROR);

		}
		else {
			WSContratForWSResponse contrat = new WSContratForWSResponse();
			WSModeleResponse r = new WSModeleResponse();
			
			eActionConnue a = myQuery.action;
			eTypeModeleConnu x = myQuery.type;
			String s = myQuery.valeur;
			
			try {
				if (a.equals(eActionConnue.Conversion)) {
					switch (x) {
					case Angle: 
							Angle ang = AngleFactory.fromString(s);
							r.valeur = ang.asDegre();
							break;
					case Latitude:
						Latitude lat = LatitudeFactory.fromString(s);
						r.valeur = lat.asDegre();
						break;
					case Longitude:
						Longitude longi = LongitudeFactory.fromString(s);
						r.valeur = longi.asDegre();
						break;
					}
				}
				JsonObject jsonObject = new Gson().toJsonTree(r).getAsJsonObject();
				contrat.data = jsonObject;
				contrat.status = 200;
				response.mimeType(eMimeTypes.json);
				response.status(Status.OK);
			}
			catch(Exception e) {
				contrat.status = 500;
				contrat.addError(e.getMessage());
				response.mimeType(eMimeTypes.txt);				
				response.status(Status.INTERNAL_ERROR);
			}
			response.setContent(new Gson().toJson(contrat));
		}
		return response;
	}
}
