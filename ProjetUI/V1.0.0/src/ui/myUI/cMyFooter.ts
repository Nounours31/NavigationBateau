
import { cUICompo } from "../components/cUICompo";
import { cUIEnv } from "../cUIEnv";

export class cMyFooter extends cUICompo {
    constructor() {
        super();
    }

    override getHtmlAsDom(): HTMLElement {
        let options : ElementCreationOptions = {};
        let headerDiv: HTMLDivElement = document.createElement('div', options) as HTMLDivElement; 
        headerDiv.id = cUIEnv.elmentIdOfGlobalFooteriv;

        let element: HTMLDivElement = document.createElement('div', options) as HTMLDivElement; 
        element.innerHTML = `Footer`;                       
        return element;        
    }

    override getHtmlAsString(): string {
        throw new Error("Method not implemented.");
    }

    override activate(): void {
        throw new Error("Method not implemented.");
    }
}

