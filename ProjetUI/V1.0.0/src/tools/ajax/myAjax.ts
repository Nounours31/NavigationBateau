import { log4TSProvider } from "../config/LogConfig";

export type WSContratForWSQuery = {
    query: any
    options?: string
}

export type WSContratForWSResponse = {
    data?: any
    errors?: Array<{message: string}>
    status?: number
}

export class myAjax {
    static readonly log = log4TSProvider.getLogger("myAjax");
    
    constructor() {
    }
        
    public async fetch(query: WSContratForWSQuery, url: string): Promise<WSContratForWSResponse> {
        myAjax.log.debug ("start fetch ", query, url);
        const response : Response = await window.fetch(url, {
            method: 'POST',
            headers: {
                'content-type': 'application/json;charset=UTF-8',
            },
            body: JSON.stringify(query),
        }) ;
        myAjax.log.debug ("fetch sent / await response");
        
        const {data, errors}: any = await response.json();
        myAjax.log.debug ("data response ", data);

        if (response.ok) {
            let dataInResponse : any  = data;
            if ((dataInResponse !== undefined) && (dataInResponse !== null)) {
                const retour : WSContratForWSResponse = {
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

    public synchrofetch(query: WSContratForWSQuery, url: string): WSContratForWSResponse {
        myAjax.log.debug ("synchrofetch", name, query, url);        
        const async: boolean = false;
        const xhr : XMLHttpRequest = new XMLHttpRequest();
        xhr.open("POST", url, async);
        xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
        xhr.send(JSON.stringify(query));
        let retour : WSContratForWSResponse = {};
        if (xhr.status < 500)  {
            retour = JSON.parse (xhr.responseText) as WSContratForWSResponse;
        }
        myAjax.log.debug ("synchrofetch return", retour);
        return retour;
    }


}
