var port = 8081;

function loadRessource(source, method, body) {
    var xhttp = new XMLHttpRequest();
    xhttp.open(method, source, false);
    xhttp.overrideMimeType("application/json");
    if (body != null) {
        xhttp.setRequestHeader("Content-type", "application/json")
        xhttp.send(body);
    }
    else {
        xhttp.send();
    }
    if (xhttp.status == 200) {
        return xhttp.response;
    }
};

function funDefault() {
    var create = document.createElement("button");
    create.appendChild(document.createTextNode('Create Vehicle'));
    document.getElementById('content_vehicle').appendChild(create);

    var delAll = document.createElement("button");
    delAll.appendChild(document.createTextNode('Delete All Vehicles'));
    document.getElementById('content_vehicle').appendChild(delAll);

    var del = document.createElement("button");
    del.appendChild(document.createTextNode('Delete Vehicle'));
    document.getElementById('content_vehicle').appendChild(del);

    create.onclick = CreateVehicle;
    delAll.onclick = DeleteAllVehicles;
    del.onclick = DeleteVehicle;

}

function AddVehicle(Vehicle){
    var circle = L.circle([Vehicle.lat,Vehicle.lon], {
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.5,
        radius: 50
    }).addTo(mymap);
}

function CreateVehicle() {
    var data = JSON.stringify({});
    var res =loadRessource(`http://127.0.0.1:8081/vehicle`, "POST", data);
    console.log(JSON.parse(res).id);
}

function DeleteAllVehicles(){
    ListVehicle = JSON.parse(loadRessource('http://127.0.0.1:8081/vehicle',"GET"));
    ListVehicle.forEach(Vehicle => {
         vehicle = Vehicle;
         if (vehicle != null){
                loadRessource("http://127.0.0.1:8081/vehicle/" + vehicle.id, "DELETE");
         }
    });  
}

function DeleteVehicle(id) {
    ListVehicle = JSON.parse(loadRessource('http://127.0.0.1:8081/vehicle',"GET"));
    ListVehicle.forEach(Vehicle => {
         vehicle = Vehicle;
         if (vehicle.id == id){
                loadRessource("http://127.0.0.1:8081/vehicle/" + vehicle.id, "DELETE");
         }
    });  
}

function onMapClick(e) {
    var vehicle = null;
    var ListVehicle = JSON.parse(loadRessource('http://127.0.0.1:8081/vehicle',"GET"));
    ListVehicle.forEach(Vehicle => {
         vehicle = Vehicle;
    });  
    if (vehicle != null){
    popup
    .setLatLng(e.latlng)
    .setContent("Le véhicule à la position "+e.latlng.toString()+" est de type:" +fire.type)
    .openOn(mymap);
} 
}


function affichagePostFiltre(ListVehicle){
}


window.onload = function() {

    funDefault();

};

window.onchange = function() {
    ListVehicle = JSON.parse(loadRessource('http://127.0.0.1:8081/vehicle',"GET"));
    console.log(ListVehicle);
}
