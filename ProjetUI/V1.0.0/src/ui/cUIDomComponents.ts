

export class cUIDomComponents {
    constructor() {
    }

    public setBouttonContent(id: string, contenu: string) :  void {
        let b : HTMLElement | null = document.getElementById(id);
        if (b == null)
            return;

        b.innerText = contenu;
    } 
}
   