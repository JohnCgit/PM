class FireStationApp{
    constructor(markersFireStation,ListFireStation){
        this.markersFireStation = markersFireStation;
        this.ListFireStation = ListFireStation;
    }

    /*AddFireStation(FireStation){
        L.circle([FireStation.lat,FireStation.lon], {
            color: 'blue',
            fillColor: '#f03',
            fillOpacity: 0.5,
            radius: Fire.range
        }).addTo(this.markersFireStation);
    }*/

    getFireStationAt(lat,lng){
        let fireStation = null;
        let radius = 50;
        this.ListFireStation.forEach(FireStation => {
            if(getDistanceFromLatLonInKm(FireStation.lat,FireStation.lon,lat,lng)*1000 <= radius)
            {
                fireStation = FireStation;
            }
        });
        return fireStation;    
    }

    ToString(fireStation){
        return `<h3> Caserne n°` + fireStation.id + `</h3>` +
        `<p> Nom: ` + fireStation.libelle + `</p>` +
        `<p> Lat/Lon: (` + fireStation.lat + `,`  + fireStation.lon + `)` +`</p>` +
        `<p> Capacité Max: ` + fire.capaciteMax +  `</p>`;
    }

    setListFireStation(ListFireStation){
        this.ListFireStation = ListFireStation;
    }

    getMarkers(){
       return this.markersFireStation;
    }
}