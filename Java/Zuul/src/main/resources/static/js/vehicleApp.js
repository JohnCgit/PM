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
        
        let DISPONIBLE = document.getElementById("DISPONIBLE").checked;
        let ALLER = document.getElementById("ALLER").checked;
		let EXTINCTION = document.getElementById("EXTINCTION").checked;
		let RETOUR = document.getElementById("RETOUR").checked;
		let NON_OPERATIONNAL = document.getElementById("NON_OPERATIONNAL").checked;

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

            switch(Vehicle.state){
                case 'DISPONIBLE':
                    if(!DISPONIBLE){
                        affiche=false; 
                    }
                    break;
    
                case 'ALLER':
                    if(!ALLER){
                        affiche=false; 
                    }
                    break;
                case 'EXTINCTION':
                    if(!EXTINCTION){
                        affiche=false; 
                    }
                    break;
                case 'RETOUR':
                    if(!RETOUR){
                        affiche=false; 
                    }
                    break;
                case 'NON_OPERATIONNAL':
                    if(!NON_OPERATIONNAL){
                        affiche=false; 
                    }
            }
            
            if(affiche){
                this.AddVehicle(Vehicle);
                //this.addPath(Vehicle.path);
                this.ListVehicleVisible.push(Vehicle);
            }
        })
        
    }

    AddVehicle(Vehicle){
          L.marker([Vehicle.lat,Vehicle.lon], {
            icon: vehicleIcon
          }).addTo(this.markersVehicle);
    }

    getVehiclesAt(lat,lng){
        let vehicle = Array();
        let radius = 50;
        this.ListVehicleVisible.forEach(Vehicle => {
            if(getDistanceFromLatLonInKm(Vehicle.lat,Vehicle.lon,lat,lng)*1000 <= radius)
            {
                vehicle.push(Vehicle);
            }
        });
        return vehicle;    
    }

    ToString(vehicle){
        //lancer tracer destination si etat occup??
        //this.addPath(Vehicle.path);
       return  `<h3> V??hicule n??` + vehicle.id + `</h3>` +
               `<p> Etat: ` + vehicle.state + ` &nbsp Type: ` + vehicle.type + `</p>`+
               `<p> Caserne: ` + vehicle.fireStationID+ `</p>`+
               `<p> Latitude/Longitude: (` + vehicle.lat + `,`  + vehicle.lon + `) &nbsp --- Efficiency: ` + vehicle.efficiency  + `</p>` +
               `<p> Liquid : &nbsp Type: ` + vehicle.liquidType + `&nbsp --- Quantity: ` + vehicle.liquidQuantity + `&nbsp --- Consumption: ` + vehicle.liquidConsumption + `</p>`+
               `<p> Fuel: &nbsp Quantity:` + vehicle.fuel + `&nbsp --- Consumption: ` + vehicle.fuelConsumption + `</p>`+
               `<p> Crew Member: &nbsp Quantity: ` + vehicle.crewMember + `&nbsp --- Capacity: ` + vehicle.vehicleCrewCapacity + `</p>` +
               `<button onclick="myVehicleApp.Update(${vehicle.id},${vehicle.lon},${vehicle.lat},\'${vehicle.type}\',
               ${vehicle.liquidQuantity},\'${vehicle.liquidType}\',${vehicle.fuel},${vehicle.crewMember},${vehicle.fireStationID});
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

        let type = document.getElementById("typeV").value;
        let lt = document.getElementById("lt").value;
        let fRID = document.getElementById("fRID").value;

        if (type != "")    {data["type"] = type;}
        if (lt != "")    {data["liquidType"] = lt;}
        if (fRID != "")    {data["fireStationID"] = fRID; }
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
        <h2> Cr??ation de v??hicule </h2>
        <label for="fireStationID"><b>fireStationID</b></label>
        <input type="int" name="fireStationID" id="fRID" >
        <label for="type"><b>Type</b></label>
        <br/>
        <select id="typeV" name="type">
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

    Update(id,lon,lat,type,liquidQuantity,liquidType,fuel,crewMember,fireStationID){

        let CarISsel ="";
        let FEISsel ="";
        let PTISsel="";
        let WTISsel="";
        let TLISsel="";
        let TISsel="";
        
        switch(type){
            case 'CAR':
                CarISsel = "selected";
            break;

            case 'FIRE_ENGINE':
                FEISsel = "selected";
            break;

            case 'PUMPER_TRUCK':
                PTISsel = "selected";
            break;

            case 'WATER_TENDERS':
                WTISsel = "selected";
            break;

            case 'TURNTABLE_LADDER_TRUCK':
                TLISsel = "selected";
            break;

            case 'TRUCK':
                TISsel = "selected";
            break;
        }
        let AllISsel ="";
        let WISsel ="";
        let WAISsel="";
        let CISsel="";
        let PISsel="";
        switch(liquidType){
            case 'ALL':
                AllISsel = "selected";
            break;

            case 'WATER':
                WISsel= "selected";
            break;

            case 'WATER_WITH_ADDITIVES':
                WAISsel= "selected";
            break;

            case 'CARBON_DIOXIDE':
                CISsel = "selected";
            break;

            case 'POWDER':
                PISsel = "selected";
            break;
        }


        document.getElementById('updateForm').innerHTML=

         `<form class="modal-content animate" action="javascript:;" onsubmit="myVehicleApp.createUpdate(false,${id})">`+
            `<div class="container">`+
             `<h2> Modification du v??hicule </h2>` +
              `<label for="lon"><b>Longitude</b></label>`+
              `<input type="double" name="lon" id="lon" value=${lon}>`+
              `<label for="lat"><b>Latitude</b></label>`+
              `<input type="double" name="lat" id="lat" value=${lat}>`+
             `<br/>`+
              `<label for="type"><b>Type</b></label>
              <br/>
              <select id="typeV" name="type">` +
                 `<option ${CarISsel} value="CAR">Car</option>
                 <option ${FEISsel} value="FIRE_ENGINE">Fire Engine</option>
                 <option ${PTISsel} value="PUMPER_TRUCK">Pumper Truck</option>
                 <option ${WTISsel} value="WATER_TENDERS">Water Tender</option>
                 <option ${TLISsel} value="TURNTABLE_LADDER_TRUCK">Turntable Ladder Truck</option>
                 <option ${TISsel} value="TRUCK">Truck</option>
              </select>
              <br/>`+
              `<label for="LiquidType"><b>LiquidType</b></label>
                <br/>
                <select id="lt" name="lt">` + 
                    `<option ${AllISsel} value="ALL">All</option>
                    <option ${WISsel} value="WATER">Water</option>
                    <option ${WAISsel} value="WATER_WITH_ADDITIVES">Water with Additives</option>
                    <option ${CISsel} value="CARBON_DIOXIDE">Carbon Dioxide</option>
                    <option ${PISsel} value="POWDER">Powder</option>
             </select> </br></br>` +
              `<label for="LiquidQuantity"><b>LiquidQuantity</b></label>`+
              `<input type="double" name="LiquidQuantity" id="lq" value=${liquidQuantity}>`+
              `<br/>`+
              `<label for="fuel"><b>fuel</b></label>`+
              `<input type="double" name="fuel" id="f" value=${fuel}>`+
              `<br/>`+
              `<label for="CrewMember"><b>CrewMember</b></label>`+
              `<input type="int" name="CrewMember" id="cm" value=${crewMember}>`+
              `<br/>`+
              `<label for="fireStationID"><b>fireStationID</b></label>`+
              `<input type="int" name="fireStationID" id="fRID" value=${fireStationID}>`+
      
      
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
