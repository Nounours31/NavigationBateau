import { log4TSProvider } from "../config/LogConfig";

export type WSResponseAsType = {
    data?: any
    errors?: Array<{message: string}>
    status?: number
}

export class myAjax {
    static readonly log = log4TSProvider.getLogger("cMyNavigationAngleConversion");
    
    constructor() {
    }
        
    private async fetch(query: string, url: string): Promise<WSResponseAsType> {
        myAjax.log.debug ("start fetch ", query, url);
        const response : Response = await window.fetch(url, {
            method: 'POST',
            headers: {
                'content-type': 'application/json;charset=UTF-8',
            },
            body: query,
        }) ;
        myAjax.log.debug ("fetch sent / await response");
        
        const {data, errors}: any = await response.json();
        myAjax.log.debug ("response ", response);

        if (response.ok) {
            let dataInResponse : any  = data;
            if ((dataInResponse !== undefined) && (dataInResponse !== null)) {
                const retour : WSResponseAsType = {
                    "data": data,
                    "errors": errors,
                    "status": response.status
                }
                myAjax.log.debug ("fetch return ", retour);
                return Object.assign(retour);
            } else {
                myAjax.log.debug ("fetch reject", response);
                return Promise.reject(new Error(`No response`))
            }
        } else {
            const error = new Error(errors?.map((e: { message: string; }) => e.message).join('\n') ?? 'unknown')
            myAjax.log.debug ("fetch reject", error);
            return Promise.reject(error)
        }
    }

    public synchrofetch(query: string, url: string): WSResponseAsType {
        myAjax.log.debug ("synchrofetch", name, query, url);
        this.fetch(query, url)
        .then((x : WSResponseAsType) => {
            myAjax.log.debug ("synchrofetch in then");
            return x;
        })
        .catch ((x) => {
            myAjax.log.debug ("synchrofetch in catch");
            throw new Error(x.toString());
        })
        .finally(() => {
            myAjax.log.debug ("synchrofetch in finally");
        });
        let x : WSResponseAsType = {};
        myAjax.log.debug ("synchrofetch return", x);
        return x;
    }

}
