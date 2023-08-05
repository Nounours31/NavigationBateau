
import {cUICompo} from "./cUICompo";
import { iUInfoItem } from "./iUInfoItem";

export class cUICompoAccordeon extends cUICompo{
    constructor() {
        super();
    }


    private createAccordeon(info?: iUInfoItem[] | undefined) : string {
        if ((info === undefined) || (info === null))
            throw new Error("missing info !!");
 
        let retour : string = `<div>`;
        info.forEach(element => {
                retour += `<button class="accordion defaut">${element.titre}</button>
                <div class="panel">
                  <p>
                  ${element.contenu}
                  </p>
                </div>`;
        });
        retour += `</div>`;
        return retour;
    } 

    private activateAccordeon() : void {
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

    override getHtmlAsDom(): HTMLElement {
        throw new Error("Method not implemented.");
    }

    override getHtmlAsString(info?: iUInfoItem[] | undefined): string {
        return this.createAccordeon(info);
    }
    override activate(): void {
        this.activateAccordeon();
    }
}
   