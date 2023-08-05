import { iOrthoInternalResponnse } from "../../../tools/iAjaxInterfaces";
import { myAjax } from "../../../tools/myAjax";
import {v4 as uuidv4} from 'uuid';



export class cMyNavigationAngleConversion  {
    readonly idCelluleCap : string  = uuidv4();
    readonly idCelluleDistance : string  = uuidv4();
    readonly idLatitudeArrivee: string  = uuidv4();
    readonly idLongitudeArrivee: string  = uuidv4();
    readonly idAngleDecimal: string  = uuidv4();
    readonly idAngleSexaGedecimal: string  = uuidv4();
    readonly idButtonComputeOrthoCapDistance: string  = uuidv4();
    
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
        <tr>
        <th>Angle degre sexagedecimale (2°45'45.55" E/W/N/S)</th>
        <th>Angle degre decimale (2°45.55" E/W/N/S)</th>
        <th>Angle decimale (+/-2.55</th>
        </tr>
        
        <tr>
        <td>
        <label for="${this.idAngleSexaGedecimal}">Angle (2°45'45.55" E/W/N/S):</label>
        </td>
        <td>
        <input type="text" id="${this.idAngleSexaGedecimal}" name="${this.idAngleSexaGedecimal}">
        </td>
        <label for="${this.idAngleDecimal}">Angle (2°45.55" E/W/N/S):</label>
        </td>
        <td>
        <input type="text" id="${this.idAngleDecimal}" name="${this.idAngleDecimal}">
        </td>
        </tr>
        
        <tr>
        <td>Longitude</td>
        <td>
        <label for="${this.idAngleDecimal}">Longitude (-180 <=> +180):</label>
        </td><td>
        <input type="number" id="${this.idAngleDecimal}" name="${this.idAngleDecimal}" min="-180" max="180">
        </td>
        </tr>
        
        <tr>
        <td rowspan="2">Arrivee</td>
        <td>Latitude</td>
        <td>
        <label for="${this.idLatitudeArrivee}">Latitude (-90 <=> +90):</label>
        </td><td>
        <input type="number" id="${this.idLatitudeArrivee}" name="Lati${this.idLatitudeArrivee}tude" min="-90" max="90">
        </td>
        <td rowspan="2">Distance</td>
        <td rowspan="2" id="${this.idCelluleDistance}">0.0</td>
        </tr>
        
        <tr>
        <td>Longitude</td>
        <td>
        <label for="${this.idLongitudeArrivee}">Longitude (-180 <=> +180):</label>
        </td><td>
        <input type="number" id="${this.idLongitudeArrivee}" name="${this.idLongitudeArrivee}" min="-180" max="180">
        </td>
        </tr>
        </tbody>
        </table>
        `;
        retour += "</div>";
        return retour;
    }   
    
    
    public activateConversionCallBack() {
        let self : cMyNavigationAngleConversion = this;
        let computeOrthoNave : HTMLElement = document.getElementById(`${this.idButtonComputeOrthoCapDistance}`) as HTMLElement;
        computeOrthoNave.addEventListener('click', function (evt: MouseEvent) : any {
            if (evt.target instanceof HTMLElement)
                self.computeOrthoRoute(evt.target as HTMLElement, evt);
        });
    }
    
    
    public computeOrthoRoute(elem: HTMLElement, evt: MouseEvent) : any{
        console.log (elem);
        console.log (evt);
        let x : myAjax = new myAjax();
        let y : Promise<iOrthoInternalResponnse> = x.fetch("test");
        
        
        let xx : HTMLElement = document.getElementById(`${this.idCelluleCap}`) as HTMLElement;
        let yy : HTMLElement = document.getElementById(`${this.idCelluleDistance}`) as HTMLElement;
        
        let retour : iOrthoInternalResponnse = {cap: 0.0, distance: 0.0}; 
        y.then((val) => { 
            retour = val;         
            xx.innerText = retour.cap.toString();
            yy.innerText = retour.distance.toString();
        })
        .then((val) => {
            console.log(val)
        })
        .catch((err) => {
            console.log(err)
        })
        .finally(() => {
            console.log("finally")
        });
        
        
        return true;
    }
}

  