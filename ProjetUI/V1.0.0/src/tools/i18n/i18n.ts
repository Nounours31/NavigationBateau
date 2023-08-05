export type iI18syntaxe = {
  key: string,
  value: string
}

import { allMessages } from "./i18n.fr";

class cI18nProvider {
  private constructor() {
  }
  
  public static createProvider() {
    return new cI18nProvider();
  }

  public t(key: string, args?: string[], defaut?: string) : string {
    let retour : string = "<non trouve>";
    let messageTrouve : iI18syntaxe | null = null;
    allMessages.forEach(unMessage => {
      if (unMessage.key === key) {
        messageTrouve = unMessage;
      }
    });
    if (messageTrouve == null) {
      if (defaut === undefined)
        return retour;
      return defaut;
    }
    
    retour = this.replaceParametres (messageTrouve, args);
    return retour;
  }

  private replaceParametres(messageTrouve: iI18syntaxe, args?: string[]) : string {
    let s : string = messageTrouve.value;
    if (args === undefined) {
      while (s.indexOf("{}") > 0) 
        s = s.replace ("{}", "");
    }
    else {
      args.forEach (unArg => {
        s = s.replace("{}", unArg);
      });
    }
    return s;
  }
}
export const I18nProvider = cI18nProvider.createProvider();


