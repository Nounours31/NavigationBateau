
import { cUICompo } from "../../components/cUICompo";
import { cUICompoAccordeon } from "../../components/cUICompoAccordeon";
import { iUInfoItem } from "../../components/iUInfoItem";
import { cMyNavigationPageDetails } from "./cMyNavigationPageDetails";

export class cMyNavigationPage extends cUICompo {
    private myAccortdeon : cUICompoAccordeon;
    private myNavigationPageDetails : cMyNavigationPageDetails;

    constructor() {
        super();
        this.myAccortdeon = new cUICompoAccordeon();
        this.myNavigationPageDetails = new cMyNavigationPageDetails();
    }

    private createMenu () : iUInfoItem[] {
        let info: iUInfoItem[] = [
            {id: "Orthodromie", titre: "Orthodromie", contenu: this.myNavigationPageDetails.createMenuOrtho()},
            {id: "Loxodromie", titre: "Loxodromie", contenu: this.myNavigationPageDetails.createMenuLoxo()},
            {id: "CapVitesse", titre: "CapVitesse", contenu: this.myNavigationPageDetails.createMenuCap()},
        ];
        return info;
    }   

    override getHtmlAsDom(): HTMLElement {
        throw new Error("Method not implemented.");
    }

    override getHtmlAsString(): string {
        return this.myAccortdeon.getHtmlAsString(this.createMenu());
    }
    override activate(): void {
        this.myAccortdeon.activate();
        this.myNavigationPageDetails.activateNavCallBack();
    }
}

