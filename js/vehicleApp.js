class VehicleApp{

    constructor(markersVehicle,markersVehiclePath,ListVehicleVisible,ListVehicle){
        this.markersVehicle = markersVehicle;
        this.markersVehiclePath = markersVehiclePath;
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
                case 'WATER_TENDERS':
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

    ToString(vehicle){
        //lancer tracer destination si etat occupé
       return  `<h3> Véhicule n°` + vehicle.id + `</h3>` +
               `<p> Type: ` + vehicle.type + `&nbsp --- &nbsp Caserne associée: ` + vehicle.facilityRefID + `</p>` +
               `<p> Latitude/Longitude: (` + vehicle.lat + `,`  + vehicle.lon + `) &nbsp --- Efficiency: ` + vehicle.efficiency  + `</p>` +
               `<p> Liquid : &nbsp Type: ` + vehicle.liquidType + `&nbsp --- Quantity: ` + vehicle.liquidQuantity + `&nbsp --- Consumption: ` + vehicle.liquidConsumption + `</p>`+
               `<p> Fuel: &nbsp Quantity:` + vehicle.fuel + `&nbsp --- Consumption: ` + vehicle.fuelConsumption + `</p>`+
               `<p> Crew Member: &nbsp Quantity: ` + vehicle.crewMember + `&nbsp --- Capacity: ` + vehicle.crewMemberCapacity + `</p>` +
               `<button onclick="myVehicleApp.Update(${vehicle.id},${vehicle.lon},${vehicle.lat},\'${vehicle.type}\',
               ${vehicle.liquidQuantity},\'${vehicle.liquidType}\',${vehicle.fuel},${vehicle.crewMember},${vehicle.facilityRefID});
               this.parentNode.parentNode.parentNode.remove();">Update Vehicle</button>`+
               `<button onclick="myVehicleApp.Delete(${vehicle.id});this.parentNode.parentNode.parentNode.remove();">Delete Vehicle</button>`;
       }

    setListVehicle(ListVehicle){
        this.ListVehicle = ListVehicle;
    }

    getMarkersVehicle(){
        return this.markersVehicle;
     }

    getMarkersVehiclePath(){
    return this.markersVehiclePath;
    }

    addPath(ListCoord){
        L.polyline(ListCoord).addTo(this.markersVehicle);
    }

    createUpdate(cU,id){

        let data = {};

        let type = document.getElementById("type").value;
        let lt = document.getElementById("lt").value;
        let fRID = document.getElementById("fRID").value;

        if (type != "")    {data["type"] = type;}
        if (lt != "")    {data["liquidType"] = lt;}
        if (fRID != "")    {data["facilityRefID"] = fRID; }
        if(!cU){
            data["id"]=id;
            let lq = document.getElementById("lq").value;
            let f = document.getElementById("f").value;
            let cm = document.getElementById("cm").value;
            let lon = document.getElementById("lon").value;
            let lat = document.getElementById("lat").value;
            if (lon != "")    {data["lon"] = lon;}
            if (lat != "")    {data["lat"] = lat;}
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
        <h2> Création de véhicule </h2>
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
        <br/>
        <select id="lt" name="lt">
          <option value="ALL">All</option>
          <option value="WATER">Water</option>
          <option value="WATER_WITH_ADDITIVES">Water with Additives</option>
          <option value="CARBON_DIOXIDE">Carbon Dioxide</option>
          <option value="POWDER">Powder</option>
        </select>
        </br></br>
        <button type="submit">Create</button>
        <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>
      </div>
  </form>`;
  document.getElementById('id01').style.display='block';
    }

    Update(id,lon,lat,type,liquidQuantity,liquidType,fuel,crewMember,facilityRefID){
        document.getElementById('updateForm').innerHTML=

         `<form class="modal-content animate" action="javascript:;" onsubmit="myVehicleApp.createUpdate(false,${id})">`+
            `<div class="container">`+
             `<h2> Modification du véhicule </h2>` +
              `<label for="lon"><b>Longitude</b></label>`+
              `<input type="double" name="lon" id="lon" placeholder=${lon}>`+
              `<label for="lat"><b>Latitude</b></label>`+
              `<input type="double" name="lat" id="lat" placeholder=${lat}>`+
             `<br/>`+
              `<label for="type"><b>Type</b></label>
              <br/>
              <select id="type" name="type">
                 <option value="CAR">Car</option>
                 <option value="FIRE_ENGINE">Fire Engine</option>
                 <option value="PUMPER_TRUCK">Pumper Truck</option>
                 <option value="WATER_TENDERS">Water Tender</option>
                 <option value="TURNTABLE_LADDER_TRUCK">Turntable Ladder Truck</option>
                 <option value="TRUCK">Truck</option>
              </select>
              <br/>`+
              `<label for="LiquidType"><b>LiquidType</b></label>
                <br/>
                <select id="lt" name="lt">
                    <option value="ALL">All</option>
                    <option value="WATER">Water</option>
                    <option value="WATER_WITH_ADDITIVES">Water with Additives</option>
                    <option value="CARBON_DIOXIDE">Carbon Dioxide</option>
                    <option value="POWDER">Powder</option>
             </select> </br></br>` +
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
      
      
              `<button type="submit">Update</button>
              <button type="button" onclick="document.getElementById('id02').style.display='none'" class="cancelbtn">Cancel</button>
            </div>
            </form>`; 
            document.getElementById('id02').style.display='block';
    }

    Delete(id){
        DeleteVehicle(id);
    }
}