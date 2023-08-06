import { cUIEnv } from "../../ui/cUIEnv";
import { log4TSProvider } from "../config/LogConfig";
import { WSContratForWSQuery, WSContratForWSResponse, myAjax } from "./myAjax";


enum eTypeModeleConnu {
    "Angle", "Latitude", "Longitude"
}

enum eActionConnue {
    "Conversion"
}

interface WSModeleRequest {
    valeur : string,
    type : string, // eTypeModeleConnu,		
    action: string // eActionConnue		
}

export interface WSModeleResponse {
    valeur : number,
}

export class cAjaxCalculAngleFactory {
    static readonly log = log4TSProvider.getLogger("cAjaxCalculAngleFactory");
    
    constructor() {}

    public callWS () : WSModeleResponse  {
        cAjaxCalculAngleFactory.log.debug("callWS");
        let queryNav : WSModeleRequest = {
            valeur: `2°56'59"`,
            type: eTypeModeleConnu[eTypeModeleConnu.Angle] as string,
            action: eActionConnue[eActionConnue.Conversion] as string
        }


        let x : myAjax = new myAjax();
        let url : string = cUIEnv.WebServerURL + "modele";
        let query: WSContratForWSQuery = {
            "query": queryNav
        };
        let y : WSContratForWSResponse = x.synchrofetch(query, url);
        cAjaxCalculAngleFactory.log.debug("reponse ", y);

        let z : WSModeleResponse = y.data;
        cAjaxCalculAngleFactory.log.debug("data ", z);
        
        return z;
    }
}
