package com.it5240.sportfriendfinding.service.cosin.similarity;

import com.it5240.sportfriendfinding.model.entity.User;
import lombok.Getter;

@Getter
public class UserWrapper {
    private User user;
    private double[] items;
    private double sim;

    public UserWrapper() {
        user = null;
        items = null;
        sim = 0;
    }

    public UserWrapper(User user) {
        this.user = user;
        items = new double[5];
        sim = 0;
    }

    public void setSim(double sim){
        this.sim = sim;
    }

    public double getItem(int i){
        return items[i];
    }

    public void setItems(double [] items){
        this.items = items;
    }

    public double itemsVectorLength() {
        double totalSquare = 0;
        for(double item : items){
            totalSquare += item*item;
        }
        return Math.sqrt(totalSquare);
    }

    public void normalized(){
        double tmp = 0;
        for(double item : items){
            tmp += item;
        }
        tmp = tmp/items.length;
        for(int i=0; i<items.length; i++){
            items[i] -= tmp;
        }
    }
}
