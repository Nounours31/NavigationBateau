import { Env } from './UIEnv';
import { cMyMainMenu } from './myUI/cMyMainMenu';
import { cUITools } from './myUI/cUITools';



export class MainPage {
    private mainMenuAsTabpanel : cMyMainMenu;

    constructor() {
        this.mainMenuAsTabpanel = new cMyMainMenu();              
    }
   

    public show () : void { 
        let t : cUITools = new cUITools();
        t.setHeader();
        t.setGlobalMenu();
        t.setFooter();
        
        let elementFromDom: HTMLElement | null = document.getElementById(Env.elmentIdOfCurrentCommandeDiv);
        let element: HTMLElement  = ((elementFromDom != null) ? elementFromDom : document.createElement('div'));

        let myPage : string  = this.mainMenuAsTabpanel.getHtml(null);
        element.innerHTML = myPage;
        this.mainMenuAsTabpanel.activate();
    }
  }