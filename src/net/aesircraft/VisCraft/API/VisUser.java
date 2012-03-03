package net.aesircraft.VisCraft.API;

import net.aesircraft.VisCraft.Player.User;
import org.bukkit.entity.Player;

public class VisUser {
    
    /*
     * NEVER USE User The reason for this layer is to seperate the API from my
     * Classes If something in there changes your plugin will not break while
     * using this API This will remain the way it is and properly deprecated
     * when needed.
     *
     * YOU HAVE BEEN WARNED!
     */
    
    private User user;
    private boolean loaded=false;
    
    public VisUser(Player player){
        User u=new User();
        user.load(player);
        user=u;
        loaded=true;        
    }
    
    public Player getPlayer(){
        return user.getPlayer();
    }
    
    public boolean isLoaded(){
        return loaded;
    }
    
    public int getVis(){
        return user.getVis();
    }
    
    public int getAirVis(){
        return user.getAirVis();
    }
    
    public int getEarthVis(){
        return user.getEarthVis();
    }
    
    public int getFireVis(){
        return user.getFireVis();
    }
    
    public int getWaterVis(){
        return user.getWaterVis();
    }
    
    public int getDarkVis(){
        return user.getDarkVis();
    }
    
    public int getPureVis(){
        return user.getPureVis();
    }
    
    public int getBrilliantVis(){
        return user.getBrilliantVis();
    }
    
    public int getEtherealVis(){
        return user.getEtherealVis();
    }
    
    public int getUnstableVis(){
        return user.getUnstableVis();
    }
    public void setVis(int amount){
        user.setVis(amount);
    }
    
    public void setAirVis(int amount){
        user.setAirVis(amount);
    }
    
    public void setEarthVis(int amount){
        user.setEarthVis(amount);
    }
    
    public void setFireVis(int amount){
        user.setFireVis(amount);
    }
    
    public void setWaterVis(int amount){
        user.setWaterVis(amount);
    }
    
    public void setDarkVis(int amount){
        user.setDarkVis(amount);
    }
    
    public void setPureVis(int amount){
        user.setPureVis(amount);
    }
    
    public void setBrilliantVis(int amount){
        user.setBrilliantVis(amount);
    }
    
    public void setEtherealVis(int amount){
        user.setEtherealVis(amount);
    }
    
    public void setUnstableVis(int amount){
        user.setUnstableVis(amount);
    }
    
    public void addVis(int amount){
        user.addVis(amount);
    }
    
    public void addAirVis(int amount){
        user.addAirVis(amount);
    }
    
    public void addEarthVis(int amount){
        user.addEarthVis(amount);
    }
    
    public void addFireVis(int amount){
        user.addFireVis(amount);
    }
    
    public void addWaterVis(int amount){
        user.addWaterVis(amount);
    }
    
    public void addDarkVis(int amount){
        user.addDarkVis(amount);
    }
    
    public void addPureVis(int amount){
        user.addPureVis(amount);
    }
    
    public void addBrilliantVis(int amount){
        user.addBrilliantVis(amount);
    }
    
    public void addEtherealVis(int amount){
        user.addEtherealVis(amount);
    }
    
    public void addUnstableVis(int amount){
        user.addUnstableVis(amount);
    }
    
    public void subtractVis(int amount){
        user.subtractVis(amount);
    }
    
    public void subtractAirVis(int amount){
        user.subtractAirVis(amount);
    }
    
    public void subtractEarthVis(int amount){
        user.subtractEarthVis(amount);
    }
    
    public void subtractFireVis(int amount){
        user.subtractFireVis(amount);
    }
    
    public void subtractWaterVis(int amount){
        user.subtractWaterVis(amount);
    }
    
    public void subtractDarkVis(int amount){
        user.subtractDarkVis(amount);
    }
    
    public void subtractPureVis(int amount){
        user.subtractPureVis(amount);
    }
    
    public void subtractBrilliantVis(int amount){
        user.subtractBrilliantVis(amount);
    }
    
    public void subtractEtherealVis(int amount){
        user.subtractEtherealVis(amount);
    }
    
    public void subtractUnstableVis(int amount){
        user.subtractUnstableVis(amount);
    }
    
    public boolean hasVis(int amount){
        return user.hasVis(amount);
    }
    
    public boolean hasAirVis(int amount){
        return user.hasAirVis(amount);
    }
    
    public boolean hasEarthVis(int amount){
        return user.hasEarthVis(amount);
    }
    
    public boolean hasFireVis(int amount){
        return user.hasFireVis(amount);
    }
    
    public boolean hasWaterVis(int amount){
        return user.hasWaterVis(amount);
    }
    
    public boolean hasDarkVis(int amount){
        return user.hasDarkVis(amount);
    }
    
    public boolean hasPureVis(int amount){
        return user.hasPureVis(amount);
    }
    
    public boolean hasBrilliantVis(int amount){
        return user.hasBrilliantVis(amount);
    }
    
    public boolean hasEtherealVis(int amount){
        return user.hasEtherealVis(amount);
    }
    
    public boolean hasUnstableVis(int amount){
        return user.hasUnstableVis(amount);
    }
    
    public boolean hasRoomForVis(int amount){
        return user.hasRoomForVis(amount);
    }
    
    public boolean hasRoomForAirVis(int amount){
        return user.hasRoomForAirVis(amount);
    }
    
    public boolean hasRoomForEarthVis(int amount){
        return user.hasRoomForEarthVis(amount);
    }
    
    public boolean hasRoomForFireVis(int amount){
        return user.hasRoomForFireVis(amount);
    }
    
    public boolean hasRoomForWaterVis(int amount){
        return user.hasRoomForWaterVis(amount);
    }
    
    public boolean hasRoomForDarkVis(int amount){
        return user.hasRoomForDarkVis(amount);
    }
    
    public boolean hasRoomForPureVis(int amount){
        return user.hasRoomForPureVis(amount);
    }
    
    public boolean hasRoomForBrilliantVis(int amount){
        return user.hasRoomForBrilliantVis(amount);
    }
    
    public boolean hasRoomForEtherealVis(int amount){
        return user.hasRoomForEtherealVis(amount);
    }
    
    public boolean hasRoomForUnstableVis(int amount){
        return user.hasRoomForUnstableVis(amount);
    } 
    
    public boolean hasPermission(){
        return user.hasPermission();
    }
    
    public boolean hasAdminPermission(){
        return user.hasAdminPermission();
    }
}
