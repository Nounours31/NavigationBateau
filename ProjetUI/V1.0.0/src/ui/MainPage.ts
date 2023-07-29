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
            {id: "item1", titre: "Navigation", contenu:".. item1 .."},
            {id: "item2", titre: "Maree", contenu:".. item2 .."},
            {id: "item3", titre: "NavAstro", contenu:".. item3 .."},
            {id: "item4", titre: "Divers", contenu:".. item4 .."}
        ];
        let info2: iTabPanelItem[] = [
            {id: "item21", titre: "Navigation", contenu:".. item1 .."},
            {id: "item22", titre: "Maree", contenu:".. item2 .."},
            {id: "item23", titre: "NavAstro", contenu:".. item3 .."},
            {id: "item24", titre: "Divers", contenu:".. item4 .."}
        ];

        element.innerHTML =  `Coucou c'est moi`;
        element.innerHTML += (this.UICompo.createBoutton("idToto", "toto", "", "monbuton") + this.UICompo.createTabPane(info) + this.UICompo.createAccordeon());
        element.innerHTML += this.UICompo.createTab(info2);
        this.UICompo.activateAccordeon();

    }
  }