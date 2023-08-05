
import {cUICompo} from "./cUICompo";

export class cUICompoButton extends cUICompo{
    constructor() {
        super();
    }

    public createBoutton(id: string, nom?: string, classe? :string, contenu? :string) : string {
        let classeInfo : string = "";
        let nomInfo : string = "";
        let contenuInfo : string = "";
        if (typeof nomInfo !== 'undefined') {
            if((nomInfo != null) && (nomInfo.length > 0))
                nomInfo=`name=${nom}`;
        }
        if (typeof classe !== 'undefined') {
            if((classe != null) && (classe.length > 0))
                classeInfo=`class="${classe}"`;
        }

        if (typeof contenu !== 'undefined') {
            if((contenu != null) && (contenu.length > 0))
            contenuInfo=`${contenu}`;
        }
        let retour : string = `<button id=${id} ${nomInfo} ${classeInfo}>${contenuInfo}</button>`;
        return retour;
    } 

    override getHtmlAsString(): string {
        throw new Error("Method not implemented.");
    }
    override getHtmlAsDom(): HTMLElement {
        throw new Error("Method not implemented.");
    }
    override activate(): void {
        throw new Error("Method not implemented.");
    }
}
   