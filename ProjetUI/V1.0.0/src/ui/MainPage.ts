import { cUIEnv } from "./cUIEnv";
import { cMyFooter } from "./myUI/cMyFooter";
import { cMyHeader } from "./myUI/cMyHeader";
import { cMyMainMenu } from "./myUI/cMyMainMenu";

export class MainPage {

    constructor() {
    }

    public show () : void { 
        let header : cMyHeader = new cMyHeader();
        let menu : cMyMainMenu = new cMyMainMenu();
        let footer : cMyFooter = new cMyFooter();

        let headerDiv: HTMLDivElement = document.getElementById(cUIEnv.elmentIdOfMainDiv) as HTMLDivElement;
        headerDiv.appendChild (header.getHtmlAsDom());
        headerDiv.appendChild (menu.getHtmlAsDom());
        headerDiv.appendChild (footer.getHtmlAsDom());

        header.activate();
        menu.activate();
    }
  }