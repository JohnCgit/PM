var port = 8081;

var mymap = L.map(document.getElementById("mapid")).setView([45.752433, 4.834328], 13);
    
L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
    maxZoom: 18,
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, ' +
        'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    id: 'mapbox/streets-v11',
    tileSize: 512,
    zoomOffset: -1
}).addTo(mymap);

var popup = L.popup();

function onMapClick(e) {
    var fire = null;
    var ListFire = JSON.parse(loadRessource('http://127.0.0.1:8081/fire',"GET"));
    ListFire.forEach(Fire => {
        if(getDistanceFromLatLonInKm(Fire.lat,Fire.lon,e.latlng.lat,e.latlng.lng)*1000 <= Fire.range)
        {
            fire = Fire;
        }
    });    
if (fire != null){
    popup
.setLatLng(e.latlng)
.setContent("Le feu a la position "+e.latlng.toString()+" est d'intensité:"+fire.intensity+", s'étend sur un rayon de:"+fire.range+"m et il est de type:"+fire.type)
.openOn(mymap);

} 
if (fire == null){
    popup
.setLatLng(e.latlng)
.setContent("il n'y a pas de feu a la position "+e.latlng.toString())
.openOn(mymap);

} 

}

mymap.on('click', onMapClick);
// Fonction permettant la récupération d'une information située à l'url source avec la méthode method
function loadRessource(source, method) {
    var xhttp = new XMLHttpRequest();
    xhttp.open(method, source, false);
    xhttp.overrideMimeType("application/json");
    xhttp.send();
    if (xhttp.status == 200) {
        return xhttp.response;
    }
};

function AddFire(Fire){
    var circle = L.circle([Fire.lat,Fire.lon], {
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.5,
        radius: Fire.range
    }).addTo(mymap);
}
window.onload = function() {
    var ListFire = JSON.parse(loadRessource('http://127.0.0.1:8081/fire',"GET"));
    affichagePostFiltre(ListFire);

};

function affichagePostFiltre(ListFire){
    var typeA = document.getElementById("typeA").checked;
    var typeBG = document.getElementById("typeB_Gasoline").checked;
    var typeBA = document.getElementById("typeB_Alcohol").checked;
    var typeBP = document.getElementById("typeB_Plastics").checked;
    var typeC = document.getElementById("typeC").checked;
    var typeD = document.getElementById("typeD").checked;
    var typeE = document.getElementById("typeE").checked;
    var I = document.location.searchParamns.get("i");
    var R = document.location.searchParamns.get("r");

    ListFire.forEach(Fire=>{
        var affiche=true;
        switch(Fire.type){
            case 'A':
                if(typeA){
                    affiche=false; 
                }
                break;

            case 'B_Gasoline':
                if(typeBG){
                    affiche=false; 
                }
                break;
            case 'B_Alcohol':
                if(typeBA){
                    affiche=false; 
                }
                break;
            case 'B_Plastics':
                if(typeBP){
                    affiche=false; 
                }
                break;
            case 'C_Flammable_Gases':
                if(typeC){
                    affiche=false; 
                }
                break;
            case 'D_Metals':
                if(typeD){
                    affiche=false; 
                }
                break;
            case 'E_Electric':
                if(typeE){
                    affiche=false; 
                }
                break;
        }
        
    })
}
//https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
function getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
    var R = 6371; // Radius of the earth in km
    var dLat = deg2rad(lat2-lat1);  // deg2rad below
    var dLon = deg2rad(lon2-lon1); 
    var a = 
      Math.sin(dLat/2) * Math.sin(dLat/2) +
      Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
      Math.sin(dLon/2) * Math.sin(dLon/2)
      ; 
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
    var d = R * c; // Distance in km
    return d;
  }
  
  function deg2rad(deg) {
    return deg * (Math.PI/180)
  }
  
  