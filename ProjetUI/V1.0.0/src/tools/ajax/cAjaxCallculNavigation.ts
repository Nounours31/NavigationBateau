import { cUIEnv } from "../../ui/cUIEnv";
import { log4TSProvider } from "../config/LogConfig";
import {WSResponseAsType, myAjax} from "./myAjax";

interface iPosition {
    latitude: number,
    longitude: number
}

interface iOrthoRequest {
    depart: iPosition, 
    arrivee: iPosition
}

export interface iOrthoInternalResponnse {
    cap: number,
    distance: number
}

export class cAjaxCallculNavigation {
    static readonly log = log4TSProvider.getLogger("cAjaxCallculNavigation");
    
    constructor() {}

    public callWS () : iOrthoInternalResponnse  {
        cAjaxCallculNavigation.log.debug("callWS");
        let queryNav : iOrthoRequest = {
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
        let y : WSResponseAsType = x.synchrofetch(JSON.stringify(queryNav), url);
        cAjaxCallculNavigation.log.debug("reponse ", y);

        let z : iOrthoInternalResponnse = y.data;
        cAjaxCallculNavigation.log.debug("data ", z);
        
        return z;
        

    }
}
