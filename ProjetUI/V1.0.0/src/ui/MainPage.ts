import UIkit from 'uikit';
import Icons from 'uikit/dist/js/uikit-icons';


export class MainPage {
    constructor() {
    }
   
    public show() : void {
        const userName = "Ousseynou DIOP";
        const salary = "5.000.000";
        
        if (document != null) {
            let x: HTMLElement | null = document.getElementById("name");
            if (x != null)
                x.innerHTML = userName;
            
            x = document.getElementById("salary");
            if (x != null)
                x.innerHTML = salary;
        }
        
        UIkit.use(Icons);
        
        // components can be called from the imported UIkit reference
        UIkit.notification('Hello world.');      
        return ;
    }

    public setupMainPage () : void {
        let element: UIkitElement = new ;
        let options: UIkit.UIkitNavbarOptions = {
            "delay-hide": 0,
            "delay-show": 0,
        };

        UIkit.navbar(element, options);
    }
  }