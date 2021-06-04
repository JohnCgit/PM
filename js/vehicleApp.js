const vehicleIcon = L.icon({
    iconUrl: `https://api.geoapify.com/v1/icon/?type=circle&color=%23000000&size=small&icon=taxi&iconType=awesome&iconSize=large&shadowColor=%230d0d0d&apiKey=${myAPIKey}`,
    iconSize: [20, 20], // size of the icon
    iconAnchor: [10, 10], // point of the icon which will correspond to marker's location
    popupAnchor: [0, -10]// point from which the popup should open relative to the iconAnchor
  });

class VehicleApp{

    constructor(markersVehicle,ListVehicleVisible,ListVehicle){
        this.markersVehicle = markersVehicle;
        this.ListVehicle = ListVehicle;
        this.ListVehicleVisible = ListVehicleVisible;
    }

    VehicleFilter(){
        let typeC = document.getElementById("car").checked;
        let typeFE = document.getElementById("fireengine").checked;
        let typePT = document.getElementById("pumpertruck").checked;
        let typeWT = document.getElementById("watertender").checked;
        let typeLT = document.getElementById("laddertruck").checked;
        let typeT = document.getElementById("truck").checked;

        this.ListVehicleVisible=Array();
        this.ListVehicle.forEach(Vehicle=>{
            let affiche=true;
            switch(Vehicle.type){
                case 'CAR':
                    if(!typeC){
                        affiche=false; 
                    }
                    break;
    
                case 'FIRE_ENGINE':
                    if(!typeFE){
                        affiche=false; 
                    }
                    break;
                case 'PUMPER_TRUCK':
                    if(!typePT){
                        affiche=false; 
                    }
                    break;
                case 'WATER_TENDER':
                    if(!typeWT){
                        affiche=false; 
                    }
                    break;
                case 'TURNTABLE_LADDER_TRUCK':
                    if(!typeLT){
                        affiche=false; 
                    }
                    break;
                case 'TRUCK':
                    if(!typeT){
                        affiche=false; 
                    }
                    break;
                default:
                    affiche = true;
            }
            
            if(affiche){
                this.AddVehicle(Vehicle);
                this.ListVehicleVisible.push(Vehicle);
            }
        })
        
    }

    AddVehicle(Vehicle){
          L.marker([Vehicle.lat,Vehicle.lon], {
            icon: vehicleIcon
          }).addTo(this.markersVehicle);
    }

    getVehicleAt(lat,lng){
        let vehicle = null;
        let radius = 50;
        this.ListVehicleVisible.forEach(Vehicle => {
            if(getDistanceFromLatLonInKm(Vehicle.lat,Vehicle.lon,lat,lng)*1000 <= radius)
            {
                vehicle = Vehicle;
            }
        });
        return vehicle;    
    }

    ToString(vehicle,popup){
         this.infoshort(popup,vehicle.id, vehicle.type, vehicle.facilityRefID, vehicle.liquidType, vehicle.lon, vehicle.lat, vehicle.efficiency,
            vehicle.liquidQuantity, vehicle.liquidConsumption,vehicle.fuel,vehicle.fuelConsumption,vehicle.crewMember,vehicle.crewMemberCapacity);
    }

    infoshort(popup, id,type,facilityRefID,liquidType,lon,lat,efficiency,liquidQuantity,liquidConsumption,fuel,fuelConsumption,crewMember,crewMemberCapacity){
        popup.setContent('<p>Véhicule n°'+id+'</p>' +
        `<p> Type: ` + type + `</p>` +
        `<p> Caserne: ` + facilityRefID + `</p>` +
        `<p> Liquid Type: ` + liquidType + `</p>` +
        `<button onclick="myVehicleApp.info(this.parentNode.parentNode.parentNode,${id},${type},${facilityRefID},${lon},${lat},${efficiency},
            ${liquidQuantity},${liquidConsumption},${fuel},${fuelConsumption},${crewMember},${crewMemberCapacity});">More Details</button>` +
        `<button onclick="myVehicleApp.Update(${id},${lon},${lat},${type},${efficiency},
            ${liquidQuantity},${liquidConsumption},${fuel},${fuelConsumption},${crewMember},${crewMemberCapacity},${facilityRefID});
        this.parentNode.parentNode.parentNode.remove();">Update Vehicle</button>`+
        `<button onclick="myVehicleApp.Delete(${id});this.parentNode.parentNode.parentNode.remove();">Delete Vehicle</button>`);
    }

//,${vehicle.liquidType}


    info(popup, id, type, caserne, lt,  lon, lat, eff, lq, lc, f, fc, cm, cmc){
        popup.firstChild.setContent(`<h3> Véhicule n°` + id + `</h3>` +
                                `<p> Type: ` + type + `&nbsp --- &nbsp Caserne associée: ` + caserne + `</p>` +
                                `<p> Latitude/Longitude: (` + lat + `,`  + lon + `) &nbsp --- Efficiency: ` + eff  + `</p>` +
                                `<p> Liquid : &nbsp Type: ` + lt + `&nbsp --- Quantity: ` + lq + `&nbsp --- Consumption: ` + lc + `</p>`+
                                `<p> Fuel: &nbsp Quantity:` + f + `&nbsp --- Consumption: ` + fc + `</p>`+
                                `<p> Crew Member: &nbsp Quantity: ` + cm + `&nbsp --- Capacity: ` + cmc + `</p>` +
                                `<button onclick="myVehicleApp.infoshort(${id},${type},${caserne},${lt},${lon},${lat},${eff},
                                ${lq},${lc},${f},${fc},${cm},${cmc});">Less Details</button>`);
    }

