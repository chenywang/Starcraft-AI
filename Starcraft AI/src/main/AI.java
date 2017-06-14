package main;

import Protoss.ProtossBot;
import Terran.TerranBot;
import Zerg.ZergBot;
import bwapi.DefaultBWListener;
import bwapi.Mirror;
import bwapi.Race;
import bwapi.Unit;

public class AI extends DefaultBWListener{
	Mirror mirror = new Mirror();
	Race race;
	ProtossBot protoss;
	TerranBot terran;
	ZergBot zerg;
	@Override
	public void onStart() {
		race = mirror.getGame().self().getRace();
		System.out.println(race);
		if(race == Race.Protoss){
			protoss = new ProtossBot(mirror);
			protoss.onStart();
		}else if(race == Race.Terran){
			terran = new TerranBot(mirror);
			terran.onStart();
		}else if(race == Race.Zerg){
			zerg = new ZergBot(mirror);
			zerg.onStart();
		}
	}
	@Override
	public void onFrame() {
		if(race == Race.Protoss){
			protoss.onFrame();
		}else if(race == Race.Terran){
			terran.onFrame();
		}else if(race == Race.Zerg){
			zerg.onFrame();
		}
	}
	@Override
	public void onUnitComplete(Unit unit) {
		if(race == Race.Protoss){
			protoss.onUnitComplete(unit);
		}else if(race == Race.Terran){
			terran.onUnitComplete(unit);
		}else if(race == Race.Zerg){
			zerg.onUnitComplete(unit);
		}
	}
	@Override
	public void onUnitCreate(Unit unit) {
		if(race == Race.Protoss){
			protoss.onUnitCreate(unit);
		}else if(race == Race.Terran){
			terran.onUnitCreate(unit);
		}else if(race == Race.Zerg){
			zerg.onUnitCreate(unit);
		}
	}
	@Override
	public void onUnitDestroy(Unit unit) {
		if(race == Race.Protoss){
			protoss.onUnitDestroy(unit);
		}else if(race == Race.Terran){
			terran.onUnitDestroy(unit);
		}else if(race == Race.Zerg){
			zerg.onUnitDestroy(unit);
		}
	}
	@Override
	public void onUnitDiscover(Unit unit) {
		if(race == Race.Protoss){
			protoss.onUnitDiscover(unit);
		}else if(race == Race.Terran){
			terran.onUnitDiscover(unit);
		}else if(race == Race.Zerg){
			zerg.onUnitDiscover(unit);
		}
	}
	public void run(){
        mirror.getModule().setEventListener(this);
        mirror.startGame();
	}
}
