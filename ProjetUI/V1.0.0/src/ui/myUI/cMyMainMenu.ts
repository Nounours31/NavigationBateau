
import { cUIEnv } from "../cUIEnv";
import { cUICompo } from "../components/cUICompo";
import {cUICompoTabPanel } from "../components/cUICompoTabPanel";
import { iUInfoItem } from "../components/iUInfoItem";
import { cMyNavigationPage } from "./menuNavigation/cMyNavigationPage";

export class cMyMainMenu extends cUICompo {
    private navePage : cMyNavigationPage;
    private myMenu : cUICompoTabPanel;

    constructor() {
        super();
        this.myMenu = new cUICompoTabPanel();
        this.navePage = new cMyNavigationPage();
    }

    createMainMenu () : iUInfoItem[] {
        let info: iUInfoItem[] = [
            {id: "Navigation", titre: "Navigation", contenu: this.navePage.getHtmlAsString()},
            {id: "Maree", titre: "Maree", contenu: this.navePage.getHtmlAsString()},
            {id: "NavAstro", titre: "NavAstro", contenu: this.navePage.getHtmlAsString()},
        ];
        return info;
    }   

    override getHtmlAsString(): string {
        throw new Error("Method not implemented.");
    }

    override getHtmlAsDom(): HTMLElement {
        let menuastxt : string =  this.myMenu.getHtmlAsString(this.createMainMenu());

        let options : ElementCreationOptions = {};
        let headerDiv: HTMLDivElement = document.createElement('div', options) as HTMLDivElement; 
        headerDiv.id = cUIEnv.elmentIdOfGlobalMenuDiv;

        headerDiv.innerHTML = menuastxt; 
        headerDiv.id = cUIEnv.elmentIdOfGlobalMenuDiv;
        return headerDiv;
    }

    override activate(): void {
        this.myMenu.activate();
        this.navePage.activate();
    }
}

