import { iOrthoInternalResponnse } from "../../../tools/iAjaxInterfaces";
import { myAjax } from "../../../tools/myAjax";


export class cMyNavigationPageDetails  {
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
               <label for="Latitude">Latitude (-90 <=> +90):</label>
               </td><td>
               <input type="number" id="Latitude" name="Latitude" min="-90" max="90">
                       </td>
                       <td rowspan="4"><button id="ComputeOrthoRoute">Calcul</button></td>
                       <td rowspan="2" >Cap</td>
                       <td rowspan="2" id="totoCap">0.0</td>
                       </tr>
              
               <tr>
               <td>Longitude</td>
               <td>
                   <label for="Longitude">Longitude (-180 <=> +180):</label>
                   </td><td>
                   <input type="number" id="Longitude" name="Longitude" min="-180" max="180">
               </td>
               </tr>
               <tr>
               <td rowspan="2">Arrivee</td>
               <td>Latitude</td>
               <td>
               <label for="Latitude">Latitude (-90 <=> +90):</label>
               </td><td>
               <input type="number" id="Latitude" name="Latitude" min="-90" max="90">
                       </td>
                       <td rowspan="2">Distance</td>
                       <td rowspan="2" id="totoDist">0.0</td>
                       </tr>
              
               <tr>
               <td>Longitude</td>
               <td>
                   <label for="Longitude">Longitude (-180 <=> +180):</label>
                   </td><td>
                   <input type="number" id="Longitude" name="Longitude" min="-180" max="180">
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
        Depart: <input>Depart</input></br>
        Arrivee: <input>cap</input></br>
        Solution: <input>cap</input></br>
        `;
        retour += "</div>";
        return retour;
    }   

    public createMenuCap () : string {
        let retour : string = ``;
        retour += "<div>";
        retour += `
        Cap:<br/>
        Depart: <input>Depart</input></br>
        Arrivee: <input>cap</input></br>
        Solution: <input>cap</input></br>
        `;
        retour += "</div>";
        return retour;
    }   

    public activateNavCallBack() {
        let i : number;
        let computeOrthoNave : HTMLElement = document.getElementById("ComputeOrthoRoute") as HTMLElement;
        computeOrthoNave.addEventListener('click', cMyNavigationPageDetails.computeOrthoRoute);
    }


    static computeOrthoRoute(this: HTMLElement, e: MouseEvent) : boolean{
        let x : myAjax = new myAjax();
        let y : Promise<iOrthoInternalResponnse> = x.fetch("test");

        let retour : iOrthoInternalResponnse = {cap: 0.0, distance: 0.0}; 

        let xx : HTMLElement = document.getElementById("totoCap") as HTMLElement;
        let yy : HTMLElement = document.getElementById("totoDist") as HTMLElement;

        y.then((val) => { retour = val;         xx.innerText = retour.cap.toString();
            yy.innerText = retour.distance.toString();
     })
        .then((val) => console.log(retour))
        .catch((err) => console.log(retour))
        .finally(() => console.log("finally"));


        return true;
    }
}

