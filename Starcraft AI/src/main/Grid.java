package main;
import bwapi.Position;

public class Grid {
	public Position leftTop,rightBot,mid;
	public double height,width;
	public int id;
	public Grid(Position leftTop,Position rightBot,int id){
		this.leftTop = leftTop;
		this.rightBot = rightBot;
		height = rightBot.getY() - leftTop.getY();
		width = rightBot.getX() - leftTop.getX();
		this.mid = new Position((leftTop.getX() + rightBot.getX())/2, (leftTop.getY() + rightBot.getY())/2);
		this.id = id;
	}
}
