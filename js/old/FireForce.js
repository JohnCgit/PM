var port = 8081;
var ListFireVisible = Array();
var mymap = L.map(document.getElementById("mapid")).setView([45.752433, 4.834328], 10);
var markersFire = L.featureGroup();
mymap.addLayer(markersFire);

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
    ListFireVisible.forEach(Fire => {
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
    }).addTo(markersFire);
}

function ResetMarkers(){
    markersFire.clearLayers();
}

function FireFilter(ListFire){
    var typeA = document.getElementById("typeA").checked;
    var typeBG = document.getElementById("typeB_Gasoline").checked;
    var typeBA = document.getElementById("typeB_Alcohol").checked;
    var typeBP = document.getElementById("typeB_Plastics").checked;
    var typeC = document.getElementById("typeC_Flammable_Gases").checked;
    var typeD = document.getElementById("typeD_Metals").checked;
    var typeE = document.getElementById("typeE_Electric").checked;
    var I = document.getElementById("i").value;
    var R = document.getElementById("r").value;

    ListFire.forEach(Fire=>{
        var affiche=true;
        switch(Fire.type){
            case 'A':
                if(!typeA){
                    affiche=false; 
                }
                break;

            case 'B_Gasoline':
                if(!typeBG){
                    affiche=false; 
                }
                break;
            case 'B_Alcohol':
                if(!typeBA){
                    affiche=false; 
                }
                break;
            case 'B_Plastics':
                if(!typeBP){
                    affiche=false; 
                }
                break;
            case 'C_Flammable_Gases':
                if(!typeC){
                    affiche=false; 
                }
                break;
            case 'D_Metals':
                if(!typeD){
                    affiche=false; 
                }
                break;
            case 'E_Electric':
                if(!typeE){
                    affiche=false; 
                }
                break;
        }
        
        if(Fire.range>R){
            affiche=false; 
        } 
        if(Fire.intensity>I){
            affiche=false; 
        }
        if(affiche){
            AddFire(Fire);
            ListFireVisible.push(Fire);
        }
    })
    
}

function displayFire(){
    ListFireVisible=Array();
    var ListFire = JSON.parse(loadRessource('http://127.0.0.1:8081/fire',"GET"));
    FireFilter(ListFire);
}


  
  
window.onload = function() {
    ResetMarkers();
    displayFire();
};

window.onchange = function() {
    ResetMarkers();
    displayFire();
};