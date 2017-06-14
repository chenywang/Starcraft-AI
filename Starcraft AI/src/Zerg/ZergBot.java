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
        if(data.frame % data.hundredChecker == 0)
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
        if(data.frame % data.hundredChecker == 0)
        if(data.myUnits.get(UnitType.Zerg_Zergling).size() 
        		+ data.underConstructing.get(UnitType.Zerg_Zergling).size() < 
        		10 + helper.usedSupply()/10){
        	List<Unit> larvas = helper.getLarva(10);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Zergling);
        	}
        }
        //train Hydralisk
        if(data.frame % data.hundredChecker == 0)
        if(data.myUnits.get(UnitType.Zerg_Hydralisk).size() 
        		+ data.underConstructing.get(UnitType.Zerg_Hydralisk).size() < 
        			20 + helper.usedSupply()/3){
        	List<Unit> larvas = helper.getLarva(15);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Hydralisk);
        	}
        }
        //train mutalisk
        if(data.frame % data.hundredChecker == 0)
        if(data.myUnits.get(UnitType.Zerg_Mutalisk).size() +
        		data.underConstructing.get(UnitType.Zerg_Mutalisk).size()
        		< 10 + helper.usedSupply()/5){
        	List<Unit> larvas = helper.getLarva(10);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Mutalisk);
        	}
        }
        //train Ultralisk
        if(data.frame % data.hundredChecker == 0)
        if(data.myUnits.get(UnitType.Zerg_Ultralisk).size() 
        		+ data.underConstructing.get(UnitType.Zerg_Ultralisk).size() < 
        			5 + helper.usedSupply()/20){
        	List<Unit> larvas = helper.getLarva(5);
        	for(Unit larva : larvas){
        		larva.morph(UnitType.Zerg_Ultralisk);
        	}
        }
        //build spawning pool
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Spawning_Pool).size() + 
        		data.underConstructing.get(UnitType.Zerg_Spawning_Pool).size() == 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Zerg_Spawning_Pool,builder); 
        }
        }
        //build Hydralisk den
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Spawning_Pool).size() != 0 && 
        		data.myUnits.get(UnitType.Zerg_Hydralisk_Den).size()
        		  + data.underConstructing.get(UnitType.Zerg_Hydralisk_Den).size() == 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Zerg_Hydralisk_Den,builder); 
        }
        }
        //build hatchery
        if(data.frame % data.threeHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Hatchery).size() + 
        		data.underConstructing.get(UnitType.Zerg_Hatchery).size() < 2 + self.supplyTotal()/45){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Zerg_Hatchery,builder); 
        }
        }
        //build queen nest
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Queens_Nest).size() + 
        		data.underConstructing.get(UnitType.Zerg_Queens_Nest).size() == 0
        		&& data.myUnits.get(UnitType.Zerg_Lair).size() != 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Zerg_Queens_Nest,builder); 
        }
        }
        //build cavern
        if(data.frame % data.twoHundredChecker == 0){
        if(data.myUnits.get(UnitType.Zerg_Ultralisk_Cavern).size() + 
        		data.underConstructing.get(UnitType.Zerg_Ultralisk_Cavern).size() == 0
        		&& data.myUnits.get(UnitType.Zerg_Hive).size() != 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Zerg_Ultralisk_Cavern,builder); 
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
            	helper.build(UnitType.Zerg_Spire,builder); 
            }
        }
        //upgrade greater spire
