import {v4 as uuidv4} from 'uuid';

import {log4TSProvider} from  "../../../tools/config/LogConfig"; 
import { cAjaxCallculNavigation, iOrthoInternalResponnse } from "../../../tools/ajax/cAjaxCallculNavigation";


export class cMyNavigationAngleConversion  {
    static readonly idAngleDecimal: string  = uuidv4();
    static readonly idAngleSexaGedecimal: string  = uuidv4();
    static readonly idAnglePresqueDecimal: string  = uuidv4();
    static readonly classAnglePresqueDecimal_button: string  = cMyNavigationAngleConversion.idAngleDecimal + uuidv4();

    static readonly id_LATITUDE_AngleDecimal: string  = uuidv4();
    static readonly id_LATITUDE_AngleSexaGedecimal: string  = uuidv4();
    static readonly id_LATITUDE_AnglePresqueDecimal: string  = uuidv4();
    static readonly class_LATITUDE_AnglePresqueDecimal_button: string  = cMyNavigationAngleConversion.id_LATITUDE_AngleDecimal + uuidv4();

    static readonly id_LONGITUDE_AngleDecimal: string  = uuidv4();
    static readonly id_LONGITUDE_AngleSexaGedecimal: string  = uuidv4();
    static readonly id_LONGITUDE_AnglePresqueDecimal: string  = uuidv4();
    static readonly class_LONGITUDE_AnglePresqueDecimal_button: string  = cMyNavigationAngleConversion.id_LONGITUDE_AngleDecimal + uuidv4();

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
        <td>
        <label for="${cMyNavigationAngleConversion.idAngleSexaGedecimal}">Angle (2°45'45.55"):</label>
        <input type="text" id="${cMyNavigationAngleConversion.idAngleSexaGedecimal}" name="${cMyNavigationAngleConversion.idAngleSexaGedecimal}">
        <button class="${cMyNavigationAngleConversion.classAnglePresqueDecimal_button}">*</button></td>
        <td>
        <label for="${cMyNavigationAngleConversion.idAnglePresqueDecimal}">Angle (2°45.55"):</label>
        <input type="text" id="${cMyNavigationAngleConversion.idAnglePresqueDecimal}" name="${cMyNavigationAngleConversion.idAnglePresqueDecimal}">
        <button class="${cMyNavigationAngleConversion.classAnglePresqueDecimal_button}">*</button></td>
        </td>
        <td>
        <label for="${cMyNavigationAngleConversion.idAngleDecimal}">Angle (+/-2.45):</label>
        <input type="text" id="${cMyNavigationAngleConversion.idAngleDecimal}" name="${cMyNavigationAngleConversion.idAngleDecimal}">
        <button class="${cMyNavigationAngleConversion.classAnglePresqueDecimal_button}">*</button></td>
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
        </td>
        <td>
        <label for="${cMyNavigationAngleConversion.id_LATITUDE_AnglePresqueDecimal}">Latitude (2°45.55" N/S):</label>
        <input type="text" id="${cMyNavigationAngleConversion.id_LATITUDE_AnglePresqueDecimal}" name="${cMyNavigationAngleConversion.id_LATITUDE_AnglePresqueDecimal}">
        </td>
        <td>
        <label for="${cMyNavigationAngleConversion.idAngleDecimal}">Latitude  (+/-2.45):</label>
        <input type="text" id="${cMyNavigationAngleConversion.idAngleDecimal}" name="${cMyNavigationAngleConversion.idAngleDecimal}">
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
        </td>
        <td>
        <label for="${cMyNavigationAngleConversion.idAnglePresqueDecimal}">Angle (2°45.55" E/W):</label>
        <input type="text" id="${cMyNavigationAngleConversion.id_LONGITUDE_AnglePresqueDecimal}" name="${cMyNavigationAngleConversion.id_LONGITUDE_AnglePresqueDecimal}">
        </td>
        <td>
        <label for="${cMyNavigationAngleConversion.id_LONGITUDE_AngleDecimal}">Angle (+/-2.45):</label>
        <input type="text" id="${cMyNavigationAngleConversion.id_LONGITUDE_AngleDecimal}" name="${cMyNavigationAngleConversion.id_LONGITUDE_AngleDecimal}">
        </td>
        </tr>
        
        <tr>
        <td>
        converti
        </td>
        <td>
        converti
        </td>
        <td id="Poubelle">
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
        let computeOrthoNave : HTMLElement = document.getElementById(`${cMyNavigationAngleConversion.idAngleDecimal}`) as HTMLElement;
        computeOrthoNave.addEventListener('click', function (evt: MouseEvent) : any {
            if (evt.target instanceof HTMLElement)
                self.computeOrthoRoute(evt.target as HTMLElement, evt);
        });

        // les calback des boutton 
        let allButtonCalculAngulaire : HTMLCollectionOf<HTMLElement> = document.getElementsByClassName(`${cMyNavigationAngleConversion.classAnglePresqueDecimal_button}`) as  HTMLCollectionOf<HTMLElement>;
        for (let i = 0; i < allButtonCalculAngulaire.length; i++) {
            let x :HTMLElement | null= allButtonCalculAngulaire.item(i);
            if(x !== null) {
                x.addEventListener('click', function (evt: MouseEvent) : any {
                    if (evt.target instanceof HTMLElement) 
                        self.computeAngleconversion(evt.target as HTMLElement, evt);                    
                });
            }
        }
    }

    computeAngleconversion(elem: HTMLElement, evt: MouseEvent) {
        cMyNavigationAngleConversion.log.debug("computeAngleconversion: elem", elem);
        cMyNavigationAngleConversion.log.debug("computeAngleconversion: evt", evt);
        
        let retour : iOrthoInternalResponnse = {cap: 0.0, distance: 0.0}; 
        let ws : cAjaxCallculNavigation = new cAjaxCallculNavigation();
        retour = ws.callWS();        
        
        let xx : HTMLElement = document.getElementById("Poubelle") as HTMLElement;
        let yy : HTMLElement = document.getElementById("Poubelle") as HTMLElement;
        
            xx.innerText = retour.cap.toString();
            yy.innerText = retour.distance.toString();
        return true;
    }
    
    
    public computeOrthoRoute(elem: HTMLElement, evt: MouseEvent) : any{
        cMyNavigationAngleConversion.log.debug("computeOrthoRoute: elem", elem);
        cMyNavigationAngleConversion.log.debug("computeOrthoRoute: evt", evt);
        return true;
    }
}

  