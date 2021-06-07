*Architecture BACKEND* : https://docs.google.com/presentation/d/1lYZFWvRvmOy5Ij_DgC-H95q_PKXF9jTXKnD1jUtFcRc/edit?usp=sharing

*Lien Trello :* https://trello.com/b/ugAGCoVi/projet-emergency

*Casernes de pompiers de la Métropole de Lyon - Point d'intérêt :* https://data.grandlyon.com/jeux-de-donnees/casernes-pompiers-metropole-lyon-point-interet/donnees

TODO:
  -move loadressources to API.js
  - try to create displayClass(map,markers[]):
          -addTo group(marker,arg)
          -reset group(marker)
          
          
          '''
     	public void update(int vehicleId, String content) throws IOException {
		Vehicule v = getVehicule(vehicleId);
		JsonNode jNode = this.mapper.readTree(content);
		

		
		if(jNode.get("fuel")!=null) {
			v.setFuel(jNode.get("fuel").asDouble());
		}
		
		if(jNode.get("lon")!=null) {
			v.setLon(jNode.get("lon").asDouble());
		}
		
		if(jNode.get("lat")!=null) {
			v.setLat(jNode.get("lat").asDouble());
		}
		
		if(jNode.get("crewMember")!=null) {
			v.setCrewMember(jNode.get("crewMember").asInt());
		}
		
		if(jNode.get("liquidQuantity")!=null) {
			v.setLiquidQuantity(jNode.get("liquidQuantity").asDouble());
		}
		
		if(jNode.get("facilityRefID")!=null) {
			v.setFacilityRefID(jNode.get("facilityRefID").asInt());
		}
		
		if(jNode.get("type").asText()!=null) {
			v.setType(VehiculeType.valueOf((jNode.get("type").asText())));
		}
		
		if(jNode.get("liquidType").asText()!=null) {
			v.setLiquidType(LiquidType.valueOf((jNode.get("liquidType").asText())));
		}
		
		
	}
'''

          
  
