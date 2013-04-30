/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dma.esim.virtualship;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dk.dma.ais.message.AisMessage;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.ship.ShipType;

public abstract class VirtualShip implements IVirtualShip {

    //protected Position position;
    protected int length;
    protected int width;
    //protected ShipType type;
    protected int mass;
    protected int speed;
    protected int cog;
    protected int mmsi;
    protected int bow;
    protected int port;
    protected int starboard;
    protected int stern;
    protected double latitude;
    protected double longitude;
    protected AisMessage aisMessage;
    protected double rudderAngle;
    protected double rudderTurnRate;
    protected Spatial spatial;
    protected Node node;
    protected Material material;
    protected double forwardSpeed;
    protected boolean valid;

    public VirtualShip() {
        this.node = new Node();
        //this.position = Position.create(0.0, 0.0);
        this.length = 0;
        this.width = 0;
        //this.type = ShipType.UNDEFINED;
        this.mass = 0;
        this.speed = 0;
        this.cog = 0;
        this.forwardSpeed = 0;
        this.rudderAngle = 0;
        this.rudderTurnRate = 1;
        this.valid = false;
    }

//    public Position getPos() {
//        return this.position;
//    }
//
//    public void setPos(Position pos) {
//        this.position = pos;
//    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setCog(int cog) {
        this.cog = cog;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setWidth(int width) {
        this.width = width;
    }

//    public void setType(ShipType type) {
//        this.type = type;
//    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public void setShipAisMessage(AisMessage aisMessage) {
        this.aisMessage = aisMessage;
    }

    public void setShipBow(int bow) {
        this.bow = bow;
    }

    public void setShipPort(int port) {
        this.port = port;
    }

    public void setShipStarboard(int starboard) {
        this.starboard = starboard;
    }

    public void setSHipStern(int stern) {
        this.stern = stern;
    }

    public void setShipLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setShipLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRudderAngle(double angle) {
        this.rudderAngle = angle;
    }

    public void setRudderTurnSpeed(double rateOfTurn) {
        this.rudderTurnRate = rateOfTurn;
    }

    public void setMaterial(Material material) {
        this.material = material;
        if(this.spatial != null) this.spatial.setMaterial(this.material);
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }
    
    public void setForwardSpeed(double speed){
        this.forwardSpeed = speed;
    }
    
    public void setValid(boolean valid){
        this.valid = valid;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getCog() {
        return this.cog;
    }

    public int getLength() {
        return this.length;
    }

    public int getWidth() {
        return this.width;
    }

//    public ShipType getType() {
//        return this.type;
//    }

    public int getMass() {
        return this.mass;
    }

    public AisMessage getShipAisMessage() {
        return this.aisMessage;
    }

    public int getShipBow() {
        return this.bow;
    }

    public int getShipPort() {
        return this.port;
    }

    public int getShipStarboard() {
        return this.starboard;
    }

    public int getShipStern() {
        return this.stern;
    }

    public double getShipLatitude() {
        return this.latitude;
    }

    public double getShipLongitude() {
        return this.longitude;
    }

    public int getShipMmsi() {
        return this.mmsi;
    }

    public double getRudderAngle() {
        return this.rudderAngle;
    }

    public void incrementRudder() {
        this.rudderAngle+=this.rudderTurnRate;
    }

    public void decrementRudder() {
        this.rudderAngle-=this.rudderTurnRate;
    }

    public Spatial getSpatial() {
        return this.spatial;
    }

    public Material getMaterial() {
        return this.material;
    }

    public double getForwardSpeed(){
        return this.forwardSpeed;
    }
    
    public Node getNode() {
        return this.node;
    }
    
    public void update() {

        Vector3f v = this.node.getLocalTranslation();
        Vector3f o = this.node.getLocalRotation().getRotationColumn(2);

        this.node.rotate(0f, (float)this.rudderAngle * 0.001f * (float)this.forwardSpeed, 0.0f);
        this.node.setLocalTranslation(v.add(o.mult(0.1f * (float)this.forwardSpeed)));

    }
    
    public boolean isValid(){
        return this.valid;
    }
}
