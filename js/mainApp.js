const myFireApp = new FireApp(L.featureGroup(),Array(),getListFire());
const myVehicleApp = new VehicleApp(L.featureGroup(),Array(),getListVehicle());
const mydisplayApp = new displayApp(L.map(document.getElementById("mapid")).setView([45.752433, 4.834328], 10));


window.onload = function(){
    Display();
}
window.onchange = function(){
    myFireApp.setListFire(getListFire());
    myVehicleApp.setListVehicle(getListVehicle());
    Display();
}

function Display(){
    mydisplayApp.resetMarker(myFireApp.getMarkers())
    mydisplayApp.resetMarker(myVehicleApp.getMarkers())
    myFireApp.FireFilter();
    myVehicleApp.VehicleFilter();
    mydisplayApp.addTo(myFireApp.getMarkers())
    mydisplayApp.addTo(myVehicleApp.getMarkers())
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