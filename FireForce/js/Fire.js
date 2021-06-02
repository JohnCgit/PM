var ListFireVisible = Array();
var markersFire = L.featureGroup();
mymap.addLayer(markersFire);

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

function AddFire(Fire){
    var circle = L.circle([Fire.lat,Fire.lon], {
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.5,
        radius: Fire.range
    }).addTo(markersFire);
}

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

window.onload = function() {
    ResetMarkers();
    displayFire();

};

window.onchange = function() {
    ResetMarkers();
    displayFire();
    // ListFireVisible = Array();
    
    // affichagePostFiltre(ListFire);
    
    // console.log(ListFireVisible);

};