package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import bwapi.Position;
import bwapi.Region;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;

public class Data {
	public List<TilePosition> enemyBase = new ArrayList<>(),enemyStructure = new ArrayList<>();
	public List<String> enemyStructureName = new ArrayList<>();
	public double checkSupplyCoefficent = 0.10;
	public Position forceRallyPoint;
	public int attackArmy = 60;
	public int stopTrainWorker = 50;
	public int scountAtSupply = 4;
	public boolean sentScount = false;
	public double stimHealthPercent = 1.0;
	public UnitType workerType,baseType,gasType; 
	public int frame = 0;
	public Unit scouter = null;
	public int twoHundredChecker = 200,tenChecker = 10,threeHundredChecker = 300,hundredChecker = 100;
	public Map<Integer,Unit> armyGroup = new HashMap<>(),
			workersGatherMineral = new HashMap<>(),
			workersGatherGas = new HashMap<>();
	public Map<Integer,List<Unit>> minerals = new HashMap<>();
	public Map<UnitType,UnitType> morphFrom = new HashMap<>();
	public Map<UnitType,HashMap<Integer,Unit>> underConstructing = new HashMap<>();
	public Map<UnitType,HashMap<Integer,Unit>> myUnits = new HashMap<>();
	public Map<Integer,Position> positionStuckChecker = new HashMap<>();
	public Set<UnitType> armyType = new HashSet<>();
	public Set<UnitType> trainArmyBuilding = new HashSet<>();
	public Set<UnitType> invisibleType = new HashSet<>();
	public int retreatDistance = 100;
	public double retreatHealthPercent = 0.5;
	public int retreatLeastHealth = 30;
	public Set<Integer> visitedRegion = new HashSet<>(),visitedPylon = new HashSet<>(),visitedHatchery;
	public Region regionsTrainBuilding,regionsOtherBuilding;
	public Queue<Region> backUpRegion = new LinkedList<>();
	public List<BaseLocation> baseLocations = new ArrayList<>();
	//special agent is for do some shift operation,wait the agent have done what I command and then do the next
	public Map<Integer,Unit> specialAgent = new HashMap<>();
	public Map<Integer,Position> specialAgentBuildPosition = new HashMap<>();
	public Map<Integer,UnitType> specialAgentBuildType = new HashMap<>();
	public Set<Integer> opponents = new HashSet<>();
	public Data(){
		UnitType[] invisibleTarget = new UnitType[]{UnitType.Zerg_Lurker,UnitType.Terran_Ghost,UnitType.Protoss_Dark_Templar};
		//initialize invisible set
		for(UnitType u : invisibleTarget){
			invisibleType.add(u);
		}
	}
//    public Set<Integer> onDuty = new HashSet<>();
//    public boolean isBuildingSupply = false;
//    public boolean isBuildingBarrak = false;
}
