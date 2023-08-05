import "./style.scss";
import { MainPage } from "./ui/MainPage";


let allDivs : string[] = [
    "sfa_nav_global_header",
    "sfa_nav_global_menu",
    "sfa_nav_current_commande",
    "sfa_nav_global_footer"
];

let newDiv : HTMLElement;
allDivs.forEach(uneDiv => {
    newDiv = document.createElement("div");
    newDiv.id = uneDiv;    
    document.body.appendChild(newDiv);
});


let x : MainPage = new MainPage();
x.show();
