import { iUInfoItem } from "./iUInfoItem";

export abstract  class cUICompo {
    constructor() {
    }

    abstract getHtmlAsString(info?: iUInfoItem[]) : string;
    abstract getHtmlAsDom(info?: iUInfoItem[]) : HTMLElement;
    abstract activate() : void;
}
   