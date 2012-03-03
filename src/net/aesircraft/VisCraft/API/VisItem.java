package net.aesircraft.VisCraft.API;

import net.aesircraft.VisCraft.Data.Items;

public class VisItem {

    /*
     * NEVER USE Items The reason for this layer is to seperate the API from my
     * Classes If something in there changes your plugin will not break while
     * using this API This will remain the way it is and properly deprecated
     * when needed.
     *
     * YOU HAVE BEEN WARNED!
     */
    private int id = 0;
    private boolean loaded = false;

    public VisItem(int itemId) {
        id = itemId;
        loaded = true;
    }

    public String getAlignment() {
        return Items.getAlignment(id);
    }

    public int getVis() {
        return Items.getVis(id);
    }

    public int getCharge() {
        return Items.getCharge(id);
    }

    public int getQuality() {
        return Items.getQuality(id);
    }

    public void setAlignment(String alignment) {
        Items.setAlignment(id, alignment);
    }

    public void setVis(int amount) {
        Items.setVis(id, amount);
    }

    public void setCharge(int amount) {
        Items.setCharge(id, amount);
    }

    public void setQuality(int amount) {
        Items.setQuality(id, amount);
    }
}
