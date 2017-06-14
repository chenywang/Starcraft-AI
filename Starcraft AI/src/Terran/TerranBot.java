package Terran;
import bwapi.Color;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.Position;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import main.Bot;

public class TerranBot extends Bot {
	public TerranBot(Mirror mirror){
		super(mirror);
	}
	@Override
    public void onStart() {
		data = new TerranData();
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
        //resume construction
        if(data.frame % data.twoHundredChecker == 0)
        	for(UnitType ut : data.underConstructing.keySet()){
				for(Unit u : data.underConstructing.get(ut).values()){
					if(!u.isBeingConstructed()){
						Unit builder = helper.grabOneUnit(data.workersGatherMineral);
						if(builder != null)
							builder.rightClick(u);
					}
				}
			}
        //repair
        if(data.frame % data.twoHundredChecker == 0)
        	for(Unit construction : self.getUnits()){
        		if(construction.getType().isBuilding() && construction.getHitPoints() < 0.3 * construction.getType().maxHitPoints()){
        			Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        			if(builder != null)
        				builder.repair(construction);
        		}
        	}
        //train worker
        for(Unit commandCenter : data.myUnits.get(data.baseType).values()){
        	if(!commandCenter.isTraining() && helper.workerSupply() < data.stopTrainWorker){
        		commandCenter.train(data.workerType);
        	}
        }
        //train marine and medic
        for(Unit barrack : data.myUnits.get(UnitType.Terran_Barracks).values()){
        	if(!barrack.isTraining()){
        		if(data.myUnits.get(UnitType.Terran_Medic).size() < data.myUnits.get(UnitType.Terran_Marine).size() * 0.1 && 
        				self.gas() >= UnitType.Terran_Medic.gasPrice() &&
        				data.myUnits.get(UnitType.Terran_Academy).size() != 0){
        			barrack.train(UnitType.Terran_Medic);
        		}else{
        			barrack.train(UnitType.Terran_Marine);
        		}
        	}
        }
        //train tank vulture and goliath
        for(Unit factory : data.myUnits.get(UnitType.Terran_Factory).values()){
        	if(!factory.isTraining()){
        		if(factory.canTrain(UnitType.Terran_Siege_Tank_Tank_Mode) &&
        				data.myUnits.get(UnitType.Terran_Siege_Tank_Tank_Mode).size() < 
        				data.myUnits.get(UnitType.Terran_Vulture).size() * 2){
        			factory.train(UnitType.Terran_Siege_Tank_Tank_Mode);
        		}else if(factory.canTrain(UnitType.Terran_Goliath) &&
        				data.myUnits.get(UnitType.Terran_Goliath).size()
        				< data.myUnits.get(UnitType.Terran_Vulture).size()){
        			factory.train(UnitType.Terran_Goliath);
        		}else{
        			factory.train(UnitType.Terran_Vulture);
        		}
        	}
        }
        //train battlecruiser
        for(Unit starport : data.myUnits.get(UnitType.Terran_Starport).values()){
        	if(!starport.isTraining()){
        		if(starport.canTrain(UnitType.Terran_Battlecruiser)){
        			starport.train(UnitType.Terran_Battlecruiser);
        		}
        	}
        }
        //build supply
        if(data.frame % data.twoHundredChecker == 0)
        if(helper.usedSupply() != 200 &&
        	helper.supplyLeft() + data.underConstructing.get(UnitType.Terran_Supply_Depot).size() * 8 <= 100 + Math.min(2, self.supplyUsed() * data.checkSupplyCoefficent)){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Terran_Supply_Depot, builder);
        }
        //build barrack
        if(data.frame % data.twoHundredChecker == 0)
        if(self.minerals() >= 150 && data.myUnits.get(UnitType.Terran_Barracks).size() +  data.underConstructing.get(UnitType.Terran_Barracks).size()
        			< self.supplyUsed()/35 + 1){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Terran_Barracks,builder);     	
        }
        //build Academy
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Terran_Barracks).size() > 0 && 
        		data.myUnits.get(UnitType.Terran_Academy).size() + 
        			data.underConstructing.get(UnitType.Terran_Academy).size() == 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Terran_Academy,builder);
        }
        //upgrade stim_pack and U-238
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Terran_Academy).size() != 0){
        	Unit academy = helper.grabOneUnit(data.myUnits.get(UnitType.Terran_Academy));
        	if(academy != null){
        		academy.upgrade(UpgradeType.U_238_Shells);
        		academy.research(TechType.Stim_Packs);
        	}
        }
        //build radar
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Terran_Comsat_Station).size() < data.myUnits.get(UnitType.Terran_Command_Center).size()){
        	for(Unit commandCenter : data.myUnits.get(UnitType.Terran_Command_Center).values()){
        		commandCenter.buildAddon(UnitType.Terran_Comsat_Station);
        	}
        }
        //build factory
        if(data.frame % data.twoHundredChecker == 0)
        if( data.myUnits.get(UnitType.Terran_Barracks).size() > 0 &&
        		helper.usedSupply()/40 + 2> data.myUnits.get(UnitType.Terran_Factory).size() + 
        		data.underConstructing.get(UnitType.Terran_Factory).size()){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Terran_Factory, builder);
        }
        //build machine shop
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Terran_Machine_Shop).size()
        		+ data.underConstructing.get(UnitType.Terran_Machine_Shop).size()
        		 < data.myUnits.get(UnitType.Terran_Factory).size()){
        	for(Unit factory : data.myUnits.get(UnitType.Terran_Factory).values()){
        		if(factory.getAddon() == null && factory.canBuildAddon(UnitType.Terran_Machine_Shop)){
        			if(factory.isTraining()) factory.cancelTrain();
        			factory.buildAddon(UnitType.Terran_Machine_Shop);
        		}
        	}
        }
        //build armory
        if(data.frame % data.twoHundredChecker == 0)
        if( data.myUnits.get(UnitType.Terran_Factory).size() > 0 &&
            		data.myUnits.get(UnitType.Terran_Armory).size() + 
            		data.underConstructing.get(UnitType.Terran_Armory).size() == 0){
            Unit builder = helper.grabOneUnit(data.workersGatherMineral);
            helper.build(UnitType.Terran_Armory, builder);
        }
        //upgrade siege mode
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Terran_Machine_Shop).size() != 0){
        	for(Unit shop : data.myUnits.get(UnitType.Terran_Machine_Shop).values()){
        		if(shop.canResearch(TechType.Tank_Siege_Mode))
        			shop.research(TechType.Tank_Siege_Mode);
        	}
        }
        //build starport
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Terran_Factory).size() > 0 && 
        		helper.usedSupply()/20 + 1 > data.myUnits.get(UnitType.Terran_Starport).size()
        		+ data.underConstructing.get(UnitType.Terran_Starport).size()){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Terran_Starport, builder);
        }
        //build control tower
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Terran_Control_Tower).size() +
        		data.underConstructing.get(UnitType.Terran_Control_Tower).size() < data.myUnits.get(UnitType.Terran_Starport).size()){
        	for(Unit starPort : data.myUnits.get(UnitType.Terran_Starport).values()){
        		if(starPort.getAddon() == null && starPort.canBuildAddon(UnitType.Terran_Control_Tower)){
        			if(starPort.isTraining()) starPort.cancelTrain();
        			starPort.buildAddon(UnitType.Terran_Control_Tower);
        		}
        	}
        }
        //build science facility
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Terran_Starport).size() > 0 &&
        		data.myUnits.get(UnitType.Terran_Science_Facility).size() +
        		 data.underConstructing.get(UnitType.Terran_Science_Facility).size() == 0){
        	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
        	helper.build(UnitType.Terran_Science_Facility, builder);		 
        }
        //build lab
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Terran_Science_Facility).size() > 0){
        	Unit facility = helper.grabOneUnit(data.myUnits.get(UnitType.Terran_Science_Facility));
        	if(facility.getAddon() == null){
        		if(facility.canBuildAddon(UnitType.Terran_Physics_Lab)){
        			facility.buildAddon(UnitType.Terran_Physics_Lab);
        		}else{
        			helper.adjustAddonStructure(facility, UnitType.Terran_Physics_Lab);
        		}
        	}
        }
        //upgrade yamto
        if(data.frame % data.twoHundredChecker == 0)
        if(data.myUnits.get(UnitType.Terran_Physics_Lab).size() > 0){
        	Unit lab = helper.grabOneUnit(data.myUnits.get(UnitType.Terran_Physics_Lab));
        	if(lab.canResearch(TechType.Yamato_Gun))
        		lab.research(TechType.Yamato_Gun);
        	if(lab.canUpgrade(UpgradeType.Colossus_Reactor)){
        		lab.upgrade(UpgradeType.Colossus_Reactor);
        	}
        }
        //isUnder attack or is attacking
        for(Unit u : self.getUnits()){
        	if(u.isUnderAttack()){
        		if(u.getType().canMove() && u.getHitPoints() < 
        				Math.min(data.retreatLeastHealth,u.getType().maxHitPoints() * data.retreatHealthPercent)){
        			Unit enemy = helper.getClosestEnemyUnit(u);
        			Position retreatPosition = helper.retreatPosition(u,enemy);
        			game.drawCircleMap(u.getPosition(), 40, Color.Green);
        			game.drawCircleMap(retreatPosition, 40, Color.Green);
        			game.drawLineMap(retreatPosition, u.getPosition(), Color.Green);
        			u.move(retreatPosition);
        		}
        		if(data.frame % data.tenChecker == 0)
        		for(Player oponent : game.enemies()){
        	        for(Unit e : oponent.getUnits()){
        	        	if(data.invisibleType.contains(e.getType())){
                			Unit radar = helper.grabOneUnit(data.myUnits.get(UnitType.Terran_Comsat_Station));
                			if(radar != null)
                				radar.useTech(TechType.Scanner_Sweep,e.getPosition());
                		}
        	        }
        		}
        	}
        	if(u.isAttackFrame()){
        		if(u.getType() == UnitType.Terran_Marine){
        			if(u.getHitPoints() >= u.getType().maxHitPoints() * data.stimHealthPercent
        					&& u.canUseTech(TechType.Stim_Packs))
        				u.useTech(TechType.Stim_Packs);
        		}else if(u.getType() == UnitType.Terran_Siege_Tank_Tank_Mode){
        			u.useTech(TechType.Tank_Siege_Mode);
        		}else if(u.getType() == UnitType.Terran_Battlecruiser){
        			if(u.canUseTech(TechType.Yamato_Gun))
        				u.useTech(TechType.Yamato_Gun,helper.getClosestEnemyUnit(u));
        		}
        	}else{
        		if(u.getType() == UnitType.Terran_Siege_Tank_Siege_Mode){
        			if(!helper.enemyAround(u))
        				u.useTech(TechType.Tank_Siege_Mode);
        		}
        	}
        }
    }
}