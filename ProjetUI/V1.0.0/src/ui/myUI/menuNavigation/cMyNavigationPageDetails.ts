import {v4 as uuidv4} from 'uuid';



export class cMyNavigationPageDetails  {
    readonly idCelluleCap : string  = uuidv4();
    readonly idCelluleDistance : string  = uuidv4();
    readonly idLatitudeArrivee: string  = uuidv4();
    readonly idLongitudeArrivee: string  = uuidv4();
    readonly idLongitudeDepart: string  = uuidv4();
    readonly idLatitudeDepart: string  = uuidv4();
    readonly idButtonComputeOrthoCapDistance: string  = uuidv4();
    
    constructor() {
    }
    
    public createMenuOrtho () : string {
        let retour : string = ``;
        retour += "<div>";
        retour += `
        <table>
        <tbody>
        
        <tr>
        <th colspan="7"><h2>Route Ortho:</h2></th>
        </tr>
        
        <tr>
        <td rowspan="2">Depart</td>
        <td>Latitude</td>
        <td>
        <label for="${this.idLatitudeDepart}">Latitude (-90 <=> +90):</label>
        </td>
        <td>
        <input type="number" id="${this.idLatitudeDepart}" name="${this.idLatitudeDepart}" min="-90" max="90">
        </td>
        <td rowspan="4"><button id="${this.idButtonComputeOrthoCapDistance}">Calcul</button></td>
        <td rowspan="2">Cap</td>
        <td rowspan="2" id="${this.idCelluleCap}">0.0</td>
        </tr>
        
        <tr>
        <td>Longitude</td>
        <td>
        <label for="${this.idLongitudeDepart}">Longitude (-180 <=> +180):</label>
        </td><td>
        <input type="number" id="${this.idLongitudeDepart}" name="${this.idLongitudeDepart}" min="-180" max="180">
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
    
    public createMenuLoxo () : string {
        let retour : string = ``;
        retour += "<div>";
        retour += `
        Route Loxo:<br/>
        ...
        `;
        retour += "</div>";
        return retour;
    }   
    
    public createMenuCap () : string {
        let retour : string = ``;
        retour += "<div>";
        retour += `
        Cap:<br/>
        ...
        `;
        retour += "</div>";
        return retour;
    }   
    
    public activateNavCallBack() {
        let self : cMyNavigationPageDetails = this;
        let computeOrthoNave : HTMLElement = document.getElementById(`${this.idButtonComputeOrthoCapDistance}`) as HTMLElement;
        computeOrthoNave.addEventListener('click', function (evt: MouseEvent) : any {
            if (evt.target instanceof HTMLElement)
                self.computeOrthoRoute(evt.target as HTMLElement, evt);
        });
    }
    
    
    public computeOrthoRoute(elem: HTMLElement, evt: MouseEvent) : boolean {
        console.log (elem);
        console.log (evt);
        return true;
    }
}

  