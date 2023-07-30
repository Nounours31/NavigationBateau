import {iPointGeographiqueCoordianete, iOrthoRequest, iOrthoResponnse, iOrthoInternalResponnse} from "./iAjaxInterfaces";


type JSONResponse = {
    data?: iOrthoResponnse
    errors?: Array<{message: string}>
}

export class myAjax {
    constructor() {
    }
        
//    public async fetch(name: string): Promise<iOrthoInternalResponnse> {
    public async fetch(name: string): Promise<iOrthoInternalResponnse> {
            let queryNav : iOrthoRequest = {
            depart: {
                latitude: 4.52,
                longitude: 0.125,
            },
            arrivee: {
                latitude: 4.52,
                longitude: 7.125,
            }
        };

        
        const response = await window.fetch('http://localhost:8001/api/nav/ortho', {
            method: 'POST',
            headers: {
                'content-type': 'application/json;charset=UTF-8',
            },
            body: JSON.stringify({
                query: queryNav
            }),
        })
    
        const {data, errors}: JSONResponse = await response.json()
        
        if (response.ok) {
            let data2 : iOrthoResponnse  = data as iOrthoResponnse ;
            const response : iOrthoInternalResponnse = {
                cap: data2._cap._angleInDegre as number,
                distance: data2._distance._distanceInMille as number
            }
            if (response) {
                return Object.assign(response)
            } else {
                return Promise.reject(new Error(`No pokemon with the name "${name}"`))
            }
        } else {
            const error = new Error(errors?.map(e => e.message).join('\n') ?? 'unknown')
            return Promise.reject(error)
        }
    }
}
