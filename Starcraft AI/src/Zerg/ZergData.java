package Zerg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bwapi.Unit;
import bwapi.UnitType;
import main.Data;

public class ZergData extends Data{
	public ZergData(){
		super();
		workerType = UnitType.Zerg_Drone;
		baseType = UnitType.Zerg_Hatchery;
		gasType = UnitType.Zerg_Extractor;
		UnitType[] armyT = new UnitType[]{UnitType.Zerg_Zergling,UnitType.Zerg_Hydralisk,UnitType.Zerg_Lurker,
				UnitType.Zerg_Guardian,UnitType.Zerg_Devourer,UnitType.Zerg_Mutalisk,UnitType.Zerg_Ultralisk,
				UnitType.Zerg_Queen,UnitType.Zerg_Defiler,UnitType.Zerg_Scourge};
		UnitType[] allUnit = new UnitType[]{UnitType.Zerg_Larva,UnitType.Zerg_Drone,UnitType.Zerg_Creep_Colony,
				UnitType.Zerg_Spawning_Pool,UnitType.Zerg_Evolution_Chamber,UnitType.Zerg_Hydralisk_Den
				,UnitType.Zerg_Spire,UnitType.Zerg_Greater_Spire,UnitType.Zerg_Queens_Nest,UnitType.Zerg_Extractor,UnitType.Zerg_Nydus_Canal
				,UnitType.Zerg_Ultralisk_Cavern,UnitType.Zerg_Defiler_Mound,UnitType.Zerg_Overlord};
		UnitType[] trainArmyB = new UnitType[]{UnitType.Zerg_Hive,UnitType.Zerg_Hatchery,UnitType.Zerg_Lair};
		UnitType[] morphPair = new UnitType[]{UnitType.Zerg_Lair,UnitType.Zerg_Hatchery,UnitType.Zerg_Hive,UnitType.Zerg_Lair,
				UnitType.Zerg_Greater_Spire,UnitType.Zerg_Spire,UnitType.Zerg_Guardian,UnitType.Zerg_Mutalisk,UnitType.Zerg_Devourer,
				UnitType.Zerg_Mutalisk,UnitType.Zerg_Lurker,UnitType.Zerg_Hydralisk};
		for(int i = 0;i < morphPair.length - 1;i += 2){
			morphFrom.put(morphPair[i],morphPair[i + 1]);
		}
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
