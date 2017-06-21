package debug;
import java.util.Map;

import bwapi.Color;
import bwapi.Game;
import bwapi.Player;
import bwapi.Unit;
import main.Data;
import main.Helper;

public class Debug {
	Game game;
	Player self;
	Data data;
	Helper helper;
	int[] enemyUnitCount;
	public Debug(Game game,Player self,Data tags,Helper helper){
		this.game = game;
		this.self = self;
		this.data = tags;
		this.helper = helper;
		enemyUnitCount = new int[game.enemies().size()];
	}
	public void DrawTextAndShape(){
//		game.drawTextScreen(10, 10, "fps:" + game.getFPS());
//		 game.drawTextScreen(10, 10, "worker:" + helper.workerSupply() + " army:" + helper.armySupply());
//	        for(Unit u : data.myUnits.get(data.workerType).values()){
//	        	if(u.isMorphing()) game.drawTextMap(u.getPosition(), "morphing");
//	        	else game.drawTextMap(u.getPosition(), "workers");
//	        }
//			// display all unit
//	        for(Map<Integer,Unit> map : data.myUnits.values()){
//	        	for(Unit u : map.values()){
//	        		game.drawTextMap(u.getPosition(), u.getType().toString());
//	        	}
//	        }
		 	for(Map<Integer,Unit> map : data.underConstructing.values()){
	        	for(Unit u : map.values()){
	        		game.drawTextMap(u.getPosition(), u.getType().toString());
	        	}
	        }
//	        if(data.scouter != null && data.myUnits.get(UnitType.Terran_SCV).containsKey(data.scouter.getID())){
//	    		game.drawTextMap(data.scouter.getPosition(), "scouter" );
//	        }
//	        for(Unit u : game.getNeutralUnits()){
//	        	if(u.getType().isMineralField()){
//	        		game.drawTextMap(u.getPosition(), "minerals");
//	        	}
//	        }
//	        for(int index : data.minerals.keySet()){
//	        	for(Unit mineral : data.minerals.get(index))
//	        		game.drawLineMap(BWTA.getBaseLocations().get(index).getPosition(), mineral.getPosition(),Color.Black);
//	        }
//		 	for(Unit u : self.getUnits()){
//		 		game.drawCircleMap(u.getPosition(), 30, Color.Blue);
//		 		game.drawTextMap(u.getPosition(), u.getType().toString());
//		 	}
//	        for(Unit structure : self.getUnits()){
//	        	if(structure.getType().isBuilding()){
//	        		Position leftTop = new Position(structure.getX() - structure.getType().dimensionLeft(),
//	        					structure.getY() - structure.getType().dimensionUp());
//	        		Position rightBot = new Position(structure.getX() + structure.getType().dimensionRight(),
//	    					structure.getY() + structure.getType().dimensionDown());
//	        		game.drawBoxMap(leftTop, rightBot, Color.Black);
//	        	}
//	        }
//	        for(Unit army : data.armyGroup.values()){
//	        	game.drawCircleMap(army.getPosition(),20, Color.Blue);
//	        }
//	        for(int i = 0;i < game.enemies().size();i++){
//	        	Player enemies = game.enemies().get(i);
//	        	for(Unit unit : enemies.getUnits()){
//	        		game.drawCircleMap(unit.getPosition(), 20, Color.Red);
//	        	}
//	        }
//	        
//	        for(int i = 0;i < data.baseLocations.size();i++){
//	        	BaseLocation bl = data.baseLocations.get(i);
//	        	game.drawCircleMap(bl.getPosition(), 30, Color.Orange);
//	        	game.drawTextMap(bl.getPosition(), (int)(i + 1)+"");
//			}
//	        
//	        System.out.println(data.myUnits.size());
	        
//	        Region regionT = data.regionsTrainBuilding;
//	        Region regionO = data.regionsOtherBuilding;
//	        helper.drawRegion(regionT, Color.Yellow);
//	        helper.drawRegion(regionO, Color.Orange);
	        for(int i = 0;i < data.enemyStructure.size();i++){
	        	game.drawTextMap(data.enemyStructure.get(i).toPosition(), data.enemyStructureName.get(i));
	        	game.drawCircleMap(data.enemyStructure.get(i).toPosition(), 30, Color.Red);
	        }
	        //draw all grid
//	        for(int i = 0;i < data.grids.length;i++){
//	        	for(int j = 0;j < data.grids[0].length;j++){
//	        		game.drawBoxMap(data.grids[i][j].leftTop, data.grids[i][j].rightBot, Color.Orange);
//	        		game.drawTextMap(data.grids[i][j].mid, "["+i+","+j+"]");
//	        	}
//	        }
	        game.drawBoxMap(data.trainGrid.leftTop, data.trainGrid.rightBot, Color.Orange);
	        game.drawBoxMap(data.researchGrid.leftTop, data.researchGrid.rightBot, Color.Yellow);
//	        for(Unit u : data.myUnits.get(UnitType.Protoss_Pylon).values()){
//	        	int width = 500,height = 320;
//	        	game.drawBoxMap(new Position(u.getX() - width/2,u.getY() - height/2), new Position(u.getX() + width/2,u.getY() + height/2), 
//	        			Color.Black);
//	        }
//	        for(Unit u : data.myUnits.get(UnitType.Zerg_Hatchery).values()){
//	        	int width = 600,height = 500;
//	        	game.drawBoxMap(new Position(u.getX() - width/2,u.getY() - height/2), new Position(u.getX() + width/2,u.getY() + height/2), 
//        			Color.Black);
//	        }
//			for(UnitType ut : data.underConstructing.keySet()){
//				for(Unit u : data.underConstructing.get(ut).values()){
//					if(u.isBeingConstructed()){
//						helper.drawBox(u, Color.Blue);
//					}
//				}
//			}
	}
}
