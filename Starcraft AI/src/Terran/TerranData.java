package Terran;
import java.util.HashMap;

import bwapi.Unit;
import bwapi.UnitType;
import main.Data;

public class TerranData extends Data{
	public TerranData(){
		super();
		workerType = UnitType.Terran_SCV;
		baseType = UnitType.Terran_Command_Center;
		gasType = UnitType.Terran_Refinery;
		UnitType[] armyT = new UnitType[]{UnitType.Terran_Marine,UnitType.Terran_Medic,UnitType.Terran_Siege_Tank_Tank_Mode,
				UnitType.Terran_Siege_Tank_Siege_Mode,UnitType.Terran_Vulture,UnitType.Terran_Goliath,
				UnitType.Terran_Wraith,UnitType.Terran_Battlecruiser,UnitType.Terran_Science_Vessel};
		UnitType[] allUnit = new UnitType[]{UnitType.Terran_Supply_Depot,
				UnitType.Terran_Command_Center,UnitType.Terran_Academy,UnitType.Terran_Comsat_Station
				,UnitType.Terran_SCV,UnitType.Terran_Refinery,UnitType.Terran_Machine_Shop,UnitType.Terran_Control_Tower
				,UnitType.Terran_Science_Facility,UnitType.Terran_Physics_Lab,UnitType.Terran_Armory};
		UnitType[] trainArmyB = new UnitType[]{UnitType.Terran_Barracks,UnitType.Terran_Factory,UnitType.Terran_Starport};
		for(UnitType u : armyT){
			armyType.add(u);
		}
		//initialize myUnits
		for(UnitType u : allUnit){
			myUnits.put(u, new HashMap<Integer,Unit>());
			underConstructing.put(u, new HashMap<Integer,Unit>());
		}
		for(UnitType u : armyT){
			myUnits.put(u, new HashMap<Integer,Unit>());
			underConstructing.put(u, new HashMap<Integer,Unit>());
		}
		for(UnitType u : trainArmyB){
			myUnits.put(u, new HashMap<Integer,Unit>());
			underConstructing.put(u,new HashMap<Integer,Unit>());
		}
		//initialize trainArmyBuilding
		for(UnitType u : trainArmyB){
			trainArmyBuilding.add(u);
		}
	}
}
