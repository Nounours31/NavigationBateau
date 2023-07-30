import { Env } from '../UIEnv';



export class cUITools {
    constructor() {
    }
   
    public setHeader () : void { 
        let elementFromDom: HTMLElement | null = document.getElementById(Env.elmentIdOfGlobalHeaderDiv);
        let element: HTMLElement  = ((elementFromDom != null) ? elementFromDom : document.createElement('div'));

        element.innerHTML = `Header`;                       
    }

    public setGlobalMenu () : void { 
        let elementFromDom: HTMLElement | null = document.getElementById(Env.elmentIdOfGlobalMenuDiv);
        let element: HTMLElement  = ((elementFromDom != null) ? elementFromDom : document.createElement('div'));

        element.innerHTML = ``;                       
    }

    public setFooter () : void { 
        let elementFromDom: HTMLElement | null = document.getElementById(Env.elmentIdOfGlobalFooteriv);
        let element: HTMLElement  = ((elementFromDom != null) ? elementFromDom : document.createElement('div'));
        element.innerHTML = `Footer`;                       
    }
  }