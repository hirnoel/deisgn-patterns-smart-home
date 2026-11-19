package com.smarthome.thirdparty;


public class MotorizedBlinds {
    private String location;
    private String name;
    private Double openPercentage;
    
    public MotorizedBlinds(String name, String location){
        this.name = name;
        this.location = location;
        openPercentage = 100.0;  //100 = fully open, default 
    }


    public double getPositionsStatus(){return this.openPercentage;}
    public String getName(){return  this.name;}
    public String getPosition(){return this.location;}
    public void raiseBlinds(Double percent){

        if (this.getPositionsStatus() > percent || percent >=
         100.0 ) return;
        this.openPercentage = percent;        
    }

    public void lowerBlinds(Double percent){
        
        if (this.getPositionsStatus() <= percent || percent < 0.0 ) return;
        this.openPercentage = percent;        
    }

}
