let myAPIKey = "ebb18e08352b474687513c0a6bb82f30";
const fireStationIcon = L.icon({
    iconUrl: `https://api.geoapify.com/v1/icon/?type=material&color=%23000000&size=small&icon=landmark&iconType=awesome&iconSize=small&textSize=small&apiKey=${myAPIKey}`,
    iconSize: [20, 20], // size of the icon
    iconAnchor: [10, 10], // point of the icon which will correspond to marker's location
    popupAnchor: [0, -10]// point from which the popup should open relative to the iconAnchor
  });

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

