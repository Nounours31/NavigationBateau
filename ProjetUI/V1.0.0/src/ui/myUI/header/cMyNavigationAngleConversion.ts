import {v4 as uuidv4} from 'uuid';

import {log4TSProvider} from  "../../../tools/config/LogConfig"; 
import { cAjaxCalculAngleFactory, WSModeleResponse, WSModeleRequest, eActionConnue, eTypeModeleConnu } from '../../../tools/ajax/cAjaxCalculAngleFactory';


export class cMyNavigationAngleConversion  {
    static readonly classIdOfAllButtonForcompute: string  = uuidv4();
    static readonly TAG_FOR_RESPONSE : string = "_response";

    static readonly idAngle: string  = uuidv4();
    static readonly id_LATITUDE_Angle: string  = uuidv4();
    static readonly id_LONGITUDE_Angle: string  = uuidv4();

    static readonly log = log4TSProvider.getLogger("cMyNavigationAngleConversion");

    constructor() {
    }
    
    public createMenuConversion () : string {
        let retour : string = ``;
        retour += "<div>";
        retour += `
        <table>
        <tbody>
        
        <tr>
        <th colspan="7"><h2>Conversion angulaire:</h2></th>
        </tr>

        <!-- **************** Angle ******************** -->
        <tr>
        <th>Angle<br/>degre sexagedecimale (2°45'45.55")<br/>degre sexadecimale (2°45.55')<br/>degre decimale (+/- 2.45°)</th>
        <th>Latitude<br/>degre sexagedecimale (2°45'45.55" N/S)<br/>degre sexadecimale (2°45.55' N/S)<br/>degre decimale (+/- 2.45° N/S)</th>
        <th>Longitude<br/>degre sexagedecimale (2°45'45.55" E/W)<br/>degre sexadecimale (2°45.55' E/W)<br/>degre decimale (+/- 2.45° E/W)</th>
        </tr>
        
        <tr>
            <td class="${cMyNavigationAngleConversion.idAngle}">
            <label for="${cMyNavigationAngleConversion.idAngle}">Angle :</label>
            <input type="text" id="${cMyNavigationAngleConversion.idAngle}" name="${cMyNavigationAngleConversion.idAngle}">
            <button class="${cMyNavigationAngleConversion.idAngle} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button>
            </td>

            <td>
            <label for="${cMyNavigationAngleConversion.id_LATITUDE_Angle}">Lat :</label>
            <input type="text" id="${cMyNavigationAngleConversion.id_LATITUDE_Angle}" name="${cMyNavigationAngleConversion.id_LATITUDE_Angle}">
            <button class="${cMyNavigationAngleConversion.id_LATITUDE_Angle} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
            </td>
        
            <td>
            <label for="${cMyNavigationAngleConversion.id_LONGITUDE_Angle}">Long :</label>
            <input type="text" id="${cMyNavigationAngleConversion.id_LONGITUDE_Angle}" name="${cMyNavigationAngleConversion.id_LONGITUDE_Angle}">
            <button class="${cMyNavigationAngleConversion.id_LONGITUDE_Angle} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
            </td>
    
        </tr>
        
        <tr>
        <td id="${cMyNavigationAngleConversion.idAngle}${cMyNavigationAngleConversion.TAG_FOR_RESPONSE}">
        -
        </td>
        <td id="${cMyNavigationAngleConversion.id_LATITUDE_Angle}${cMyNavigationAngleConversion.TAG_FOR_RESPONSE}">
        -
        </td>
        <td id="${cMyNavigationAngleConversion.id_LONGITUDE_Angle}${cMyNavigationAngleConversion.TAG_FOR_RESPONSE}">
        -
        </td>
        </tr>
        </tbody>
        </table>
        `;
        retour += "</div>";
        return retour;
    }   
    
    

    public activateCallBack() : void {
        this.activateConversionCallBack();
    }
    
    public activateConversionCallBack() {
        let self : cMyNavigationAngleConversion = this;

        // les calback de TOUS les boutton 
        let allButtonCalculAngulaire : HTMLCollectionOf<HTMLElement> = document.getElementsByClassName(`${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}`) as  HTMLCollectionOf<HTMLElement>;
        for (let i = 0; i < allButtonCalculAngulaire.length; i++) {
            let x :HTMLElement | null= allButtonCalculAngulaire.item(i);
            if(x !== null) {
                x.addEventListener('click', function (evt: MouseEvent) : any {
                    if (evt.target instanceof HTMLElement) 
                        self.computeConversion(evt.target as HTMLElement, evt);                    
                });
            }
        }
    }

    computeConversion(elem: HTMLElement, evt: MouseEvent) {
        cMyNavigationAngleConversion.log.debug("computeAngleconversion: elem", elem);
        cMyNavigationAngleConversion.log.debug("computeAngleconversion: evt", evt);
  
        // Recup du boutton
        let b : HTMLButtonElement | null = elem as HTMLButtonElement;
        if (b == null)
            return false;

        let buttonId : DOMTokenList = b.classList;
        let typeConversion : string = "Inconnue";
        if (buttonId.contains (cMyNavigationAngleConversion.idAngle))
            typeConversion =  eTypeModeleConnu[eTypeModeleConnu.Angle] as string;
        else if (buttonId.contains (cMyNavigationAngleConversion.id_LATITUDE_Angle))
            typeConversion =  eTypeModeleConnu[eTypeModeleConnu.Latitude] as string;
        else if (buttonId.contains (cMyNavigationAngleConversion.id_LONGITUDE_Angle))
            typeConversion =  eTypeModeleConnu[eTypeModeleConnu.Longitude] as string;

        // recup de l'input associe au boutton
        let input : HTMLInputElement | null = b.previousElementSibling as HTMLInputElement;
        if (input == null)
            return false;

        // recup de l'input 
        let value : string = input.value;

        // recup de la zone de reponse
        let responsezone : HTMLElement | null = document.getElementById(input.id + cMyNavigationAngleConversion.TAG_FOR_RESPONSE);
        if (responsezone == null)
            return false;

        let query : WSModeleRequest = {
            "valeur": value,
            "type": typeConversion,
            "action": eActionConnue[eActionConnue.Conversion] as string
        }
        let ws : cAjaxCalculAngleFactory = new cAjaxCalculAngleFactory();
        let retour : WSModeleResponse = ws.callWS(query);        
        
        
        responsezone.innerText = retour.valeur.toString();
        return true;
    }
}

  