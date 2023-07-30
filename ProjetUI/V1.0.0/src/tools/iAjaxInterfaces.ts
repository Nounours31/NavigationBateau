export interface iPointGeographiqueCoordianete {
    id?: string;
    latitude: number;
    longitude: number;
}

export interface iOrthoRequest {
    depart: iPointGeographiqueCoordianete
    arrivee: iPointGeographiqueCoordianete
}

export interface iAngle {
    "_angleInDegre"?: number
    "_distanceInMille"?:number
}


export interface iOrthoResponnse {
    "_cap": iAngle
    "_distance":iAngle
}

export interface iOrthoInternalResponnse {
    "cap": number
    "distance":number
}