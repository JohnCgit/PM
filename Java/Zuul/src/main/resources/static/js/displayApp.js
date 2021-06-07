const myAPIKey = "ebb18e08352b474687513c0a6bb82f30";
const vehicleIcon = L.icon({
    iconUrl: `https://api.geoapify.com/v1/icon/?type=circle&color=%23000000&size=small&icon=taxi&iconType=awesome&iconSize=large&shadowColor=%230d0d0d&apiKey=${myAPIKey}`,
    iconSize: [20, 20], // size of the icon
    iconAnchor: [10, 10], // point of the icon which will correspond to marker's location
    popupAnchor: [0, -10]// point from which the popup should open relative to the iconAnchor
  });

const fireStationIcon = L.icon({
  iconUrl: `https://api.geoapify.com/v1/icon/?type=material&color=%23000000&size=small&icon=landmark&iconType=awesome&iconSize=small&textSize=small&apiKey=${myAPIKey}`,
  iconSize: [20, 20], // size of the icon
  iconAnchor: [10, 10], // point of the icon which will correspond to marker's location
  popupAnchor: [0, -10]// point from which the popup should open relative to the iconAnchor
});

class displayApp{
    
    constructor(){
        this.map = L.map(document.getElementById("mapid")).setView([45.752433, 4.834328], 10)
        L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, ' +
                'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
            id: 'mapbox/streets-v11',
            tileSize: 512,
            zoomOffset: -1
        }).addTo(this.map);
        
        this.map.on('click', this.onMapClick);
        //this.map.on('zoomend', this.resizeIcon); 
    
    }

    addTo(arg){
        arg.addTo(this.map);
    }

    resetMarker(arg){
        arg.clearLayers();
    }

    resizeIcon(){
        let zoom = this.map.getZoom();
        vehicleIcon = new L.icon({
            iconUrl: `https://api.geoapify.com/v1/icon/?type=circle&color=%23000000&size=small&icon=taxi&iconType=awesome&iconSize=large&shadowColor=%230d0d0d&apiKey=${myAPIKey}`,
            iconSize: [2*zoom, 2*zoom], // size of the icon
            iconAnchor: [zoom, zoom], // point of the icon which will correspond to marker's location
            popupAnchor: [0, -zoom]// point from which the popup should open relative to the iconAnchor
          });
        
        fireStationIcon = new L.icon({
          iconUrl: `https://api.geoapify.com/v1/icon/?type=material&color=%23000000&size=small&icon=landmark&iconType=awesome&iconSize=small&textSize=small&apiKey=${myAPIKey}`,
          iconSize: [2*zoom, 2*zoom], // size of the icon
          iconAnchor: [zoom, zoom], // point of the icon which will correspond to marker's location
          popupAnchor: [0, -zoom]// point from which the popup should open relative to the iconAnchor
        });
    }
    

    onMapClick(e){
        let Fire = myFireApp.getFireAt(e.latlng.lat,e.latlng.lng);
        let FireStation = myFireStationApp.getFireStationAt(e.latlng.lat,e.latlng.lng);
        let Vehicle = myVehicleApp.getVehicleAt(e.latlng.lat,e.latlng.lng);
        let string ="";

        if(Fire != null){
            string += myFireApp.ToString(Fire);
        }
        if(FireStation != null){
            string += myFireStationApp.ToString(FireStation);
        }
        if(Vehicle != null){
            string += myVehicleApp.ToString(Vehicle);
        }

        if(string != ""){
            L.popup(
                {
                    maxHeight:300
                }
            )
            .setLatLng(e.latlng)
            .setContent(string)
            .openOn(this);
        }

    }

    hideDisplay(idElem,display) {
        let res = document.getElementById(idElem);
        if (!display) {
        res.childNodes.forEach(c =>
                c.checked=false
        )
        }
        else {
            res.childNodes.forEach(c =>
                c.checked=true
            )
        }
    } 
}