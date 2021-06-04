class FireStationApp{
    constructor(markersFireStation,ListFireStation){
        this.markersFireStation = markersFireStation;
        this.ListFireStation = ListFireStation;
    }

    AddFireStation(FireStation){
        L.circle([FireStation.lat,FireStation.lon], {
            color: 'red',
            fillColor: '#f03',
            fillOpacity: 0.5,
            radius: Fire.range
        }).addTo(this.markersFire);
    }

    getFireStationAt(lat,lng){
        let fireStation = null;
        this.ListFireStation.forEach(FireStation => {
            if(getDistanceFromLatLonInKm(FireStation.lat,FireStation.lon,lat,lng)*1000 <= Fire.range)
            {
                fireStation = FireStation;
            }
        });
        return fireStation;    
    }

    ToString(fireStation){
        return "Le feu a la position ("+fire.lon+";"+fire.lat+") est d'intensité:"
        +fire.intensity+", s'étend sur un rayon de:"+fire.range+"m et il est de type:"+fire.type;

    }

    setListFireStation(ListFireStation){
        this.ListFireStation = ListFireStation;
    }

    getMarkers(){
       return this.markersFireStation;
    }
}

