import { Env } from './UIEnv';



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

        element.innerHTML = 
        `<div class="pure-menu pure-menu-horizontal">
        <a href="#" class="pure-menu-heading pure-menu-link">BRAND</a>
        <ul class="pure-menu-list">
            <li class="pure-menu-item">
                <a href="#" class="pure-menu-link">News</a>
            </li>
            <li class="pure-menu-item">
                <a href="#" class="pure-menu-link">Sports</a>
            </li>
            <li class="pure-menu-item">
                <a href="#" class="pure-menu-link">Finance</a>
            </li>
        </ul>
    </div>
    `;                       
    }

    public setFooter () : void { 
        let elementFromDom: HTMLElement | null = document.getElementById(Env.elmentIdOfGlobalFooteriv);
        let element: HTMLElement  = ((elementFromDom != null) ? elementFromDom : document.createElement('div'));
        element.innerHTML = `Footer`;                       
    }
  }