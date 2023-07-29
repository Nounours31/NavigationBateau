
export interface iTabPanelItem {
    id: string;
    titre: string;
    contenu: string;
  }

export class cUIComponents {
    constructor() {
    }

    public createBoutton(id: string, nom?: string, classe? :string, contenu? :string) : string {
        let classeInfo : string = "";
        let nomInfo : string = "";
        let contenuInfo : string = "";
        if (typeof nomInfo !== 'undefined') {
            if((nomInfo != null) && (nomInfo.length > 0))
                nomInfo=`name=${nom}`;
        }
        if (typeof classe !== 'undefined') {
            if((classe != null) && (classe.length > 0))
                classeInfo=`class="${classe}"`;
        }

        if (typeof contenu !== 'undefined') {
            if((contenu != null) && (contenu.length > 0))
            contenuInfo=`${contenu}`;
        }
        let retour : string = `<button id=${id} ${nomInfo} ${classeInfo}>${contenuInfo}</button>`;
        return retour;
    } 

    public createAccordeon() : string {
        let retour : string = `<button class="accordion defaut">Section 1</button>
        <div class="panel">
          <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
        </div>
        
        <button class="accordion">Section 2</button>
        <div class="panel">
          <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
        </div>
        
        <button class="accordion">Section 3</button>
        <div class="panel">
          <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
        </div>`;
        return retour;
    } 
    public activateAccordeon() : void {
        let acc = document.getElementsByClassName("accordion");
        var i;

        for (i = 0; i < acc.length; i++) {
            acc[i].addEventListener("click", function(evt: Event) {
                let o : HTMLElement = evt.target as HTMLElement; 
                o.classList.toggle("active");
                let panel : HTMLElement = o.nextElementSibling as HTMLElement; 
                if (panel.style.display === "block") {
                    panel.style.display = "none";
                } 
                else {
                    panel.style.display = "block";
                }
            });
        }
    } 
    

    public createTabPane(info: iTabPanelItem[]) : string {
        let partie1 : string = `<p class="tabpanel">`;
        let partie2 : string = `<div class="tabpanel">`;

        info.forEach(element => {
            partie1 += `<a href="#${element.id}" class="tabpanel">${element.titre}</a><span class="tabpanel tabpanelseparator">x</span> `;
            partie2 += `<p id="${element.id}" class="tabpanel">${element.contenu}</p>`;
        });
        partie1 += "</p>";
        partie2 += "</div>";
        return partie1 + partie2;
    }     
    
    public createTab(info: iTabPanelItem[]) : string {
        let partie1 : string = `<div class="tab">`;
        let partie2 : string = ``;

        info.forEach(element => {
            partie1 += `<button class="tablinks" onclick="openCity(event, '${element.id}')">${element.titre}</button>`;
            partie2 += `<div id="${element.id}" class="tabcontent">
            <h3>${element.titre}</h3>
            <p>${element.contenu}</p>
          </div>`;
        });
        partie1 += "</div>";
        return partie1 + partie2;
    } 

}
   