import { Env } from '../UIEnv';
import { cMyNavigationAngleConversion } from './menuNavigation/cMyNavigationAngleConversion';



export class cUITools {
    constructor() {
    }
   
    public setHeader () : void { 
        let elementFromDom: HTMLElement | null = document.getElementById(Env.elmentIdOfGlobalHeaderDiv);
        let element: HTMLElement  = ((elementFromDom != null) ? elementFromDom : document.createElement('div'));

        let convertTool : cMyNavigationAngleConversion = new cMyNavigationAngleConversion();
         
        element.innerHTML = convertTool.createMenuConversion();   
        convertTool.activateConversionCallBack();                    
    }

    public setGlobalMenu () : void { 
        let elementFromDom: HTMLElement | null = document.getElementById(Env.elmentIdOfGlobalMenuDiv);
        let element: HTMLElement  = ((elementFromDom != null) ? elementFromDom : document.createElement('div'));

                
        let myPage : string  = this.mainMenuAsTabpanel.getHtml(null);
        element.innerHTML = myPage;
        this.mainMenuAsTabpanel.activate();
                 
    }

    public setFooter () : void { 
        let elementFromDom: HTMLElement | null = document.getElementById(Env.elmentIdOfGlobalFooteriv);
        let element: HTMLElement  = ((elementFromDom != null) ? elementFromDom : document.createElement('div'));
        element.innerHTML = `Footer`;                       
    }
  }