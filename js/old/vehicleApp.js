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
                    affiche=false;
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
        return '<p>Véhicule n°'+vehicle.id+'</p>' +
        '<p> Type: '+vehicle.type+'</p>' +
        `<button onclick="DeleteVehicle(${vehicle.id})">Delete Vehicle</button>`;
    }

    setListVehicle(ListVehicle){
        this.ListVehicle = ListVehicle;
    }

    getMarkers(){
        return this.markersVehicle;
     }

    create(){
        
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
        
        CreateVehicle(JSON.stringify(data));
    }
}