package com.example.hp.googlemap;

public class SelectableItem extends Vehicle{
    private boolean isSelected = false;


    public SelectableItem(Vehicle item,boolean isSelected) {
        //super(item.getAmountId(),item.getAmount());
        this.isSelected = isSelected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
