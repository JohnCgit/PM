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

function getListFire(){
    return JSON.parse(loadRessource('http://127.0.0.1:8081/fire',"GET"));
};

function getListVehicle(){
    return JSON.parse(loadRessource('http://127.0.0.1:8081/vehicle',"GET"));
};

function CreateVehicle(data) {
    return JSON.parse(loadRessource(`http://127.0.0.1:8081/vehicle`, "POST", data));
}

function DeleteAllVehicles(){
    let ListVehicle = getListVehicle();
    ListVehicle.forEach(Vehicle => {
         DeleteVehicle(Vehicle.id);
         }
    )  
}

function DeleteVehicle(id) {
    loadRessource("http://127.0.0.1:8081/vehicle/" + id, "DELETE");        
}

function UpdateVehicle(id, data) {
    return JSON.parse(loadRessource(`http://127.0.0.1:8081/vehicle/` + id, "PUT", data));
}
