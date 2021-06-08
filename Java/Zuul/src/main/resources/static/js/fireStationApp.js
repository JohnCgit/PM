class FireStationApp{
    constructor(markersFireStation,ListFireStation){
        this.markersFireStation = markersFireStation;
        this.ListFireStation = ListFireStation;
        this.ListFireStationVisible = ListFireStation;
    }
    
    AddFireStation(FireStation){
      L.marker([FireStation.lat,FireStation.lon], {
        icon: fireStationIcon
      }).addTo(this.markersFireStation);
    }
    

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

	fireStationFilter(){
	 this.ListFireStationVisible.forEach(FireStation => 
        this.AddFireStation(FireStation))
	}
    ToString(fireStation){
        return `<h3> Caserne n°` + fireStation.id + `</h3>` +
        `<p> Nom: ` + fireStation.libelle + `</p>` +
        `<p> Lat/Lon: (` + fireStation.lat + `,`  + fireStation.lon + `)` +`</p>` +
        `<p> Capacité Max: ` + fireStation.maxCapacity +  `</p>`;
    }

    setListFireStation(ListFireStation){
        this.ListFireStation = ListFireStation;
    }

    getMarkers(){
       return this.markersFireStation;
    }
    
    hideDisplay(display) {
        if (!display) {
        	this.ListFireStationVisible=Array();
        }
        else {
            this.ListFireStationVisible=this.ListFireStation;
        }
    } 
}