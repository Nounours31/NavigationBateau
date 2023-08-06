import { cUIEnv } from "../../ui/cUIEnv";
import { log4TSProvider } from "../config/LogConfig";
import {WSContratForWSQuery, WSContratForWSResponse, myAjax} from "./myAjax";

interface WSPosition {
    latitude: number,
    longitude: number
}

interface WSDepartArrivee {
    depart: WSPosition, 
    arrivee: WSPosition
}

interface WSDepartArrivee {
    depart: WSPosition, 
    arrivee: WSPosition
}

export interface WSNavigation {
    capInDegre: number,
    distanceInMiles: number
}


export class cAjaxCallculNavigation {
    static readonly log = log4TSProvider.getLogger("cAjaxCallculNavigation");
    
    constructor() {}

    public callWS () : WSNavigation  {
        cAjaxCallculNavigation.log.debug("callWS");
        let queryNav : WSDepartArrivee = {
            depart: {
                latitude: 4.52,
                longitude: 0.125,
            },
            arrivee: {
                latitude: 4.52,
                longitude: 7.125,
            }
        }


        let x : myAjax = new myAjax();
        let url : string = cUIEnv.WebServerURL + "ortho";
        let query: WSContratForWSQuery = {
            "query": queryNav
        };
        let y : WSContratForWSResponse = x.synchrofetch(query, url);
        cAjaxCallculNavigation.log.debug("reponse ", y);

        let z : WSNavigation = y.data;
        cAjaxCallculNavigation.log.debug("data ", z);
        
        return z;
        

    }
}
