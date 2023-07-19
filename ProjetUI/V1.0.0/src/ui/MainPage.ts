import { Env } from './UIEnv';
import { cUITools } from './cUITools';
import { cUIComponents, iTabPanelItem } from './cUIComponents';



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

        let info: iTabPanelItem[] = [
            {id: "item1", titre: "item1", contenu:".. item1 .."},
            {id: "item2", titre: "item2", contenu:".. item2 .."},
            {id: "item3", titre: "item3", contenu:".. item3 .."},
            {id: "item4", titre: "item4", contenu:".. item4 .."}
        ];

        element.innerHTML =  `Coucou c'est moi`;
        element.innerHTML += (this.UICompo.createBoutton("idToto", "toto", "", "monbuton") + this.UICompo.createTabPane(info) + this.UICompo.createAccordeon());
        this.UICompo.activateAccordeon();

    }
  }