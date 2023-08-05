import { cUITools } from './myUI/cUITools';



export class MainPage {

    constructor() {
    }
   

    public show () : void { 
        let t : cUITools = new cUITools();
        t.setHeader();
        t.setGlobalMenu();
        t.setFooter();
    }
  }