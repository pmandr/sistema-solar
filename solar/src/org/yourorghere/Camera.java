package org.yourorghere;

public class Camera
{
    private double xPos;
    private double yPos;
    private double zPos;
    
    private double xLPos;
    private double yLPos;
    private double zLPos;
    
    private double pitch;
    private double yaw;
    
    public Camera()
    {
        xPos = 0;
        yPos = 0;
        zPos = 0;
        
        xLPos = 0;
        yLPos = 0;
        zLPos = 10;
    }
    
    public Camera(double xPos, double yPos, double zPos, double xLPos, double yLPos, double zLPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        
        this.xLPos = xLPos;
        this.yLPos = yLPos;
        this.zLPos = zLPos;
    }
    
    public void setPitch(double pitch)
    {
    	this.pitch = pitch;
    }
    
    public void setYaw(double yaw)
    {
    	this.yaw = yaw;
    }

    public void updatePosition(double xPos, double yPos, double zPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }
    
    public void lookPosition(double xLPos, double yLPos, double zLPos)
    {
        this.xLPos = xLPos;
        this.yLPos = yLPos;
        this.zLPos = zLPos;
    }

    
    // Moves the entity forward according to its pitch and yaw and the magnitude.
    
    public void moveForward(double magnitude)
    {
        double xCurrent = this.xPos;
        double yCurrent = this.yPos;
        double zCurrent = this.zPos;
        
        // Spherical coordinates maths
        double xMovement = magnitude * Math.cos(pitch) * Math.cos(yaw); 
        double yMovement = magnitude * Math.sin(pitch);
        double zMovement = magnitude * Math.cos(pitch) * Math.sin(yaw);
              
        double xNew = xCurrent + xMovement;
        double yNew = yCurrent + yMovement;
        double zNew = zCurrent + zMovement;
        
        updatePosition(xNew, yNew, zNew);
    }

    public void moveUp(double magnitude) {
        double xCurrent = this.xPos;
        double yCurrent = this.yPos;
        double zCurrent = this.zPos;

        double yMovement = magnitude;

        double xNew = xCurrent;
        double yNew = yCurrent + yMovement;
        double zNew = zCurrent;
        updatePosition(xNew, yNew, zNew);
    }
    
    public void strafeLeft(double magnitude)
    {
        double pitchTemp = pitch;
        pitch = 0;
        yaw = yaw - (0.5 * Math.PI);
        moveForward(magnitude);

        pitch = pitchTemp;
        yaw = yaw + (0.5 * Math.PI);
    }
    
    public void strafeRight(double magnitude)
    {
        double pitchTemp = pitch;
        pitch = 0;
        yaw = yaw + (0.5 * Math.PI);
        moveForward(magnitude);
        yaw = yaw - (0.5 * Math.PI);
        pitch = pitchTemp;
    }
    
    
    public void look(double distanceAway)
    {
        System.out.println("pitch: " + pitch);
        if(pitch > 1.4)
        pitch = 1.4;
        
        if(pitch < -1.4)
        pitch = -1.4;
         
        moveForward(distanceAway);
        
        double xLook = xPos;
        double yLook = yPos;
        double zLook = zPos;
        
        moveForward(-distanceAway);
        
        lookPosition(xLook, yLook, zLook);
    }
           
    
    /* -------Get commands--------- */
    
    public double getXPos()
    {
        return xPos;
    }
    
    public double getYPos()
    {
        return yPos;
    }
    
    public double getZPos()
    {
        return zPos;
    }
    
    public double getXLPos()
    {
        return xLPos;
    }
    
    public double getYLPos()
    {
        return yLPos;
    }
    
    public double getZLPos()
    {
        return zLPos;
    }
    
    public double getPitch()
    {
        return pitch;
    }
    
    public double getYaw()
    {
        return yaw;
    }
    
    /* --------------------------- */
    
    /* -------------- Pitch and Yaw commands --------------- */
    
    public void pitchUp(double amount)
    {
        this.pitch += amount;
    }
    
    public void pitchDown(double amount)
    {
        this.pitch -= amount;
    }
    
    public void yawRight(double amount)
    {
        this.yaw += amount;
    }
    
    public void yawLeft(double amount)
    {
        this.yaw -= amount;
    }
    
    /* ---------------------------------------------------- */
    
}
