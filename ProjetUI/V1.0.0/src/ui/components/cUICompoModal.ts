import { cUIEnv } from "../cUIEnv";
import {cUICompo} from "./cUICompo";
import {v4 as uuidv4} from 'uuid';

export class cUICompoModal extends cUICompo{
    private static readonly modalId : string = uuidv4();
    constructor() {
        super();
    }

    private createModal(id: string) : string {
        let retour : string = `<dialog id="${id}">
        <form>
          <p>
            <label>
              Favorite animal:
              <select>
                <option value="default">Choose…</option>
                <option>Brine shrimp</option>
                <option>Red panda</option>
                <option>Spider monkey</option>
              </select>
            </label>
          </p>
          <div>
            <button value="cancel" formmethod="dialog">Cancel</button>
            <button id="confirmBtn" value="default">Confirm</button>
          </div>
        </form>
      </dialog>
      <p>
        <button id="showDialog">Show the dialog</button>
      </p>
      <output></output>`;
        return retour;
    } 

    override getHtmlAsString(): string {
        const s : string = this.createModal(cUICompoModal.modalId);
        return s;

    }
    override getHtmlAsDom(): HTMLElement {
        let x : HTMLDivElement = document.createElement("div");
        x.innerHTML = this.getHtmlAsString();
        return x;
        
    }
    override activate(): void {
        const showButton : HTMLButtonElement = document.getElementById("showDialog") as HTMLButtonElement;
        const favDialog = document.getElementById(cUICompoModal.modalId) as HTMLDialogElement;
        const outputBox = document.querySelector("output") as HTMLOutputElement;
        const selectEl = favDialog.querySelector("select") as HTMLSelectElement;
        const confirmBtn = favDialog.querySelector("#confirmBtn") as HTMLButtonElement;
        
        // "Show the dialog" button opens the <dialog> modally
        showButton.addEventListener("click", () => {
          favDialog.showModal();
        });
        
        // "Favorite animal" input sets the value of the submit button
        selectEl.addEventListener("change", (e) => {
          confirmBtn.value = selectEl.value;
          console.log(e);
        });
        
        // "Cancel" button closes the dialog without submitting because of [formmethod="dialog"], triggering a close event.
        favDialog.addEventListener("close", (e) => {
          outputBox.value = favDialog.returnValue === "default" ? "No return value.": `ReturnValue: ${favDialog.returnValue}.`; // Have to check for "default" rather than empty string
            console.log(e);
        });
        
        // Prevent the "confirm" button from the default behavior of submitting the form, and close the dialog with the `close()` method, which triggers the "close" event.
        confirmBtn.addEventListener("click", (event) => {
          event.preventDefault(); // We don't want to submit this fake form
          favDialog.close(selectEl.value); // Have to send the select box value here.
        });
    }
    public insertInDom() {
        const maindiv : HTMLDivElement = document.getElementById(cUIEnv.elmentIdOfMainDiv) as HTMLDivElement;
        maindiv.appendChild(this.getHtmlAsDom());

        this.activate();
    }

   public show() {
        const showButton : HTMLButtonElement = document.getElementById("showDialog") as HTMLButtonElement;
        showButton.dispatchEvent(new MouseEvent("click", {}));
    }
}
   