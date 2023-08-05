
import { cUICompo } from "../components/cUICompo";
import { cUIEnv } from "../cUIEnv";
import { cMyNavigationAngleConversion } from "./header/cMyNavigationAngleConversion";

export class cMyHeader extends cUICompo {
    private x : cMyNavigationAngleConversion ;
    constructor() {
        super();
        this.x  = new cMyNavigationAngleConversion();
    }

    override getHtmlAsDom(): HTMLElement {
        let options : ElementCreationOptions = {};
        let headerDiv: HTMLDivElement = document.createElement('div', options) as HTMLDivElement; 
        headerDiv.id = cUIEnv.elmentIdOfGlobalHeaderDiv;

        let conversionAngulaireAsHtmlText : string = this.x.createMenuConversion();

        let element: HTMLDivElement = document.createElement('div', options) as HTMLDivElement; 
        element.innerHTML = conversionAngulaireAsHtmlText;                       
        return element;        
    }

    override getHtmlAsString(): string {
        throw new Error("Method not implemented.");
    }

    override activate(): void {
        this.x.activateCallBack();
    }
}

