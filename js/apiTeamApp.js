let port = 8081;
let host = `http://127.0.0.1:${port}`;

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
    return JSON.parse(loadRessource(`${host}/fire/getAll`,"GET"));
};


function getFireStationById(id){
    //return JSON.parse(loadRessource(`${host}/fireStation/${id}`,"GET"));
    return Array();
};

function getListFireStation(){
    //return JSON.parse(loadRessource(`${host}/fireStation/getAll`,"GET"));
    return Array();
};

function getVehicleById(id){
    return JSON.parse(loadRessource(`${host}/vehicle/get/${id}`,"GET"));
};

function getListVehicle(){
    return JSON.parse(loadRessource(`${host}/vehicle/getAll`,"GET"));
};

function CreateVehicle(data) {
    return JSON.parse(loadRessource(`${host}/vehicle/create`, "POST", data));
}

function DeleteAllVehicles(){
    let ListVehicle = getListVehicle();
    ListVehicle.forEach(Vehicle => {
         DeleteVehicle(Vehicle.id);
         }
    )  
}

function DeleteVehicle(id) {
    loadRessource(`${host}/vehicle/delete/${id}`, "DELETE");        
}

function UpdateVehicle(id, data) {
    return JSON.parse(loadRessource(`${host}/vehicle/update/${id}`, "PUT", data));
}
