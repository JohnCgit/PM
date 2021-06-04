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

        let myAPIKey = "ebb18e08352b474687513c0a6bb82f30";
        const markerIcon = L.icon({
            iconUrl: `https://api.geoapify.com/v1/icon/?type=circle&color=%23000000&size=small&icon=taxi&iconType=awesome&iconSize=large&shadowColor=%230d0d0d&apiKey=${myAPIKey}`,
            iconSize: [20, 20], // size of the icon
            iconAnchor: [10, 10], // point of the icon which will correspond to marker's location
          });
          L.marker([45.8,4.87], {
            icon: markerIcon
          }).addTo(this.map);
    }

    addTo(arg){
        arg.addTo(this.map);
    }

    resetMarker(arg){
        arg.clearLayers();
    }

    

    onMapClick(e){
        let Fire = myFireApp.getFireAt(e.latlng.lat,e.latlng.lng);
        let Vehicle = myVehicleApp.getVehicleAt(e.latlng.lat,e.latlng.lng);
        let string ="";
        if(Fire != null){
            string += myFireApp.ToString(Fire);
        }
        if(Vehicle != null){
            string += myVehicleApp.ToString(Vehicle);
        }

        if(string != ""){
            L.popup()
            .setLatLng(e.latlng)
            .setContent(string)
            .openOn(this);
        }

    }
    

}

