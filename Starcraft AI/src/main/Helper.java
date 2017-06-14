package main;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bwapi.Color;
import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.Region;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;

public class Helper {
	Game game;
	Player self;
	Data data;
	public Helper(Game game,Player self,Data tags){
		this.game = game;
		this.self = self;
		this.data = tags;
	}
	public boolean build(UnitType buildType,Unit builder,Position pos){
		if(buildType.mineralPrice() > self.minerals() || buildType.gasPrice() > self.gas()
				|| builder == null) return false;
		builder.move(pos);
		data.specialAgent.put(builder.getID(), builder);
		data.specialAgentBuildType.put(builder.getID(),buildType);
		data.specialAgentBuildPosition.put(builder.getID(), pos);
		return true;
	}
	public boolean build(UnitType buildType,Unit builder){
		if(buildType.mineralPrice() > self.minerals() || buildType.gasPrice() > self.gas()
				|| builder == null) return false;
		TilePosition buildTile = getBuildTile(builder,buildType);
		if(buildTile == null) return false;
		builder.move(buildTile.toPosition());
		data.specialAgent.put(builder.getID(), builder);
		data.specialAgentBuildType.put(builder.getID(),buildType);
		data.specialAgentBuildPosition.put(builder.getID(), buildTile.toPosition());
		return true;
	}
	public TilePosition getBuildTile(Unit builder,UnitType buildingType) {
		if(buildingType.isRefinery()){
			Unit gas = null;
			for (Unit n : game.neutral().getUnits()) {
				if ((n.getType() == UnitType.Resource_Vespene_Geyser)){
					if(gas == null || gas.getDistance(self.getStartLocation().toPosition()) 
								> n.getDistance(self.getStartLocation().toPosition()))
						gas = n;
				}
			}
			return gas == null ? null : gas.getTilePosition();
		}
		if(data.baseType == UnitType.Protoss_Nexus){
			return getBuildTileProtoss(builder,buildingType);
		}else if(data.baseType == UnitType.Terran_Command_Center){
			return getBuildTileTerran(builder,buildingType);
		}else if(data.baseType == UnitType.Zerg_Hatchery){
			return getBuildTileZerg(builder,buildingType);
		}else{
			return getBuildTileZerg(builder,buildingType);
		}
	}
	public TilePosition getBuildTileZerg(Unit builder,UnitType buildingType) {
		if(buildingType == UnitType.Zerg_Hatchery) return findNormalTileForBuild(builder,buildingType);
		List<Unit> base = new ArrayList<>();
		base.addAll(data.myUnits.get(UnitType.Zerg_Hatchery).values());
		base.addAll(data.myUnits.get(UnitType.Zerg_Lair).values());
		base.addAll(data.myUnits.get(UnitType.Zerg_Hive).values());
		for(Unit hatchery : base){
			if(data.visitedPylon.contains(hatchery.getID())) continue;
			int width = 600,height = 500,searchDistance = 20;
			for(int x = hatchery.getPosition().getX() - width/2;x <  hatchery.getPosition().getX() + width/2;x += searchDistance){
				for(int y = hatchery.getPosition().getY() - height/2;y < hatchery.getPosition().getY() + height/2;y += searchDistance){
					Position pos = new Position(x, y);
					if(game.canBuildHere(pos.toTilePosition(),buildingType,builder,false)
							&& !blockResource(pos,buildingType)){
						return pos.toTilePosition();
					}
				}
			}
			data.visitedHatchery.add(hatchery.getID());
		}
		return null;
	}
	public TilePosition getBuildTileProtoss(Unit builder,UnitType buildingType) {
		if(buildingType == UnitType.Protoss_Pylon) return findNormalTileForBuild(builder,buildingType);
		for(Unit pylon : data.myUnits.get(UnitType.Protoss_Pylon).values()){
			if(data.visitedPylon.contains(pylon.getID())) continue;
			int width = 500,height = 320,searchDistance = 50;
			for(int x = pylon.getPosition().getX() - width/2;x <  pylon.getPosition().getX() + width/2;x += searchDistance){
				for(int y = pylon.getPosition().getY() - height/2;y < pylon.getPosition().getY() + height/2;y += searchDistance){
					Position pos = new Position(x, y);
					if(game.canBuildHere(pos.toTilePosition(),buildingType,builder,false)
							&& !blockResource(pos,buildingType)){
						return pos.toTilePosition();
					}
				}
			}
			data.visitedPylon.add(pylon.getID());
		}
		return null;
		
	}
	public TilePosition getBuildTileTerran(Unit builder,UnitType buildingType) {
		return findNormalTileForBuild(builder,buildingType);
	}
	public TilePosition findNormalTileForBuild(Unit builder,UnitType buildingType){
		Region region = data.trainArmyBuilding.contains(buildingType) ? data.regionsTrainBuilding : data.regionsOtherBuilding;
		while(true){
			int x = region.getBoundsLeft(),y = region.getBoundsTop();
			int height = 40,
				width = 30;
			while(region.getBoundsBottom() > y){
				while(region.getBoundsRight() > x){
					Position pos = new Position(x, y);
					if(game.canBuildHere(pos.toTilePosition(),buildingType,builder,false)
							&& !blockResource(pos,buildingType)){
						return pos.toTilePosition();
					}
					x += width;
				}
				x = region.getBoundsLeft();
				y += height;
			}
			for(Region neighbor : region.getNeighbors()){
				if(data.visitedRegion.add(neighbor.getID())){
					data.backUpRegion.offer(neighbor);
				}
			}
			if(data.backUpRegion.size() == 0){
				System.out.println("ur queue is empty nows");
				System.out.println("we add region " + game.getAllRegions().size());
				for(Region re : game.getAllRegions())
					data.backUpRegion.offer(re);
			}
			region = data.backUpRegion.poll();
			if(data.trainArmyBuilding.contains(buildingType))
				data.regionsTrainBuilding = region;
			else
				data.regionsOtherBuilding = region;
		}
	}
	public boolean blockResource(Position p,UnitType buildingType){
		List<BaseLocation> list = BWTA.getBaseLocations();
		for(int index : data.minerals.keySet()){
			for(Unit mineral : data.minerals.get(index)){
				Line2D line = new Line2D.Float(list.get(index).getPosition().getX(),list.get(index).getPosition().getY(),
						mineral.getPosition().getX(),mineral.getPosition().getY());
				Rectangle rec = new Rectangle(p.getX() - buildingType.dimensionLeft(),p.getY() - buildingType.dimensionUp(),
						 buildingType.dimensionLeft() +  buildingType.dimensionRight(),buildingType.dimensionUp() + 
						 buildingType.dimensionDown());
				if(line.intersects(rec)){
					return true;
				}
			}
		}
		return false;
	}
	public void findEnemy(Unit scout,boolean isFirst){
		scout.stop();
		for(BaseLocation bl : BWTA.getBaseLocations()){
			if(!bl.getTilePosition().toString().equals(self.getStartLocation().toString())){
				if(isFirst && !bl.isStartLocation()) continue;
				scout.move(bl.getPosition(),true);
			}
		}
//		scout.move(self.getStartLocation().toPosition(), true);
//		gatherClosestMineral(scout);
	}
	public boolean isBase(Unit u){
		return u.getType() == UnitType.Terran_Command_Center ||
				u.getType() == UnitType.Protoss_Nexus ||
				u.getType() == UnitType.Zerg_Hive ||
				u.getType() == UnitType.Zerg_Hatchery ||
				u.getType() == UnitType.Zerg_Lair;
	}
	public int supplyLeft(){
		return (self.supplyTotal() - self.supplyUsed())/2;
	}
	public boolean gatherClosestMineral(Unit myUnit){
		 Unit closestMineral = null;              
         //find the closest mineral
         for (Unit neutralUnit : game.neutral().getUnits()) {
             if (neutralUnit.getType().isMineralField()) {
                 if (closestMineral == null || myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
                     closestMineral = neutralUnit;
                 }
             }
         }
         //if a mineral patch was found, send the worker to gather it
         if (closestMineral != null) {
             myUnit.gather(closestMineral);
             data.workersGatherMineral.put(myUnit.getID(), myUnit);
             return true;
         }
         return false;
	}
	public boolean gatherGas(Unit myUnit,Unit gasStation){
        if (gasStation != null) {
            myUnit.gather(gasStation);
            return true;
        }
        return false;
	}
	public List<Unit> grabUnits(Map<Integer,Unit> map,int num){
		List<Unit> result = new ArrayList<>();
		for(int id : map.keySet()){
    		if(num == 0) break;
    		num--;
    		result.add(map.get(id));
    	}
		//if it's worker, we need to remove it so that we wont command scv in constructing
		if(result.size() != 0 && result.get(0).getType().isWorker()){
			for(Unit u : result){
				map.remove(u.getID());
			}
		}
		return result;
	}
	public Unit grabOneUnit(Map<Integer,Unit> map){
		if(map.size() == 0){
			return null;
		}
		else return grabUnits(map,1).get(0);
	}
	public int armySupply(){
		return usedSupply() - workerSupply();
	}
	public int workerSupply(){
		return data.myUnits.get(data.workerType).size();
	}
	public int usedSupply(){
		return self.supplyUsed()/2;
	}
	public TilePosition findNextBase(Unit builder){
		for(int i = 0;i < data.baseLocations.size();i++){
			BaseLocation bl = data.baseLocations.get(i);
			if(game.canBuildHere(bl.getTilePosition(),data.baseType, builder, false))
				return bl.getTilePosition();
		}
		return null;
	}
	public boolean enemyAround(Unit unit){
		for(Player p : game.enemies()){
			for(Unit u : p.getUnits()){
				if(unit.isInWeaponRange(u)) return true;
			}
		}
		return false;
	}
	public Unit getClosestEnemyUnit(Unit myUnit){
		Unit enemy = null;
        int distance = Integer.MAX_VALUE; 
        for(Player oponent : game.enemies()){
        	for(Unit e : oponent.getUnits()){
        		if(!e.getType().canAttack() || !e.isAttackFrame())
        			continue;
        		int currentDistance = myUnit.getDistance(e);
        		if(currentDistance < distance){
        			distance = currentDistance;
        			enemy = e;
        		}		
        	}
        }
        return enemy;
	}
	public Position retreatPosition(Unit myUnit,Unit enemy){
		int x1 = myUnit.getPosition().getX(),y1 = myUnit.getPosition().getY(),
				x2 = enemy.getPosition().getX(),y2 = enemy.getPosition().getY();
		double len = Math.sqrt((double)((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
		double[] direction = new double[]{(x1 - x2)/len,(y1 - y2)/len};
		Position result = new Position(x1 + (int)(data.retreatDistance * direction[0]), y1 + (int)(data.retreatDistance * direction[1]));
		return result;	
	}
	public void drawRegion(Region region,Color color){
		 Position leftTop = new Position(region.getBoundsLeft(),region.getBoundsTop());
	     Position rightBot = new Position(region.getBoundsRight(),region.getBoundsBottom());
	     game.drawBoxMap(leftTop, rightBot, color);
	}
	public void adjustAddonStructure(Unit structure,UnitType addon){
		Unit builder = grabOneUnit(data.workersGatherMineral);
		if(builder == null) return;
		if(structure.canLift())
			structure.lift();
		TilePosition pos = getBuildTile(builder,structure.getType());
		if(structure.canLand(pos))
			structure.land(pos);
		if(structure.canBuildAddon(addon))
			structure.buildAddon(addon);
	}
	public void drawBox(Unit structure,Color color){
		Position leftTop = new Position(structure.getX() - structure.getType().dimensionLeft(),
		structure.getY() - structure.getType().dimensionUp());
		Position rightBot = new Position(structure.getX() + structure.getType().dimensionRight(),
				structure.getY() + structure.getType().dimensionDown());
		game.drawBoxMap(leftTop, rightBot, color);
	}
	public boolean hasEnemy(List<Unit> list){
		for(Unit u : list){
			if(data.opponents.contains(u.getPlayer().getID())) return true;
		}
		return false;
	}
	public List<Unit> getLarva(int num){
		List<Unit> larvas = new ArrayList<>();
		for(Unit hatchery : data.myUnits.get(UnitType.Zerg_Hatchery).values()){
			for(Unit larva : hatchery.getLarva()){
				larvas.add(larva);
				if(larvas.size() >= num) return larvas;
			}
		}
		for(Unit hatchery : data.myUnits.get(UnitType.Zerg_Lair).values()){
			for(Unit larva : hatchery.getLarva()){
				larvas.add(larva);
				if(larvas.size() >= num) return larvas;
			}
		}
		for(Unit hatchery : data.myUnits.get(UnitType.Zerg_Hive).values()){
			for(Unit larva : hatchery.getLarva()){
				larvas.add(larva);
				if(larvas.size() >= num) return larvas;
			}
		}
		return larvas;
	}
}