//        System.out.println(data.myUnits.get(UnitType.Zerg_Spire).size()
//        		+" " +  data.underConstructing.get(UnitType.Zerg_Greater_Spire).size() + " "
//        		+ data.myUnits.get(UnitType.Zerg_Spire).size() + " " + 
//        				data.myUnits.get(UnitType.Zerg_Hive).size());
        if(data.frame % data.twoHundredChecker == 0){
            if(data.myUnits.get(UnitType.Zerg_Spire).size() != 0 &&
            		+ data.underConstructing.get(UnitType.Zerg_Greater_Spire).size()
            		+ data.myUnits.get(UnitType.Zerg_Greater_Spire).size() == 0 &&
            		data.myUnits.get(UnitType.Zerg_Hive).size() != 0){
            	System.out.println("build greater spire!!");
            	Unit spire = helper.grabOneUnit(data.myUnits.get(UnitType.Zerg_Spire));
            	if(spire != null){
            		System.out.println("build greater spire");
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
        	System.out.println("upgrade!!");
        	hive.upgrade(UpgradeType.Ventral_Sacs);
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
            for(Unit den : data.myUnits.get(UnitType.Zerg_Ultralisk).values()){
              	if(den.canUpgrade(UpgradeType.Anabolic_Synthesis)){
              		den.upgrade(UpgradeType.Anabolic_Synthesis);
            	}
            	if(den.canUpgrade(UpgradeType.Chitinous_Plating)){
            		den.upgrade(UpgradeType.Chitinous_Plating);
            	}
            }
    	}
//        //build core
//        if(data.frame % data.twoHundredChecker == 0)
//        if(data.myUnits.get(UnitType.Protoss_Gateway).size() != 0 &&
//        		data.myUnits.get(UnitType.Protoss_Cybernetics_Core).size() + 
//        			data.underConstructing.get(UnitType.Protoss_Cybernetics_Core).size() == 0){
//        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
//        	helper.build(UnitType.Protoss_Cybernetics_Core, builder);
//        }
//        //build adun
//        if(data.frame % data.twoHundredChecker == 0)
//        if(data.myUnits.get(UnitType.Protoss_Cybernetics_Core).size() != 0 &&
//        		data.myUnits.get(UnitType.Protoss_Citadel_of_Adun).size() + 
//        			data.underConstructing.get(UnitType.Protoss_Citadel_of_Adun).size() == 0){
//        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
//        	helper.build(UnitType.Protoss_Citadel_of_Adun, builder);
//        }
//        //build templar archive
//        if(data.frame % data.threeHundredChecker == 0)
//        if(data.myUnits.get(UnitType.Protoss_Citadel_of_Adun).size() != 0 &&
//        		data.myUnits.get(UnitType.Protoss_Templar_Archives).size() + 
//        			data.underConstructing.get(UnitType.Protoss_Templar_Archives).size() == 0){
//        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
//        	helper.build(UnitType.Protoss_Templar_Archives, builder);
//        }
//        //build facility
//        if(data.frame % data.twoHundredChecker == 0){
//            if(data.myUnits.get(UnitType.Protoss_Cybernetics_Core).size() != 0 &&
//            		data.myUnits.get(UnitType.Protoss_Robotics_Facility).size() + 
//            			data.underConstructing.get(UnitType.Protoss_Robotics_Facility).size() == 0){
//            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
//            	helper.build(UnitType.Protoss_Robotics_Facility, builder);
//            }
//        }
//        //build facility support
//        if(data.frame % data.threeHundredChecker == 0){
//            if(data.myUnits.get(UnitType.Protoss_Robotics_Facility).size() != 0 &&
//            		data.myUnits.get(UnitType.Protoss_Robotics_Support_Bay).size() + 
//            			data.underConstructing.get(UnitType.Protoss_Robotics_Support_Bay).size() == 0){
//            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
//            	helper.build(UnitType.Protoss_Robotics_Support_Bay, builder);
//            }
//        }
//        //build observatory
//        if(data.frame % data.threeHundredChecker == 0){
//            if(data.myUnits.get(UnitType.Protoss_Robotics_Facility).size() != 0 &&
//            		data.myUnits.get(UnitType.Protoss_Observatory).size() + 
//            			data.underConstructing.get(UnitType.Protoss_Observatory).size() == 0){
//            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
//            	helper.build(UnitType.Protoss_Observatory, builder);
//            }
//        }
//        //build stargate
//        if(data.frame % data.twoHundredChecker == 0){
//            if(data.myUnits.get(UnitType.Protoss_Cybernetics_Core).size() != 0 &&
//            		data.myUnits.get(UnitType.Protoss_Stargate).size() + 
//            			data.underConstructing.get(UnitType.Protoss_Stargate).size() < 1 + helper.usedSupply()/50){
//            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
//            	helper.build(UnitType.Protoss_Stargate, builder);
//            }
//        }
//        //build fleet beacon
//        if(data.frame % data.threeHundredChecker == 0){
//            if(data.myUnits.get(UnitType.Protoss_Stargate).size() != 0 &&
//            		data.myUnits.get(UnitType.Protoss_Fleet_Beacon).size() + 
//            			data.underConstructing.get(UnitType.Protoss_Fleet_Beacon).size() == 0){
//            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
//            	helper.build(UnitType.Protoss_Fleet_Beacon, builder);
//            }
//        }
//        //build arbiter tribunal
//        if(data.frame % data.threeHundredChecker == 0){
//            if(data.myUnits.get(UnitType.Protoss_Stargate).size() != 0 &&
//            		data.myUnits.get(UnitType.Protoss_Templar_Archives).size() != 0 && 
//            		data.myUnits.get(UnitType.Protoss_Arbiter_Tribunal).size() + 
//            			data.underConstructing.get(UnitType.Protoss_Arbiter_Tribunal).size() == 0){
//            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
//            	helper.build(UnitType.Protoss_Arbiter_Tribunal, builder);
//            }
//        }
//        //train zealot , dragoon,templar and dark templar;
//        for(Unit gateway : data.myUnits.get(UnitType.Protoss_Gateway).values()){
//        	if(!gateway.isTraining()){
//        		int zealot = data.myUnits.get(UnitType.Protoss_Zealot).size(),dragoon = data.myUnits.get(UnitType.Protoss_Dragoon).size()
//        				,darkTemplar = data.myUnits.get(UnitType.Protoss_Dark_Templar).size(),
//        					templar = data.myUnits.get(UnitType.Protoss_High_Templar).size();
//        		if(gateway.canTrain(UnitType.Protoss_Dark_Templar)
//        				&& darkTemplar < zealot / 5){
//        			gateway.train(UnitType.Protoss_Dark_Templar);
//        		}else if(gateway.canTrain(UnitType.Protoss_High_Templar)
//        				&& templar < zealot / 5){
//        			gateway.train(UnitType.Protoss_High_Templar);
//        		}else if(gateway.canTrain(UnitType.Protoss_Dragoon)
//        				&& dragoon < zealot / 3){
//        			gateway.train(UnitType.Protoss_Dragoon);
//        		}else{
//        			gateway.train(UnitType.Protoss_Zealot);
//        		}
//        	}
//        }       
//        //train scout ,carrier, arbitor,corsair;
//        for(Unit stargate : data.myUnits.get(UnitType.Protoss_Stargate).values()){
//        	if(!stargate.isTraining()){
//        		int zealot = data.myUnits.get(UnitType.Protoss_Zealot).size(),
//        				carrier = data.myUnits.get(UnitType.Protoss_Carrier).size()
//        				,arbiter = data.myUnits.get(UnitType.Protoss_Arbiter).size(),
//        				corsair =  data.myUnits.get(UnitType.Protoss_Corsair).size();
//        		if(stargate.canTrain(UnitType.Protoss_Arbiter)
//        				&& arbiter == 0){
//        			stargate.train(UnitType.Protoss_Arbiter);
//        		}else if(stargate.canTrain(UnitType.Protoss_Carrier)
//        				&& carrier < zealot / 7 + 1){
//        			stargate.train(UnitType.Protoss_Carrier);
//        		}else if(stargate.canTrain(UnitType.Protoss_Corsair) &&
//        				corsair < zealot /5 + 1){
//        			stargate.train(UnitType.Protoss_Corsair);
//        		}else{
//        			stargate.train(UnitType.Protoss_Scout);
//        		}
//        	}
//        }
//        //train reaver and observer
//        for(Unit facility : data.myUnits.get(UnitType.Protoss_Robotics_Facility).values()){
//        	if(!facility.isTraining()){
//        		int zealot = data.myUnits.get(UnitType.Protoss_Zealot).size(),
//        				reaver = data.myUnits.get(UnitType.Protoss_Reaver).size(),
//        					ob = data.myUnits.get(UnitType.Protoss_Observer).size();
//        		if(facility.canTrain(UnitType.Protoss_Observer)
//        				&& ob < helper.armySupply() / 10){
//        			facility.train(UnitType.Protoss_Observer);
//        		}else if(facility.canTrain(UnitType.Protoss_Reaver)
//        				&& reaver < zealot / 7 + 1){
//        			facility.train(UnitType.Protoss_Reaver);
//        		}
//        	}
//        }
//        //for reaver and carrier
//        for(Unit carrier : data.myUnits.get(UnitType.Protoss_Carrier).values()){
//        	if(!carrier.isTraining() && carrier.getInterceptorCount() < 8){
//        		carrier.train(UnitType.Protoss_Interceptor);
//        	}
//        }
//        for(Unit reaver : data.myUnits.get(UnitType.Protoss_Reaver).values()){
//        	if(!reaver.isTraining() && reaver.getScarabCount() < 8){
//        		reaver.train(UnitType.Protoss_Scarab);
//        	}
//        }
//        //upgrade dargoon
//        for(Unit core : data.myUnits.get(UnitType.Protoss_Cybernetics_Core).values()){
//        	if(core.canUpgrade(UpgradeType.Singularity_Charge))
//        		core.upgrade(UpgradeType.Singularity_Charge);
//        }
//        //upgrade reaver 
//        for(Unit support : data.myUnits.get(UnitType.Protoss_Robotics_Support_Bay).values()){
//        	if(support.canUpgrade(UpgradeType.Scarab_Damage))
//        		support.upgrade(UpgradeType.Scarab_Damage);
//           	if(support.canUpgrade(UpgradeType.Reaver_Capacity))
//        		support.upgrade(UpgradeType.Reaver_Capacity);
//          	if(support.canUpgrade(UpgradeType.Gravitic_Drive))
//        		support.upgrade(UpgradeType.Gravitic_Drive);
//        }
//        //upgrade observer
//        for(Unit observatory : data.myUnits.get(UnitType.Protoss_Observatory).values()){
//        	if(observatory.canUpgrade(UpgradeType.Gravitic_Boosters))
//        		observatory.upgrade(UpgradeType.Gravitic_Boosters);
//           	if(observatory.canUpgrade(UpgradeType.Sensor_Array))
//        		observatory.upgrade(UpgradeType.Sensor_Array);
//        }
//        //upgrade carrier
//        for(Unit beacon : data.myUnits.get(UnitType.Protoss_Fleet_Beacon).values()){
//        	if(beacon.canUpgrade(UpgradeType.Carrier_Capacity))
//        		beacon.upgrade(UpgradeType.Carrier_Capacity);
//           	if(beacon.canUpgrade(UpgradeType.Apial_Sensors))
//        		beacon.upgrade(UpgradeType.Apial_Sensors);
//        	if(beacon.canUpgrade(UpgradeType.Gravitic_Thrusters))
//        		beacon.upgrade(UpgradeType.Gravitic_Thrusters);
//        	if(beacon.canUpgrade(UpgradeType.Argus_Jewel))
//        		beacon.upgrade(UpgradeType.Argus_Jewel);
//        	if(beacon.research(TechType.Disruption_Web))
//        		beacon.research(TechType.Disruption_Web);
//        }
//        //upgrade zealot
//        for(Unit adun : data.myUnits.get(UnitType.Protoss_Citadel_of_Adun).values()){
//        	if(adun.canUpgrade(UpgradeType.Leg_Enhancements))
//        		adun.upgrade(UpgradeType.Leg_Enhancements);
//        }
//        //upgrade templar
//        for(Unit archive : data.myUnits.get(UnitType.Protoss_Templar_Archives).values()){
//        	if(archive.canUpgrade(UpgradeType.Khaydarin_Amulet))
//        		archive.upgrade(UpgradeType.Khaydarin_Amulet);
//        	if(archive.canUpgrade(UpgradeType.Argus_Talisman))
//        		archive.upgrade(UpgradeType.Argus_Talisman);
//        	if(archive.research(TechType.Hallucination))
//        		archive.research(TechType.Hallucination);
//        	if(archive.research(TechType.Psionic_Storm))
//        		archive.research(TechType.Psionic_Storm);
//        	if(archive.research(TechType.Mind_Control))
//        		archive.research(TechType.Mind_Control);
//        	if(archive.research(TechType.Maelstrom))
//        		archive.research(TechType.Maelstrom);
//        }
//        //upgrade arbitor
//        for(Unit tributernl : data.myUnits.get(UnitType.Protoss_Arbiter_Tribunal).values()){
//        	if(tributernl.canUpgrade(UpgradeType.Khaydarin_Core))
//        		tributernl.upgrade(UpgradeType.Khaydarin_Core);
//        	if(tributernl.research(TechType.Recall))
//        		tributernl.research(TechType.Recall);
//        	if(tributernl.research(TechType.Stasis_Field))
//        		tributernl.research(TechType.Stasis_Field);
//        }
//        //for archon ability
//        for(Unit unit : data.myUnits.get(UnitType.Protoss_High_Templar).values()){
//        	if(data.myUnits.get(UnitType.Protoss_Archon).size() < 3){
//        	}
//        }
    }


}