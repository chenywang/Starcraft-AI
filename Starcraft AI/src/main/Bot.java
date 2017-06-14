package main;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import bwapi.Color;
import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.Position;
import bwapi.Region;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
import debug.Debug;

public class Bot extends DefaultBWListener{

    protected Game game;
    protected Helper helper;
    protected Player self;
    protected Data data;
    protected Debug debug;
    protected Mirror mirror;
    public Bot(Mirror mirror){
    	this.mirror = mirror;
    }
	@Override
	public void onStart() {
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");  
        game = mirror.getGame();
        self = game.self();
        helper = new Helper(game,self,data);
        debug = new Debug(game, self, data, helper);
        game.setLocalSpeed(0);
        game.enableFlag(1);
        game.sendText("show me the money");
        game.sendText("show me the money");
        game.sendText("show me the money");
        game.sendText("show me the money");
        game.sendText("the gathering");
//        data.forceRallyPoint = BWTA.getNearestChokepoint(self.getStartLocation()).getPoint();
        data.forceRallyPoint = BWTA.getNearestBaseLocation(self.getStartLocation().toPosition()).getPosition();
        data.regionsTrainBuilding = game.getRegionAt(self.getStartLocation().toPosition());
        data.regionsOtherBuilding = game.getRegionAt(self.getStartLocation().toPosition());
        data.visitedRegion.add(data.regionsTrainBuilding.getID());
        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
        	if(!baseLocation.getTilePosition().toString().equals(self.getStartLocation().toString()))
        		data.baseLocations.add(baseLocation);
        }
        for(Chokepoint cp : BWTA.getChokepoints()){
        	data.visitedRegion.add(game.getRegionAt(cp.getCenter()).getID());
        }
        Collections.sort(data.baseLocations,new Comparator<BaseLocation>(){
        	public int compare(BaseLocation b1,BaseLocation b2){
        		return self.getStartLocation().toPosition().getApproxDistance(b1.getPosition())
    					- self.getStartLocation().toPosition().getApproxDistance(b2.getPosition());
        	}
        });
        for(Player player : game.enemies()){
        	data.opponents.add(player.getID());
        }
	}
	@Override
	public void onUnitCreate(Unit unit) {
    	if(unit.getPlayer().getID() != self.getID()) return;
    	data.underConstructing.get(unit.getType()).put(unit.getID(), unit);
	}
	@Override
	public void onUnitComplete(Unit unit) {
    	if(unit.getPlayer().getID() != self.getID()) return;
//    	System.out.println(unit.getType());
    	HashMap<Integer,Unit> map = data.myUnits.get(unit.getType());
    	if(!map.containsKey(unit.getID()))
    		map.put(unit.getID(), unit);
    	data.underConstructing.get(unit.getType()).remove(unit.getID());
    	if(unit.getType().isWorker()){
        	helper.gatherClosestMineral(unit);
        }else if(data.trainArmyBuilding.contains(unit.getType())) {
        	unit.setRallyPoint(data.forceRallyPoint);
        }else if(data.armyType.contains(unit.getType())){
        	data.armyGroup.put(unit.getID(), unit);
        }
	}
	@Override
	public void onUnitDiscover(Unit unit) {
		if(data.opponents.contains(unit.getPlayer().getID())){
    		if(unit.getType().isBuilding()){
    			for(int i = 0;i < data.enemyStructure.size();i++){
    				if(data.enemyStructure.get(i).equals(unit.getTilePosition())) return;
    			}
    			if(helper.isBase(unit)){
    				data.enemyBase.add(unit.getTilePosition());
    			}
    			data.enemyStructureName.add(unit.getType().toString());
    			data.enemyStructure.add(unit.getTilePosition());
    		}
    	}
    	else if(unit.getType().isMineralField() || unit.getType() == UnitType.Resource_Vespene_Geyser){
    		List<BaseLocation> list = BWTA.getBaseLocations();
    		int len = Integer.MAX_VALUE,index = -1;
    		for(int i = 0;i < list.size();i++){
    			if(unit.getDistance(list.get(i).getPosition()) < len){
    				index = i;
    				len = unit.getDistance(list.get(i).getPosition());
    			}
    		}
    		if(data.minerals.get(index) == null) data.minerals.put(index,new ArrayList<Unit>());
    		data.minerals.get(index).add(unit);
    	}
	}
	@Override
	public void onUnitDestroy(Unit unit) {
		if(data.opponents.contains(unit.getPlayer().getID())){
			for(int i = 0;i < data.enemyBase.size();i++){
				if(unit.getTilePosition().equals(data.enemyBase.get(i))){
					data.enemyBase.remove(i);
					break;
				}
			}
			for(int i = 0;i < data.enemyStructure.size();i++){
				if(data.enemyStructure.get(i).equals(unit.getTilePosition())){
					data.enemyStructure.remove(i);
					data.enemyStructureName.remove(i);
					break;
				}
			}
//			System.out.println(data.enemyBase.size() + " " + data.enemyStructure.size());
		}else if(unit.getPlayer().getID() == self.getID()){
			if(data.myUnits.get(unit.getType()).containsKey(unit.getID()))
				data.myUnits.get(unit.getType()).remove(unit.getID());
			else
				data.underConstructing.get(unit.getType()).remove(unit.getID());
				data.positionStuckChecker.remove(unit.getID());
			if(unit.getType().isWorker()){
				if(data.workersGatherMineral.containsKey(unit.getID()))
					data.workersGatherMineral.remove(unit.getID());
			}
			else if(data.armyType.contains(unit.getType())){
				if(data.armyGroup.containsKey(unit.getID()))
					data.armyGroup.remove(unit.getID());
	    		}
		}
	}
	@Override
	public void onFrame() {
    	debug.DrawTextAndShape();
        //find enemy
        if(data.frame % data.tenChecker == 0)
        if(helper.usedSupply() >= data.scountAtSupply && !data.sentScount){
        	data.sentScount = true;
        	if(data.scouter == null){
        		data.scouter = helper.grabOneUnit(data.workersGatherMineral);
        	}
        	helper.findEnemy(data.scouter,true);     
        }
    	//special agent
    	for(int key : data.specialAgent.keySet()){
    		if(data.specialAgent.get(key).isIdle()){
    			data.specialAgent.get(key).build(data.specialAgentBuildType.get(key), data.specialAgentBuildPosition.get(key).toTilePosition());
    			data.specialAgent.remove(key);
    			data.specialAgentBuildType.remove(key);
    			data.specialAgentBuildPosition.remove(key);
    		}
    	}
        //idle work
        if(data.frame % data.tenChecker == 0)
        for(Unit workers : self.getUnits()){
        	if(workers.getType().isWorker()){
        		if( workers.isIdle() && workers.isCompleted() && !data.specialAgent.containsKey(workers.getID())){
        			if(data.scouter == null || data.scouter.getID() != workers.getID())
        				helper.gatherClosestMineral(workers);
        		}
        		//deal with the situation that we grab the worker, but it didnt go to build and keep gathering.
        		else if(workers.isGatheringMinerals() && !data.workersGatherMineral.containsKey(workers.getID())){
        			data.workersGatherMineral.put(workers.getID(), workers);
        		}
        		
        	}
        }
        //gather the gas
        if(data.frame % data.tenChecker == 0)
        if(data.workersGatherGas.size() < data.myUnits.get(data.gasType).size() * 2){
        	for(Unit gasStation : data.myUnits.get(data.gasType).values()){
        		if(gasStation.isBeingGathered()) continue;
        		List<Unit> collectors = helper.grabUnits(data.workersGatherMineral, 2);
        		for(Unit collector : collectors){
        			collector.gather(gasStation);
        			data.workersGatherGas.put(collector.getID(),collector);
        		}
        	}
        }
        //defend
        for(Unit structure : self.getUnits()){
        	if(structure.getType().isBuilding() && structure.isUnderAttack()){
        		//army far away come back and defend later
        		for(Unit army : data.armyGroup.values()){
        			army.attack(structure.getPosition(),true);
        		}
        		//army near defend directly
        		for(Unit u : structure.getRegion().getUnits())
        			if(u.getPlayer().getID() == self.getID() && data.armyType.contains(u.getType()))
        				u.attack(structure.getPosition());
        		for(Region region : structure.getRegion().getNeighbors())
        			for(Unit u : region.getUnits())
        				if(u.getPlayer().getID() == self.getID() && data.armyType.contains(u.getType()))
        					u.attack(structure.getPosition());
        		}
        }
      //conquer
      if(data.frame % data.twoHundredChecker == 0){
      	if(helper.armySupply() > data.attackArmy){
      		for(Unit a : data.armyGroup.values()){
      			if(a.isIdle()){
      				if(!data.enemyBase.isEmpty()){
      					if(a.canAttack())
      						a.attack(data.enemyBase.get(0).toPosition());
      					else
      						a.move(data.enemyBase.get(0).toPosition());
      				}
      				else if(!data.enemyStructure.isEmpty()){
      					if(a.canAttack())
      						a.attack(data.enemyStructure.get(0).toPosition());
      					else
      						a.move(data.enemyStructure.get(0).toPosition());
      				}
      				else{
      		        	data.scouter = helper.grabOneUnit(data.workersGatherMineral);
      					helper.findEnemy(data.scouter,false);
      				}
      			}
      		}
      	}
      }
      //build next base
      if(data.frame % data.twoHundredChecker == 0)
      if(data.myUnits.get(data.baseType).size() 
      		+ data.underConstructing.get(data.baseType).size() < helper.armySupply()/20 + 1){
      	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
      	if(builder != null){
      		TilePosition pos = helper.findNextBase(builder);
      		if(pos == null || !game.canBuildHere(pos, data.baseType, builder, false))
      			System.out.println("you can't build command center here");
      		else{
      			helper.build(data.baseType,builder,pos.toPosition());
      		}
      	}
      }
//    build gas station
      if(data.frame % data.threeHundredChecker == 0)
      if(data.myUnits.get(data.gasType).size() + data.underConstructing.get(data.gasType).size() == 0){
      	Unit builder = helper.grabOneUnit(data.workersGatherMineral);
      	helper.build(data.gasType,builder);
      }
      //check stuck
      if(data.frame % data.twoHundredChecker == 0){
      	 for(Unit u : self.getUnits()){
      		if(u.isIdle() || u.getType().isBuilding() || !u.getType().canMove()) continue;
           		Position last = data.positionStuckChecker.getOrDefault(u.getID(),new Position(0, 0));
           	if(last.equals(u.getPosition())){
           		u.stop();
           	}
           		data.positionStuckChecker.put(u.getID(), u.getPosition());
           }
      }
      //clean enemy structure trash location
//      if(data.frame % data.threeHundredChecker == 0){
//    	  for(int i = 0;i < data.enemyStructure.size();i++){
//    		  TilePosition tp = data.enemyStructure.get(i);
//    		  if(game.isVisible(tp) && helper.hasEnemy(game.getUnitsInRadius(tp.toPosition(), 100))){
//    			  System.out.println("remove!");
//    			  data.enemyStructure.remove(i);
//    			  data.enemyStructureName.remove(i);
//    			  break;
//    		  }
//    	  }
//      }
      data.frame++;
	}
}
