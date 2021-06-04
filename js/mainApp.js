const myFireApp = new FireApp(L.featureGroup(),Array(),getListFire());

const myVehicleApp = new VehicleApp(L.featureGroup(),Array(),getListVehicle());

const mydisplayApp = new displayApp();
const myFireStationApp = new FireStationApp(L.featureGroup(),getListFireStation())


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

window.onload = function(){
    Display();
    funDefault();
}
window.onclick = function(event){
    Display();
}

window.setInterval(function(){
  Display();
}, 1000);

function Display(){
    myFireApp.setListFire(getListFire());
    myVehicleApp.setListVehicle(getListVehicle());
    myFireStationApp.setListFireStation(getListFireStation());

    mydisplayApp.resetMarker(myFireApp.getMarkers());
    mydisplayApp.resetMarker(myVehicleApp.getMarkers());
    mydisplayApp.resetMarker(myFireStationApp.getMarkers());

    myFireApp.FireFilter();
    myVehicleApp.VehicleFilter();
    
    mydisplayApp.addTo(myFireApp.getMarkers());
    mydisplayApp.addTo(myFireStationApp.getMarkers());
    mydisplayApp.addTo(myVehicleApp.getMarkers());
}

function funDefault() {
    var create = document.createElement("button");
    create.appendChild(document.createTextNode('Create Vehicle'));
    document.getElementById('content_vehicle').appendChild(create);

    var delAll = document.createElement("button");
    delAll.appendChild(document.createTextNode('Delete All Vehicles'));
    document.getElementById('content_vehicle').appendChild(delAll);

    create.onclick = function(){
        CreateVehicle(JSON.stringify({}))
    };
    delAll.onclick = DeleteAllVehicles;

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