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
        L.circle([Vehicle.lat,Vehicle.lon], {
            color: 'blue',
            fillColor: '#00f',
            fillOpacity: 0.5,
            radius: 50
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
        console.log(vehicle);
        return '<p>Véhicule n°'+vehicle.id+'</p>' +
        `<input type=text value=${vehicle.type}>`+
        `<button onclick="myVehicleApp.Update(${vehicle.id},${vehicle.lon},${vehicle.lat},${vehicle.type},${vehicle.efficiency},
            ${vehicle.liquidQuantity},${vehicle.liquidConsumption},${vehicle.fuel},${vehicle.fuelConsumption},${vehicle.crewMember},${vehicle.crewMemberCapacity},${vehicle.facilityRefID});
        this.parentNode.parentNode.parentNode.remove();">Update Vehicle</button>`+
        `<button onclick="myVehicleApp.Delete(${vehicle.id});this.parentNode.parentNode.parentNode.remove();">Delete Vehicle</button>`;
    }
//,${vehicle.liquidType}
    setListVehicle(ListVehicle){
        this.ListVehicle = ListVehicle;
    }

    getMarkers(){
        return this.markersVehicle;
     }

    createUpdate(cU,id){
        
        let lon = document.getElementById("lon").value;
        let lat = document.getElementById("lat").value;
        let type = document.getElementById("type").value;
        let eff = document.getElementById("eff").value;
        let lt = document.getElementById("lt").value;
        let lq = document.getElementById("lq").value;
        let lc = document.getElementById("lc").value;
        let f = document.getElementById("f").value;
        let fc = document.getElementById("fc").value;
        let cm = document.getElementById("cm").value;
        let cmc = document.getElementById("cmc").value;
        let fRID = document.getElementById("fRID").value;

        let data = {};

        if (lon != "")    {data["lon"] = lon;}
        if (lat != "")    {data["lat"] = lat;}
        if (type != "")    {data["type"] = type;}
        if (eff != "")    {data["efficiency"] = eff;}
        if (lt != "")    {data["liquidType"] = lt;}
        if (lq != "")    {data["liquidQuantity"] = lq;}
        if (lc != "")    {data["liquidConsumption"] = lc;}
        if (f != "")    {data["fuel"] = f;}
        if (fc != "")    {data["fuelConsumption"] = fc;}
        if (cm != "")    {data["crewMember"] = cm;}
        if (cmc != "")    {data["crewMemberCapacity"] = cmc;}
        if (fRID != "")    {data["facilityRefID"] = fRID; }
        
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
        <label for="lon"><b>Longitude</b></label>
        <input type="double" name="lon" id="lon" >
        <label for="lat"><b>Latitude</b></label>
        <input type="double" name="lat" id="lat" >
        <br/>
        <label for="type"><b>Type</b></label>
        <input type="text" name="type" id="type" >
        <label for="Efficiency"><b>Efficiency</b></label>
        <input type="double" name="Efficiency" id="eff" >
        <br/>
        <label for="LiquidType"><b>LiquidType</b></label>
        <input type="double" name="LiquidType" id="lt" >
        <label for="LiquidQuantiy"><b>LiquidQuantiy</b></label>
        <input type="double" name="LiquidQuantiy" id="lq" >
        <br/>
        <label for="LiquidConsumption"><b>LiquidConsumption</b></label>
        <input type="double" name="LiquidConsumption" id="lc" >
        <br/>
        <label for="fuel"><b>fuel</b></label>
        <input type="double" name="fuel" id="f" >
        <label for="fuelConsumption"><b>fuelConsumption</b></label>
        <input type="double" name="fuelConsumption" id="fc" >
        <br/>
        <br/>
        <label for="CrewMember"><b>CrewMember</b></label>
        <input type="int" name="CrewMember" id="cm" >
        <label for="CrewMemberCapacity"><b>CrewMemberCapacity</b></label>
        <input type="int" name="CrewMemberCapacity" id="cmc" >
        <br/>
        <label for="facilityRefID"><b>facilityRefID</b></label>
        <input type="int" name="facilityRefID" id="fRID" >


        <button type="submit">Create</button>
      </div>

      <div class="container" style="background-color:#f1f1f1">
        <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>
      </div>
  </form>`;
  document.getElementById('id01').style.display='block';
    }
//,liquidType
    Update(id,lon,lat,type,efficiency,liquidQuantity,liquidConsumption,fuel,fuelConsumption,crewMember,crewMemberCapacity,facilityRefID){
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
              `<label for="Efficiency"><b>Efficiency</b></label>`+
              `<input type="double" name="Efficiency" id="eff" placeholder=${efficiency}>`+
              `<br/>`+
              //`<label for="LiquidType"><b>LiquidType</b></label>`+
            //`<input type="double" name="LiquidType" id="lt" placeholder=${liquidType}>`+
              `<label for="LiquidQuantiy"><b>LiquidQuantiy</b></label>`+
              `<input type="double" name="LiquidQuantiy" id="lq" placeholder=${liquidQuantity}>`+
              `<br/>`+
              `<label for="LiquidConsumption"><b>LiquidConsumption</b></label>`+
              `<input type="double" name="LiquidConsumption" id="lc" placeholder=${liquidConsumption}>`+
              `<br/>`+
              `<label for="fuel"><b>fuel</b></label>`+
              `<input type="double" name="fuel" id="f" placeholder=${fuel}>`+
              `<label for="fuelConsumption"><b>fuelConsumption</b></label>`+
              `<input type="double" name="fuelConsumption" id="fc" placeholder=${fuelConsumption}>`+
              `<br/>`+
              `<br/>`+
              `<label for="CrewMember"><b>CrewMember</b></label>`+
              `<input type="int" name="CrewMember" id="cm" placeholder=${crewMember}>`+
              `<label for="CrewMemberCapacity"><b>CrewMemberCapacity</b></label>`+
              `<input type="int" name="CrewMemberCapacity" id="cmc" placeholder=${crewMemberCapacity}>`+
              `<br/>`+
              `<label for="facilityRefID"><b>facilityRefID</b></label>`+
              `<input type="int" name="facilityRefID" id="fRID" placeholder=${facilityRefID}>`+
      
      
              `<button type="submit">Update</button>`+
            `</div>`+
            `<div class="container" style="background-color:#f1f1f1">`+
            `<button type="button" onclick="document.getElementById('id02').style.display='none'" class="cancelbtn">Cancel</button>`+
  
            `</form>`; 
            document.getElementById('id02').style.display='block';
    }
    Delete(id){
        DeleteVehicle(id);
    }
}