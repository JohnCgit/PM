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
                case 'typeC':
                    if(!typeC){
                        affiche=false; 
                    }
                    break;
    
                case 'typeFE':
                    if(!typeFE){
                        affiche=false; 
                    }
                    break;
                case 'typePT':
                    if(!typePT){
                        affiche=false; 
                    }
                    break;
                case 'typeWT':
                    if(!typeWT){
                        affiche=false; 
                    }
                    break;
                case 'typeLT':
                    if(!typeLT){
                        affiche=false; 
                    }
                    break;
                case 'typeT':
                    if(!typeT){
                        affiche=false; 
                    }
                    break;
            }
            
            if(affiche){
                AddVehicle(Vehicle);
                this.ListVehicleVisible.push(Vehicle);
            }
        })
        
    }

    AddVehicle(Vehicle){
        L.circle([Vehicle.lat,Vehicle.lon], {
            color: 'red',
            fillColor: '#f03',
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
        '<button onclick="DeleteVehicle(vehicle.id)">Delete Vehicle</button>';
    }

    setListVehicle(ListVehicle){
        this.ListVehicle = ListVehicle;
    }
}