package Protoss;
import java.util.HashMap;

import bwapi.Unit;
import bwapi.UnitType;
import main.Data;

public class ProtossData extends Data{
	public ProtossData(){
		super();
		workerType = UnitType.Protoss_Probe;
		baseType = UnitType.Protoss_Nexus;
		gasType = UnitType.Protoss_Assimilator;
		UnitType[] armyT = new UnitType[]{UnitType.Protoss_Zealot,UnitType.Protoss_Dragoon,UnitType.Protoss_Dark_Archon,
				UnitType.Protoss_Archon,UnitType.Protoss_Dark_Templar,UnitType.Protoss_High_Templar,
				UnitType.Protoss_Carrier,UnitType.Protoss_Scout,UnitType.Protoss_Corsair,UnitType.Protoss_Arbiter
				,UnitType.Protoss_Reaver,UnitType.Protoss_Observer};
		UnitType[] allUnit = new UnitType[]{UnitType.Protoss_Pylon,
				UnitType.Protoss_Nexus,UnitType.Protoss_Cybernetics_Core,UnitType.Protoss_Templar_Archives
				,UnitType.Protoss_Probe,UnitType.Protoss_Assimilator,UnitType.Protoss_Forge,UnitType.Protoss_Citadel_of_Adun
				,UnitType.Protoss_Fleet_Beacon,UnitType.Protoss_Robotics_Support_Bay,UnitType.Protoss_Observatory
				,UnitType.Protoss_Arbiter_Tribunal};
		UnitType[] trainArmyB = new UnitType[]{UnitType.Protoss_Stargate,UnitType.Protoss_Gateway,UnitType.Protoss_Robotics_Facility};
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
