class FireApp{
    constructor(markersFire,ListFireVisible,ListFire){
        this.markersFire = markersFire;
        this.ListFire = ListFire;
        this.ListFireVisible = ListFireVisible;
    }

    FireFilter(){
        let typeA = document.getElementById("typeA").checked;
        let typeBG = document.getElementById("typeB_Gasoline").checked;
        let typeBA = document.getElementById("typeB_Alcohol").checked;
        let typeBP = document.getElementById("typeB_Plastics").checked;
        let typeC = document.getElementById("typeC_Flammable_Gases").checked;
        let typeD = document.getElementById("typeD_Metals").checked;
        let typeE = document.getElementById("typeE_Electric").checked;
        let I = document.getElementById("i").value;
        let R = document.getElementById("r").value;
        this.ListFireVisible=Array();
        this.ListFire.forEach(Fire=>{
            let affiche=true;
            switch(Fire.type){
                case 'A':
                    if(!typeA){
                        affiche=false; 
                    }
                    break;
    
                case 'B_Gasoline':
                    if(!typeBG){
                        affiche=false; 
                    }
                    break;
                case 'B_Alcohol':
                    if(!typeBA){
                        affiche=false; 
                    }
                    break;
                case 'B_Plastics':
                    if(!typeBP){
                        affiche=false; 
                    }
                    break;
                case 'C_Flammable_Gases':
                    if(!typeC){
                        affiche=false; 
                    }
                    break;
                case 'D_Metals':
                    if(!typeD){
                        affiche=false; 
                    }
                    break;
                case 'E_Electric':
                    if(!typeE){
                        affiche=false; 
                    }
                    break;
            }
            
            if(Fire.range>R){
                affiche=false; 
            } 
            if(Fire.intensity>I){
                affiche=false; 
            }
            if(affiche){
                AddFire(Fire);
                this.ListFireVisible.push(Fire);
            }
        })
        
    }

    AddFire(Fire){
        L.circle([Fire.lat,Fire.lon], {
            color: 'red',
            fillColor: '#f03',
            fillOpacity: 0.5,
            radius: Fire.range
        }).addTo(this.markersFire);
    }

    getFireAt(lat,lng){
        let fire = null;
        this.ListFireVisible.forEach(Fire => {
            if(getDistanceFromLatLonInKm(Fire.lat,Fire.lon,lat,lng)*1000 <= Fire.range)
            {
                fire = Fire;
            }
        });
        return fire;    
    }

    ToString(fire){
        return "Le feu a la position ("+fire.lon+";"+fire.lat+") est d'intensité:"
        +fire.intensity+", s'étend sur un rayon de:"+fire.range+"m et il est de type:"+fire.type;

    }

    setListFire(ListFire){
        this.ListFire = ListFire;
    }
}
