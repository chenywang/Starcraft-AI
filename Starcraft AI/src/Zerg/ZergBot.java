package Zerg;
import java.util.List;

import bwapi.Mirror;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import main.Bot;

public class ZergBot extends Bot {
	public ZergBot(Mirror mirror){
		super(mirror);
	}
	@Override
    public void onStart() {
		data = new ZergData();
		super.onStart();
	}
    @Override
    public void onUnitCreate(Unit unit) {
    	super.onUnitCreate(unit);
    }
    @Override
  	public void onUnitComplete(Unit unit) {
    	if(unit.getPlayer().getID() != self.getID()) return;
    	if(data.morphFrom.containsKey(unit.getType())){
    		data.underConstructing.get(unit.getType()).remove(unit.getID());
    	}else if(unit.getType().isBuilding()){
    		data.underConstructing.get(unit.getType()).remove(unit.getID());
    	}else{
    		data.underConstructing.get(unit.getType()).remove(unit.getID());
    	}
    	super.onUnitComplete(unit);
  	}
    @Override
	public void onUnitDiscover(Unit unit) {
    	super.onUnitDiscover(unit);
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		super.onUnitDestroy(unit);
	}


    @Override
    public void onFrame() {
    	super.onFrame();
    	//morph, sorry, we cant count underconstructing minions can't its only called egg, and never say what kind of egg
    	for(Unit unit : self.getUnits()){
    		if(unit.isMorphing()){
    			if(data.morphFrom.containsKey(unit.getType())){
    				UnitType from = data.morphFrom.get(unit.getType());
    				data.myUnits.get(from).remove(unit.getID());
    			}else if(unit.getType().isBuilding()){
    				data.myUnits.get(UnitType.Zerg_Drone).remove(unit.getID());
    			}else{
    				data.myUnits.get(UnitType.Zerg_Larva).remove(unit.getID());
    			}
    			data.underConstructing.get(unit.getType()).put(unit.getID(),unit);
    		}
    	}
        //train overlord
        if(data.frame % data.twoHundredChecker == 0)
        if(self.supplyTotal()/2 != 200 &&
        	helper.supplyLeft() + data.underConstructing.get(UnitType.Zerg_Overlord).size() * 8 <= 10 + Math.min(2, self.supplyUsed() * data.checkSupplyCoefficent)){
        	List<Unit> larvas = helper.getLarva(2);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Overlord);
        	}
        }
        //train drone
        if(data.frame % data.hundredChecker == 1)
        if(data.myUnits.get(UnitType.Zerg_Drone).size() 
        		+ data.underConstructing.get(UnitType.Zerg_Zergling).size() < data.stopTrainWorker
        		&& helper.workerSupply() + data.underConstructing.get(UnitType.Zerg_Zergling).size()
        			< 13 + helper.armySupply()/3){
        	List<Unit> larvas = helper.getLarva(2);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Drone);
        	}
        }
        //train zergling
        if(data.frame % data.hundredChecker == 2)
        if(data.myUnits.get(UnitType.Zerg_Zergling).size() 
        		+ data.underConstructing.get(UnitType.Zerg_Zergling).size() < 
        		20 + helper.usedSupply()/10){
        	List<Unit> larvas = helper.getLarva(10);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Zergling);
        	}
        }
        //train Hydralisk
        if(data.frame % data.hundredChecker == 3)
        if(data.myUnits.get(UnitType.Zerg_Hydralisk).size() 
        		+ data.underConstructing.get(UnitType.Zerg_Hydralisk).size() < 
        			30 + helper.usedSupply()/3){
        	List<Unit> larvas = helper.getLarva(15);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Hydralisk);
        	}
        }
        //train mutalisk
        if(data.frame % data.hundredChecker == 4)
        if(data.myUnits.get(UnitType.Zerg_Mutalisk).size() +
        		data.underConstructing.get(UnitType.Zerg_Mutalisk).size()
        		< 10 + helper.usedSupply()/5){
        	List<Unit> larvas = helper.getLarva(10);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Mutalisk);
        	}
        }
        //train Ultralisk
        if(data.frame % data.hundredChecker == 5)
        if(data.myUnits.get(UnitType.Zerg_Ultralisk).size() 
        		+ data.underConstructing.get(UnitType.Zerg_Ultralisk).size() < 
        			5 + helper.usedSupply()/20){
        	List<Unit> larvas = helper.getLarva(5);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Ultralisk);
        	}
        }
        //train defiler
        if(data.frame % data.hundredChecker == 6)
        if(data.myUnits.get(UnitType.Zerg_Defiler).size() 
        		+ data.underConstructing.get(UnitType.Zerg_Defiler).size() < 
        			1 + helper.usedSupply()/100){
        	List<Unit> larvas = helper.getLarva(1);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Defiler);
        	}
        }
        //train gudian and devourer
        if(data.frame % data.hundredChecker == 0){
            if(data.myUnits.get(UnitType.Zerg_Mutalisk).size() != 0){
            	boolean tag = true;
            	for(Unit u : data.myUnits.get(UnitType.Zerg_Mutalisk).values()){
            		if(tag)
            			u.morph(UnitType.Zerg_Guardian);
            		else
            			u.morph(UnitType.Zerg_Devourer);
            		tag = !tag;
            	}
            }
        }
        //build spawning pool
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Spawning_Pool).size() + 
        		data.underConstructing.get(UnitType.Zerg_Spawning_Pool).size() == 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	if(builder != null)
        		helper.build(UnitType.Zerg_Spawning_Pool,builder); 
        }
        }
        //build Hydralisk den
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Spawning_Pool).size() != 0 && 
        		data.myUnits.get(UnitType.Zerg_Hydralisk_Den).size()
        		  + data.underConstructing.get(UnitType.Zerg_Hydralisk_Den).size() == 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	if(builder != null)
        		helper.build(UnitType.Zerg_Hydralisk_Den,builder); 
        }
        }
        //build hatchery
        if(data.frame % data.threeHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Hatchery).size() + 
        		data.underConstructing.get(UnitType.Zerg_Hatchery).size() < 2 + self.supplyTotal()/35){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	if(builder != null)
        		helper.build(UnitType.Zerg_Hatchery,builder); 
        }
        }
        //build queen nest
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Queens_Nest).size() + 
        		data.underConstructing.get(UnitType.Zerg_Queens_Nest).size() == 0
        		&& data.myUnits.get(UnitType.Zerg_Lair).size() != 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	if(builder != null)
        		helper.build(UnitType.Zerg_Queens_Nest,builder); 
        }
        }
        //build cavern
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Ultralisk_Cavern).size() + 
        		data.underConstructing.get(UnitType.Zerg_Ultralisk_Cavern).size() == 0
        		&& data.myUnits.get(UnitType.Zerg_Hive).size() != 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	if(builder != null)
        		helper.build(UnitType.Zerg_Ultralisk_Cavern,builder); 
        }
        }
        //build mound
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Defiler_Mound).size() + 
        		data.underConstructing.get(UnitType.Zerg_Defiler_Mound).size() == 0
        		&& data.myUnits.get(UnitType.Zerg_Hive).size() != 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	if(builder != null)
        		helper.build(UnitType.Zerg_Defiler_Mound,builder); 
        }
        }
        //build spire
        if(data.frame % data.twoHundredChecker == 0){
            if(data.myUnits.get(UnitType.Zerg_Spire).size() + 
            		data.underConstructing.get(UnitType.Zerg_Spire).size() 
            		+ data.myUnits.get(UnitType.Zerg_Greater_Spire).size()
            		+ data.underConstructing.get(UnitType.Zerg_Greater_Spire).size()== 0
            		&& data.myUnits.get(UnitType.Zerg_Lair).size() 
            		+ data.myUnits.get(UnitType.Zerg_Hive).size()
            			!= 0){
            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
            	if(builder != null)
            		helper.build(UnitType.Zerg_Spire,builder); 
            }
        }
        if(data.frame % data.twoHundredChecker == 0){
            if(data.myUnits.get(UnitType.Zerg_Spire).size() != 0 &&
            		+ data.underConstructing.get(UnitType.Zerg_Greater_Spire).size()
            		+ data.myUnits.get(UnitType.Zerg_Greater_Spire).size() == 0 &&
            		data.myUnits.get(UnitType.Zerg_Hive).size() != 0){
            	Unit spire = helper.grabOneUnit(data.myUnits.get(UnitType.Zerg_Spire));
            	if(spire != null){
            		spire.morph(UnitType.Zerg_Greater_Spire);
            	}
            }
        }
        //upgrade lair
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Spawning_Pool).size() != 0 && 
        		data.myUnits.get(UnitType.Zerg_Lair).size() 
        		+ data.underConstructing.get(UnitType.Zerg_Lair).size() 
        		+ data.myUnits.get(UnitType.Zerg_Hive).size()
        		+ data.underConstructing.get(UnitType.Zerg_Hive).size() == 0){
        	Unit hatchery = helper.grabOneUnit(data.myUnits.get(UnitType.Zerg_Hatchery));
        	if(hatchery != null){
        		hatchery.morph(UnitType.Zerg_Lair);
        	}
        }
        }
        //upgrade hive
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Queens_Nest).size() != 0 && 
        		data.myUnits.get(UnitType.Zerg_Lair).size() != 0 &&
        		+ data.myUnits.get(UnitType.Zerg_Hive).size()
        		+ data.underConstructing.get(UnitType.Zerg_Hive).size() == 0){
        	Unit lair = helper.grabOneUnit(data.myUnits.get(UnitType.Zerg_Lair));
        	if(lair != null){
        		lair.morph(UnitType.Zerg_Hive);
        	}
        }
        }
        //upgrade overlord
        if(data.frame % data.threeHundredChecker == 0)
        for(Unit lair : data.myUnits.get(UnitType.Zerg_Lair).values()){
        	if(lair.canUpgrade(UpgradeType.Pneumatized_Carapace)){
        		lair.upgrade(UpgradeType.Pneumatized_Carapace);
        	}
        }
        if(data.frame % data.threeHundredChecker == 0)
        for(Unit hive : data.myUnits.get(UnitType.Zerg_Hive).values()){
          	if(hive.canUpgrade(UpgradeType.Ventral_Sacs)){
          		System.out.println("upgrade1");
          		hive.upgrade(UpgradeType.Ventral_Sacs);
        	}
        	if(hive.canUpgrade(UpgradeType.Antennae)){
        		System.out.println("upgrade2");
        		hive.upgrade(UpgradeType.Antennae);
        	}
        }
        //upgrad hydralisk
        if(data.frame % data.threeHundredChecker == 0){
            for(Unit den : data.myUnits.get(UnitType.Zerg_Hydralisk_Den).values()){
              	if(den.canUpgrade(UpgradeType.Muscular_Augments)){
              		den.upgrade(UpgradeType.Muscular_Augments);
            	}
            	if(den.canUpgrade(UpgradeType.Grooved_Spines)){
            		den.upgrade(UpgradeType.Grooved_Spines);
            	}
            	if(den.canResearch(TechType.Lurker_Aspect)){
            		den.research(TechType.Lurker_Aspect);
            	}
            }
    	}
        //upgrad ultralisk
        if(data.frame % data.twoHundredChecker == 0){
            for(Unit cavern : data.myUnits.get(UnitType.Zerg_Ultralisk_Cavern).values()){
              	if(cavern.canUpgrade(UpgradeType.Anabolic_Synthesis)){
              		cavern.upgrade(UpgradeType.Anabolic_Synthesis);
            	}
            	if(cavern.canUpgrade(UpgradeType.Chitinous_Plating)){
            		cavern.upgrade(UpgradeType.Chitinous_Plating);
            	}
            }
    	}
        //overlord lead
        if(data.frame % data.twoHundredChecker == 0){
        	for(int i = data.leadingOverlord.size() - 1;i >= 0;i--){
        		if(!data.myUnits.get(UnitType.Zerg_Overlord).containsKey(data.leadingOverlord.get(i).getID())){
        			data.leadingOverlord.remove(i);
        		}
        	}
        	while(data.leadingOverlord.size() < helper.usedSupply()/60){
        		Unit overlord = helper.grabOneUnit(data.myUnits.get(UnitType.Zerg_Overlord));
        		if(overlord != null){
        			data.armyGroup.put(overlord.getID(), overlord);
        			data.leadingOverlord.add(overlord);
        		}
        		else
        			break;
        	}
        }
    }


}