    setListVehicle(ListVehicle){
        this.ListVehicle = ListVehicle;
    }

    getMarkers(){
        return this.markersVehicle;
     }
    createUpdate(cU,id){

        let data = {};
        let lon = document.getElementById("lon").value;
        let lat = document.getElementById("lat").value;
        let type = document.getElementById("type").value;
        //let lt = document.getElementById("lt").value;
        let fRID = document.getElementById("fRID").value;
        if (lon != "")    {data["lon"] = lon;}
        if (lat != "")    {data["lat"] = lat;}
        if (type != "")    {data["type"] = type;}
        //if (lt != "")    {data["liquidType"] = lt;}
        if (fRID != "")    {data["facilityRefID"] = fRID; }
        if(!cU){
            data["id"]=id;
            let lq = document.getElementById("lq").value;
            let f = document.getElementById("f").value;
            let cm = document.getElementById("cm").value;
            if (lq != "")    {data["liquidQuantity"] = lq;}
            if (f != "")    {data["fuel"] = f;}
            if (cm != "")    {data["crewMember"] = cm;}
        }

        if(cU){
            CreateVehicle(JSON.stringify(data));
            document.getElementById('createForm').innerHTML="";
            document.getElementById('id01').style.display='none';
        }
        else{
            UpdateVehicle(id,JSON.stringify(data));
            document.getElementById('updateForm').innerHTML="";
            document.getElementById('id02').style.display='none';
        }
    }

    Create(){
        document.getElementById("createForm").innerHTML=
        `<form class="modal-content animate" action="javascript:;" onsubmit="myVehicleApp.createUpdate(true)">
      <div class="container">
        <label for="facilityRefID"><b>facilityRefID</b></label>
        <input type="int" name="facilityRefID" id="fRID" >
        <label for="type"><b>Type</b></label>
        <br/>
        <select id="type" name="type">
          <option value="CAR">Car</option>
          <option value="FIRE_ENGINE">Fire Engine</option>
          <option value="PUMPER_TRUCK">Pumper Truck</option>
          <option value="WATER_TENDERS">Water Tender</option>
          <option value="TURNTABLE_LADDER_TRUCK">Turntable Ladder Truck</option>
          <option value="TRUCK">Truck</option>
        </select>
        <br/>
        <label for="LiquidType"><b>LiquidType</b></label>
        <input type="double" name="LiquidType" id="lt" >
        <button type="submit">Create</button>
        <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>
      </div>
  </form>`;
  document.getElementById('id01').style.display='block';
    }
//,liquidType
    Update(id,lon,lat,type,liquidQuantity,fuel,crewMember,facilityRefID){
        document.getElementById('updateForm').innerHTML=

         `<form class="modal-content animate" action="javascript:;" onsubmit="myVehicleApp.createUpdate(false,${id})">`+
            `<div class="container">`+
              `<label for="lon"><b>Longitude</b></label>`+
              `<input type="double" name="lon" id="lon" placeholder=${lon}>`+
              `<label for="lat"><b>Latitude</b></label>`+
              `<input type="double" name="lat" id="lat" placeholder=${lat}>`+
             `<br/>`+
              `<label for="type"><b>Type</b></label>
              <input type="text" name="type" id="type" placeholder=${type}>`+
              `<br/>`+
              //`<label for="LiquidType"><b>LiquidType</b></label>`+
            //`<input type="double" name="LiquidType" id="lt" placeholder=${liquidType}>`+
              `<label for="LiquidQuantity"><b>LiquidQuantity</b></label>`+
              `<input type="double" name="LiquidQuantity" id="lq" placeholder=${liquidQuantity}>`+
              `<br/>`+
              `<label for="fuel"><b>fuel</b></label>`+
              `<input type="double" name="fuel" id="f" placeholder=${fuel}>`+
              `<br/>`+
              `<label for="CrewMember"><b>CrewMember</b></label>`+
              `<input type="int" name="CrewMember" id="cm" placeholder=${crewMember}>`+
              `<br/>`+
              `<label for="facilityRefID"><b>facilityRefID</b></label>`+
              `<input type="int" name="facilityRefID" id="fRID" placeholder=${facilityRefID}>`+
      
      
              `<button type="submit">Update</button>`+

              +
              `<button type="button" onclick="document.getElementById('id02').style.display='none'" class="cancelbtn">Cancel</button>` + 
              
            `</div>`+
          
            `</form>`; 
            document.getElementById('id02').style.display='block';
    }

    Delete(id){
        DeleteVehicle(id);
    }
}