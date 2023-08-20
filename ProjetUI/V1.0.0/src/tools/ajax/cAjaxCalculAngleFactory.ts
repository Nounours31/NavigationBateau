import { cUIEnv } from "../../ui/cUIEnv";
import { log4TSProvider } from "../config/LogConfig";
import { WSContratForWSQuery, WSContratForWSResponse, myAjax } from "./myAjax";


export enum eTypeModeleConnu {
    "Angle", "Latitude", "Longitude"
}

export enum eActionConnue {
    "Conversion"
}

export interface WSModeleRequest {
    valeur : string,
    type : string, // eTypeModeleConnu,		
    action: string // eActionConnue		
}

export interface WSModeleResponse {
    valeur : string,
}

export class cAjaxCalculAngleFactory {
    static readonly log = log4TSProvider.getLogger("cAjaxCalculAngleFactory");
    
    constructor() {}

    public callWS (queryNav : WSModeleRequest | null) : WSModeleResponse  {
        if (queryNav == null) {
            queryNav = {
                "valeur": "",
                type: eTypeModeleConnu[eTypeModeleConnu.Angle] as string,
                action: eActionConnue[eActionConnue.Conversion] as string
            }
        }
        cAjaxCalculAngleFactory.log.debug("callWS");



        let x : myAjax = new myAjax();
        let url : string = cUIEnv.WebServerURL + "modele";
        let query: WSContratForWSQuery = {
            "query": queryNav
        };
        let y : WSContratForWSResponse = x.synchrofetch(query, url);
        cAjaxCalculAngleFactory.log.debug("reponse ", y);


        let z : WSModeleResponse = {valeur: ""};
        if ((y.status !=null) && (y.status == 200))
            z.valeur = (y.data as WSModeleResponse).valeur;
        else if (y.errors != null) {
            if (Array.isArray(y.errors)) {
                y.errors.forEach(element => {
                    z.valeur += element.message;
                });
            }
            else {
                z.valeur = "Error inconnue: HTTP = " + y.status;
            }
        }
        cAjaxCalculAngleFactory.log.debug("data ", z);
        
        return z;
    }
}
