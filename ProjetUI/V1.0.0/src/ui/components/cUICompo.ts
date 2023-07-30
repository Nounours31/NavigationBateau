import { iUInfoItem } from "./iUInfoItem";

export abstract  class cUICompo {
    constructor() {
    }

    abstract getHtml(info: iUInfoItem[] | null) : string;
    abstract activate() : void;
}
   