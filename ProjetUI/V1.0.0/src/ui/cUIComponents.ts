

export class cUIComponents {
    constructor() {
    }

    public getBoutton(nom: string, classe? :string) : string {
        let classeInfo = "";
        if (typeof classe !== 'undefined') {
            classeInfo=`class=${classe}`;
        }
        let retour : string = `<button name=${nom} ${classeInfo}></button>`;
        return retour;
    } 
}
   