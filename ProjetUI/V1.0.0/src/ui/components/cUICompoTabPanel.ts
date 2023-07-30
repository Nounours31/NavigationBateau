
import {cUICompo} from "./cUICompo";
import { iUInfoItem } from './iUInfoItem';

export class cUICompoTabPanel extends cUICompo{
    static listOfTabPanel : HTMLElement[] = [];

    constructor() {
        super();
    }

    public createTab(info: iUInfoItem[]|null) : string {
        if (info === null)
            return "";
            
        let partie1 : string = `<div class="tab">`;
        let partie2 : string = ``;

        info.forEach(element => {
            partie1 += `<button class="tablinks" id="${element.id}_button">${element.titre}</button>`;
            partie2 += `<div id="${element.id}_div" class="tabcontent">
            <h3>${element.titre}</h3>
            <p>${element.contenu}</p>
          </div>`;
        });
        partie1 += "</div>";
        return partie1 + partie2;
    } 
    override getHtml(info: iUInfoItem[]| null): string {
        return this.createTab(info);
    }

    override activate(): void {
        let i : number;
        let tablinks : HTMLCollectionOf<Element>;

        // Get all elements with class="tabcontent" and hide them
        tablinks = document.getElementsByClassName("tablinks");
        for (i = 0; i < tablinks.length; i++) {
            let x : HTMLElement = tablinks[i] as HTMLElement;

            let needListener : boolean = true;
            cUICompoTabPanel.listOfTabPanel.forEach(element => {
                if (element.id === x.id)
                    needListener = false;
            });
            if (needListener) {
                x.addEventListener('click', cUICompoTabPanel.clickCallBackOnTabpanel);
                cUICompoTabPanel.listOfTabPanel.push(x);
            }
        }
    }

    static clickCallBackOnTabpanel (this: HTMLElement, e: MouseEvent) : any {
        e.preventDefault();
        e.stopImmediatePropagation();

        // Declare all variables
        let i : number;
        let tabcontent : HTMLCollectionOf<Element>;
        let tablinks : HTMLCollectionOf<Element>;

        // Get all elements with class="tabcontent" and hide them
        tabcontent = document.getElementsByClassName("tabcontent");
        for (i = 0; i < tabcontent.length; i++) {
            let x : HTMLElement = tabcontent[i] as HTMLElement;
            x.style.display = "none";
        }

        // Get all elements with class="tablinks" and remove the class "active"
        tablinks = document.getElementsByClassName("tablinks");
        for (i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
        }

        // Show the current tab, and add an "active" class to the button that opened the tab
        let elementId : string= this.id;
        elementId = elementId.substring(0, elementId.lastIndexOf("_")) + "_div";
        let elementToDisplay : HTMLElement = document.getElementById(elementId) as HTMLElement;

        elementToDisplay.style.display = "block";
        
        elementToDisplay.className += " active";
    }
}
   