import {v4 as uuidv4} from 'uuid';

import {log4TSProvider} from  "../../../tools/config/LogConfig"; 
import { cAjaxCalculAngleFactory, WSModeleResponse } from '../../../tools/ajax/cAjaxCalculAngleFactory';


export class cMyNavigationAngleConversion  {
    static readonly classIdOfAllButtonForcompute: string  = uuidv4();
    static readonly TAG_FOR_RESPONSE : string = "_response";

    static readonly idAngleDecimal: string  = uuidv4();
    static readonly idAngleSexaGedecimal: string  = uuidv4();
    static readonly idAnglePresqueDecimal: string  = uuidv4();

    static readonly id_LATITUDE_AngleDecimal: string  = uuidv4();
    static readonly id_LATITUDE_AngleSexaGedecimal: string  = uuidv4();
    static readonly id_LATITUDE_AnglePresqueDecimal: string  = uuidv4();

    static readonly id_LONGITUDE_AngleDecimal: string  = uuidv4();
    static readonly id_LONGITUDE_AngleSexaGedecimal: string  = uuidv4();
    static readonly id_LONGITUDE_AnglePresqueDecimal: string  = uuidv4();

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
        <th colspan="7">Angle</th>
        </tr>
        <tr>
        <th>Angle degre sexagedecimale (2°45'45.55")</th>
        <th>Angle Minutes Decimale (2°45.55")</th>
        <th>Angle decimale (2.55)</th>
        </tr>
        
        <tr>
        <td class="${cMyNavigationAngleConversion.idAngleSexaGedecimal}">
        <label for="${cMyNavigationAngleConversion.idAngleSexaGedecimal}">Angle (2°45'45.55"):</label>
        <input type="text" id="${cMyNavigationAngleConversion.idAngleSexaGedecimal}" name="${cMyNavigationAngleConversion.idAngleSexaGedecimal}">
        <button class="${cMyNavigationAngleConversion.idAngleSexaGedecimal} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
        <td>
        <label for="${cMyNavigationAngleConversion.idAnglePresqueDecimal}">Angle (2°45.55"):</label>
        <input type="text" id="${cMyNavigationAngleConversion.idAnglePresqueDecimal}" name="${cMyNavigationAngleConversion.idAnglePresqueDecimal}">
        <button class="${cMyNavigationAngleConversion.idAnglePresqueDecimal} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
        </td>
        <td>
        <label for="${cMyNavigationAngleConversion.idAngleDecimal}">Angle (+/-2.45):</label>
        <input type="text" id="${cMyNavigationAngleConversion.idAngleDecimal}" name="${cMyNavigationAngleConversion.idAngleDecimal}">
        <button class="${cMyNavigationAngleConversion.idAngleDecimal} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
        </td>
        </tr>
        
        <tr>
        <td id="${cMyNavigationAngleConversion.idAngleSexaGedecimal}${cMyNavigationAngleConversion.TAG_FOR_RESPONSE}">
        -
        </td>
        <td id="${cMyNavigationAngleConversion.idAnglePresqueDecimal}${cMyNavigationAngleConversion.TAG_FOR_RESPONSE}">
        -
        </td>
        <td id="${cMyNavigationAngleConversion.idAngleDecimal}${cMyNavigationAngleConversion.TAG_FOR_RESPONSE}">
        -
        </td>
        </tr>
        <!-- **************** Latitude ******************** -->
        <th colspan="7">Latitude</th>
        <tr>
        <th>Latitude degre sexagedecimale (2°45'45.55" N/S)</th>
        <th>Latitude Minutes Decimale (2°45.55" N/S)</th>
        <th>Latitude decimale (+/- 2.55)</th>
        </tr>
        
        <tr>
        <td>
        <label for="${cMyNavigationAngleConversion.id_LATITUDE_AngleSexaGedecimal}">Latitude (2°45'45.55" N/S):</label>
        <input type="text" id="${cMyNavigationAngleConversion.id_LATITUDE_AngleSexaGedecimal}" name="${cMyNavigationAngleConversion.id_LATITUDE_AngleSexaGedecimal}">
        <button class="${cMyNavigationAngleConversion.id_LATITUDE_AngleSexaGedecimal} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
        </td>
        <td>
        <label for="${cMyNavigationAngleConversion.id_LATITUDE_AnglePresqueDecimal}">Latitude (2°45.55" N/S):</label>
        <input type="text" id="${cMyNavigationAngleConversion.id_LATITUDE_AnglePresqueDecimal}" name="${cMyNavigationAngleConversion.id_LATITUDE_AnglePresqueDecimal}">
        <button class="${cMyNavigationAngleConversion.id_LATITUDE_AnglePresqueDecimal} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
        </td>
        <td>
        <label for="${cMyNavigationAngleConversion.id_LATITUDE_AngleDecimal}">Latitude  (+/-2.45):</label>
        <input type="text" id="${cMyNavigationAngleConversion.id_LATITUDE_AngleDecimal}" name="${cMyNavigationAngleConversion.id_LATITUDE_AngleDecimal}">
        <button class="${cMyNavigationAngleConversion.id_LATITUDE_AngleDecimal} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
        </td>
        </tr>
        
        <tr>
        <td>
        converti
        </td>
        <td>
        converti
        </td>
        <td>
        converti
        </td>
        <tr>
        <th>Longitude degre sexagedecimale (2°45'45.55"  E/W)</th>
        <th>Longitude Minutes Decimale (2°45.55"  E/W )</th>
        <th>Longitude decimale (+/-2.55)</th>
        </tr>
        
        <!-- **************** Longitude ******************** -->
        <th colspan="7">Longitude</th>
        <tr>
        <td>
        <label for="${cMyNavigationAngleConversion.id_LONGITUDE_AngleSexaGedecimal}">Angle (2°45'45.55" E/W):</label>
        <input type="text" id="${cMyNavigationAngleConversion.id_LONGITUDE_AngleSexaGedecimal}" name="${cMyNavigationAngleConversion.id_LONGITUDE_AngleSexaGedecimal}">
        <button class="${cMyNavigationAngleConversion.id_LONGITUDE_AngleSexaGedecimal} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
        </td>
        <td>
        <label for="${cMyNavigationAngleConversion.id_LONGITUDE_AnglePresqueDecimal}">Angle (2°45.55" E/W):</label>
        <input type="text" id="${cMyNavigationAngleConversion.id_LONGITUDE_AnglePresqueDecimal}" name="${cMyNavigationAngleConversion.id_LONGITUDE_AnglePresqueDecimal}">
        <button class="${cMyNavigationAngleConversion.id_LONGITUDE_AnglePresqueDecimal} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
        </td>
        <td>
        <label for="${cMyNavigationAngleConversion.id_LONGITUDE_AngleDecimal}">Angle (+/-2.45):</label>
        <input type="text" id="${cMyNavigationAngleConversion.id_LONGITUDE_AngleDecimal}" name="${cMyNavigationAngleConversion.id_LONGITUDE_AngleDecimal}">
        <button class="${cMyNavigationAngleConversion.id_LONGITUDE_AngleDecimal} ${cMyNavigationAngleConversion.classIdOfAllButtonForcompute}">*</button></td>
        </td>
        </tr>
        
        <tr>
        <td>
        converti
        </td>
        <td>
        converti
        </td>
        <td>
        converti
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

        let retour : WSModeleResponse = {valeur: 0.0}; 
        let ws : cAjaxCalculAngleFactory = new cAjaxCalculAngleFactory();
        retour = ws.callWS(value);        
        
        
        responsezone.innerText = retour.valeur.toString();
        return true;
    }
}

  