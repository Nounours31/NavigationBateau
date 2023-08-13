package sfa.nav.ws;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import sfa.nav.httpd.ws.infra.WSContratForWSQuery;
import sfa.nav.httpd.ws.infra.WSContratForWSResponse;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.Longitude;
import sfa.nav.model.LongitudeFactory;


public class CalculAnguilaireRestService {
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


    public WSContratForWSResponse calculInterne(WSContratForWSQuery q) {
    	WSContratForWSResponse response = new WSContratForWSResponse();
    	WSModeleRequest myQuery = new Gson().fromJson(q.query, WSModeleRequest.class);

    	if (myQuery == null) {
       		response.data = null;
    		response.status = Response.Status.BAD_REQUEST.getStatusCode();
    		response.addError("No WSAngleConversionRequest in query"); 
 

    	}
    	else {
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
    			response.data = jsonObject;
    			response.status = Response.Status.OK.getStatusCode();
    		}
    		catch(Exception e) {
    			response.status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();;
    			response.addError(e.getMessage());
    		}
    	}
		return response;
    }

    @POST
    @Path("/angle")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public WSContratForWSResponse calcul(WSContratForWSQuery q){
        return calculInterne(q);
    }
}