import { Env } from './UIEnv';
import { cUITools } from './cUITools';
import { cUIComponents } from './cUIComponents';



export class MainPage {
    private UICompo : cUIComponents;

    constructor() {
        this.UICompo = new cUIComponents();              
    }
   

    public show () : void { 
        let t : cUITools = new cUITools();
        t.setHeader();
        t.setGlobalMenu();
        t.setFooter();
        
        let elementFromDom: HTMLElement | null = document.getElementById(Env.elmentIdOfCurrentCommandeDiv);
        let element: HTMLElement  = ((elementFromDom != null) ? elementFromDom : document.createElement('div'));

        element.innerHTML =  `Coucou c'est moi`;
        element.innerHTML += this.UICompo.getBoutton("toto");
    }
  }