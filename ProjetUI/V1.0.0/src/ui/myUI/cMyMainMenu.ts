
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
            {id: "Navigation", titre: "Navigation", contenu: this.navePage.getHtml(null)},
            {id: "Maree", titre: "Maree", contenu: this.navePage.getHtml(null)},
            {id: "NavAstro", titre: "NavAstro", contenu: this.navePage.getHtml(null)},
        ];
        return info;
    }   

    override getHtml(info: iUInfoItem[]| null): string {
        if (info === null)
            info = [];
        return this.myMenu.getHtml(this.createMainMenu());
    }
    override activate(): void {
        this.myMenu.activate();
        this.navePage.activate();
    }
}

