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
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.nav.calculs.CalculsDeNavigation;


public class WSNavigationImpl extends WSAdapteur{
	private static final Logger logger = LoggerFactory.getLogger(WSNavigationImpl.class);

	class WSPosition {
		@SerializedName(value = "latitude")
		double latitude;
		
		@SerializedName(value = "longitude")
		double longitude;
	}
	
	class WSDepartArrivee {
		@SerializedName(value = "depart")
		WSPosition depart;
		@SerializedName(value = "arrivee")
		WSPosition arrivee;
	}
	
	class WSNavigation {
		@SerializedName(value = "capInDegre")
	    double cap;
		@SerializedName(value = "distanceInMiles")
	    double distance;
	}

	public WSNavigationImpl(WSContratForWSQuery q) {
		super(q);
	}

	public MyRequestInfo response() {
		MyRequestInfo response = new MyRequestInfo();
		WSDepartArrivee myQuery = (WSDepartArrivee)parseQuery (WSDepartArrivee.class);
		if (myQuery == null) {
			WSContratForWSResponse x = new WSContratForWSResponse();
			x.data = null;
			x.status = 500;
			x.addError("No WSDepartArrivee in query"); 
			response.mimeType(eMimeTypes.txt);
			response.setContent(x.toString());
			response.status(Status.INTERNAL_ERROR);

		}
		else {
			CalculsDeNavigation cnv = new CalculsDeNavigation();
			PointGeographique pg1 = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(myQuery.depart.latitude), LongitudeFactory.fromDegre(myQuery.depart.longitude));
			PointGeographique pg2 = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(myQuery.arrivee.latitude), LongitudeFactory.fromDegre(myQuery.arrivee.longitude));
			DataLoxodromieCapDistance calculs = cnv.capLoxodromique(pg1, pg2);

			
			WSContratForWSResponse x = new WSContratForWSResponse();
			WSNavigation nav = new WSNavigation();
			nav.cap = calculs.cap().asDegre();
			nav.distance = calculs.distance().distanceInMilleNautique();
			JsonObject jsonObject = new Gson().toJsonTree(nav).getAsJsonObject();
			x.data = jsonObject;
			x.status = 200;
			response.mimeType(eMimeTypes.json);
			response.setContent(new Gson().toJson(x));
			response.status(Status.OK);
		}
		return response;
	}
}
