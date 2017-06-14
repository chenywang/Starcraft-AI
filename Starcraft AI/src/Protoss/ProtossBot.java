package Protoss;
import bwapi.Mirror;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import main.Bot;

public class ProtossBot extends Bot {
	public ProtossBot(Mirror mirror){
		super(mirror);
	}
	@Override
    public void onStart() {
		data = new ProtossData();
		super.onStart();
	}
    @Override
    public void onUnitCreate(Unit unit) {
    	super.onUnitCreate(unit);
    }
    @Override
  	public void onUnitComplete(Unit unit) {
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
        //build supply
        if(data.frame % data.threeHundredChecker == 0)
        if(self.supplyTotal()/2 != 200 &&
        	helper.supplyLeft() + data.underConstructing.get(UnitType.Protoss_Pylon).size() * 8 <= 50 + Math.min(2, self.supplyUsed() * data.checkSupplyCoefficent)){
//        if(data.underConstructing.get(UnitType.Protoss_Pylon).size() + data.myUnits.get(UnitType.Protoss_Pylon).size() == 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Protoss_Pylon, builder);
        }
        //build gateway
        if(data.frame % data.twoHundredChecker == 0)
        if(self.minerals() >= 150 && data.myUnits.get(UnitType.Protoss_Gateway).size() +  data.underConstructing.get(UnitType.Protoss_Gateway).size()
        			< self.supplyUsed()/35 + 1 && data.myUnits.get(UnitType.Protoss_Pylon).size() != 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Protoss_Gateway,builder);     	
        }
        //build core
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Protoss_Gateway).size() != 0 &&
        		data.myUnits.get(UnitType.Protoss_Cybernetics_Core).size() + 
        			data.underConstructing.get(UnitType.Protoss_Cybernetics_Core).size() == 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Protoss_Cybernetics_Core, builder);
        }
        //build adun
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Protoss_Cybernetics_Core).size() != 0 &&
        		data.myUnits.get(UnitType.Protoss_Citadel_of_Adun).size() + 
        			data.underConstructing.get(UnitType.Protoss_Citadel_of_Adun).size() == 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Protoss_Citadel_of_Adun, builder);
        }
        //build templar archive
        if(data.frame % data.threeHundredChecker == 0)
        if(data.myUnits.get(UnitType.Protoss_Citadel_of_Adun).size() != 0 &&
        		data.myUnits.get(UnitType.Protoss_Templar_Archives).size() + 
        			data.underConstructing.get(UnitType.Protoss_Templar_Archives).size() == 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Protoss_Templar_Archives, builder);
        }
        //build facility
        if(data.frame % data.twoHundredChecker == 0){
            if(data.myUnits.get(UnitType.Protoss_Cybernetics_Core).size() != 0 &&
            		data.myUnits.get(UnitType.Protoss_Robotics_Facility).size() + 
            			data.underConstructing.get(UnitType.Protoss_Robotics_Facility).size() == 0){
            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
            	helper.build(UnitType.Protoss_Robotics_Facility, builder);
            }
        }
        //build facility support
        if(data.frame % data.threeHundredChecker == 0){
            if(data.myUnits.get(UnitType.Protoss_Robotics_Facility).size() != 0 &&
            		data.myUnits.get(UnitType.Protoss_Robotics_Support_Bay).size() + 
            			data.underConstructing.get(UnitType.Protoss_Robotics_Support_Bay).size() == 0){
            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
            	helper.build(UnitType.Protoss_Robotics_Support_Bay, builder);
            }
        }
        //build observatory
        if(data.frame % data.threeHundredChecker == 0){
            if(data.myUnits.get(UnitType.Protoss_Robotics_Facility).size() != 0 &&
            		data.myUnits.get(UnitType.Protoss_Observatory).size() + 
            			data.underConstructing.get(UnitType.Protoss_Observatory).size() == 0){
            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
            	helper.build(UnitType.Protoss_Observatory, builder);
            }
        }
        //build stargate
        if(data.frame % data.twoHundredChecker == 0){
            if(data.myUnits.get(UnitType.Protoss_Cybernetics_Core).size() != 0 &&
            		data.myUnits.get(UnitType.Protoss_Stargate).size() + 
            			data.underConstructing.get(UnitType.Protoss_Stargate).size() < 1 + helper.usedSupply()/50){
            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
            	helper.build(UnitType.Protoss_Stargate, builder);
            }
        }
        //build fleet beacon
        if(data.frame % data.threeHundredChecker == 0){
            if(data.myUnits.get(UnitType.Protoss_Stargate).size() != 0 &&
            		data.myUnits.get(UnitType.Protoss_Fleet_Beacon).size() + 
            			data.underConstructing.get(UnitType.Protoss_Fleet_Beacon).size() == 0){
            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
            	helper.build(UnitType.Protoss_Fleet_Beacon, builder);
            }
        }
        //build arbiter tribunal
        if(data.frame % data.threeHundredChecker == 0){
            if(data.myUnits.get(UnitType.Protoss_Stargate).size() != 0 &&
            		data.myUnits.get(UnitType.Protoss_Templar_Archives).size() != 0 && 
            		data.myUnits.get(UnitType.Protoss_Arbiter_Tribunal).size() + 
            			data.underConstructing.get(UnitType.Protoss_Arbiter_Tribunal).size() == 0){
            	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
            	helper.build(UnitType.Protoss_Arbiter_Tribunal, builder);
            }
        }
        //train worker
        for(Unit commandCenter : data.myUnits.get(data.baseType).values()){
        	if(!commandCenter.isTraining() && helper.workerSupply() < data.stopTrainWorker){
        		commandCenter.train(data.workerType);
        	}
        }
        //train zealot , dragoon,templar and dark templar;
        for(Unit gateway : data.myUnits.get(UnitType.Protoss_Gateway).values()){
        	if(!gateway.isTraining()){
        		int zealot = data.myUnits.get(UnitType.Protoss_Zealot).size(),dragoon = data.myUnits.get(UnitType.Protoss_Dragoon).size()
        				,darkTemplar = data.myUnits.get(UnitType.Protoss_Dark_Templar).size(),
        					templar = data.myUnits.get(UnitType.Protoss_High_Templar).size();
        		if(gateway.canTrain(UnitType.Protoss_Dark_Templar)
        				&& darkTemplar < zealot / 5){
        			gateway.train(UnitType.Protoss_Dark_Templar);
        		}else if(gateway.canTrain(UnitType.Protoss_High_Templar)
        				&& templar < zealot / 5){
        			gateway.train(UnitType.Protoss_High_Templar);
        		}else if(gateway.canTrain(UnitType.Protoss_Dragoon)
        				&& dragoon < zealot / 3){
        			gateway.train(UnitType.Protoss_Dragoon);
        		}else{
        			gateway.train(UnitType.Protoss_Zealot);
        		}
        	}
        }       
        //train scout ,carrier, arbitor,corsair;
        for(Unit stargate : data.myUnits.get(UnitType.Protoss_Stargate).values()){
        	if(!stargate.isTraining()){
        		int zealot = data.myUnits.get(UnitType.Protoss_Zealot).size(),
        				carrier = data.myUnits.get(UnitType.Protoss_Carrier).size()
        				,arbiter = data.myUnits.get(UnitType.Protoss_Arbiter).size(),
        				corsair =  data.myUnits.get(UnitType.Protoss_Corsair).size();
        		if(stargate.canTrain(UnitType.Protoss_Arbiter)
        				&& arbiter == 0){
        			stargate.train(UnitType.Protoss_Arbiter);
        		}else if(stargate.canTrain(UnitType.Protoss_Carrier)
        				&& carrier < zealot / 7 + 1){
        			stargate.train(UnitType.Protoss_Carrier);
        		}else if(stargate.canTrain(UnitType.Protoss_Corsair) &&
        				corsair < zealot /5 + 1){
        			stargate.train(UnitType.Protoss_Corsair);
        		}else{
        			stargate.train(UnitType.Protoss_Scout);
        		}
        	}
        }
        //train reaver and observer
        for(Unit facility : data.myUnits.get(UnitType.Protoss_Robotics_Facility).values()){
        	if(!facility.isTraining()){
        		int zealot = data.myUnits.get(UnitType.Protoss_Zealot).size(),
        				reaver = data.myUnits.get(UnitType.Protoss_Reaver).size(),
        					ob = data.myUnits.get(UnitType.Protoss_Observer).size();
        		if(facility.canTrain(UnitType.Protoss_Observer)
        				&& ob < helper.armySupply() / 10){
        			facility.train(UnitType.Protoss_Observer);
        		}else if(facility.canTrain(UnitType.Protoss_Reaver)
        				&& reaver < zealot / 7 + 1){
        			facility.train(UnitType.Protoss_Reaver);
        		}
        	}
        }
        //for reaver and carrier
        for(Unit carrier : data.myUnits.get(UnitType.Protoss_Carrier).values()){
        	if(!carrier.isTraining() && carrier.getInterceptorCount() < 8){
        		carrier.train(UnitType.Protoss_Interceptor);
        	}
        }
        for(Unit reaver : data.myUnits.get(UnitType.Protoss_Reaver).values()){
        	if(!reaver.isTraining() && reaver.getScarabCount() < 8){
        		reaver.train(UnitType.Protoss_Scarab);
        	}
        }
        //upgrade dargoon
        for(Unit core : data.myUnits.get(UnitType.Protoss_Cybernetics_Core).values()){
        	if(core.canUpgrade(UpgradeType.Singularity_Charge))
        		core.upgrade(UpgradeType.Singularity_Charge);
        }
        //upgrade reaver 
        for(Unit support : data.myUnits.get(UnitType.Protoss_Robotics_Support_Bay).values()){
        	if(support.canUpgrade(UpgradeType.Scarab_Damage))
        		support.upgrade(UpgradeType.Scarab_Damage);
           	if(support.canUpgrade(UpgradeType.Reaver_Capacity))
        		support.upgrade(UpgradeType.Reaver_Capacity);
          	if(support.canUpgrade(UpgradeType.Gravitic_Drive))
        		support.upgrade(UpgradeType.Gravitic_Drive);
        }
        //upgrade observer
        for(Unit observatory : data.myUnits.get(UnitType.Protoss_Observatory).values()){
        	if(observatory.canUpgrade(UpgradeType.Gravitic_Boosters))
        		observatory.upgrade(UpgradeType.Gravitic_Boosters);
           	if(observatory.canUpgrade(UpgradeType.Sensor_Array))
        		observatory.upgrade(UpgradeType.Sensor_Array);
        }
        //upgrade carrier
        for(Unit beacon : data.myUnits.get(UnitType.Protoss_Fleet_Beacon).values()){
        	if(beacon.canUpgrade(UpgradeType.Carrier_Capacity))
        		beacon.upgrade(UpgradeType.Carrier_Capacity);
           	if(beacon.canUpgrade(UpgradeType.Apial_Sensors))
        		beacon.upgrade(UpgradeType.Apial_Sensors);
        	if(beacon.canUpgrade(UpgradeType.Gravitic_Thrusters))
        		beacon.upgrade(UpgradeType.Gravitic_Thrusters);
        	if(beacon.canUpgrade(UpgradeType.Argus_Jewel))
        		beacon.upgrade(UpgradeType.Argus_Jewel);
        	if(beacon.research(TechType.Disruption_Web))
        		beacon.research(TechType.Disruption_Web);
        }
        //upgrade zealot
        for(Unit adun : data.myUnits.get(UnitType.Protoss_Citadel_of_Adun).values()){
        	if(adun.canUpgrade(UpgradeType.Leg_Enhancements))
        		adun.upgrade(UpgradeType.Leg_Enhancements);
        }
        //upgrade templar
        for(Unit archive : data.myUnits.get(UnitType.Protoss_Templar_Archives).values()){
        	if(archive.canUpgrade(UpgradeType.Khaydarin_Amulet))
        		archive.upgrade(UpgradeType.Khaydarin_Amulet);
        	if(archive.canUpgrade(UpgradeType.Argus_Talisman))
        		archive.upgrade(UpgradeType.Argus_Talisman);
        	if(archive.research(TechType.Hallucination))
        		archive.research(TechType.Hallucination);
        	if(archive.research(TechType.Psionic_Storm))
        		archive.research(TechType.Psionic_Storm);
        	if(archive.research(TechType.Mind_Control))
        		archive.research(TechType.Mind_Control);
        	if(archive.research(TechType.Maelstrom))
        		archive.research(TechType.Maelstrom);
        }
        //upgrade arbitor
        for(Unit tributernl : data.myUnits.get(UnitType.Protoss_Arbiter_Tribunal).values()){
        	if(tributernl.canUpgrade(UpgradeType.Khaydarin_Core))
        		tributernl.upgrade(UpgradeType.Khaydarin_Core);
        	if(tributernl.research(TechType.Recall))
        		tributernl.research(TechType.Recall);
        	if(tributernl.research(TechType.Stasis_Field))
        		tributernl.research(TechType.Stasis_Field);
        }
        //for archon ability
        for(Unit unit : data.myUnits.get(UnitType.Protoss_High_Templar).values()){
        	if(data.myUnits.get(UnitType.Protoss_Archon).size() < 3){
        	}
        }
    }



}