package dk.dma.esim.virtualship;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import dk.dma.ais.message.AisMessage;

public abstract class AbstractVirtualShip implements IVirtualShip {

    protected int length;
    protected int width;
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
    //protected double rot;
    
    protected Integer shipHeading;
    protected Integer shipType;
    protected String shipName;

    public AbstractVirtualShip() {
        this.node = new Node();
        this.length = 30;
        this.width = 0;
        this.mass = 0;
        this.speed = 0;
        this.cog = 0;
        this.forwardSpeed = 0;
        this.rudderAngle = 0;
        this.rudderTurnRate = 1;
        this.valid = false;
    }

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
    
    /**
     * Virtual ship
     */
    public void update() {

//        Vector3f v = this.node.getLocalTranslation();
//        Vector3f o = this.node.getLocalRotation().getRotationColumn(2);
//
        
        //this.node.rotate(0f, (float)this.rudderAngle * 0.001f * (float)this.forwardSpeed, 0.0f);
        Quaternion q = new Quaternion();
        this.node.rotate(q);
//        this.node.setLocalTranslation(v.add(o.mult(0.1f * (float)this.forwardSpeed)));
        
    }
    
    /**
     * Below is used for ais ships
     */
    
    public boolean isValid(){
        return this.valid;
    }
    
    public double getRot(double t){
        
        double rot = 0.0;
        if(this.rudderAngle > 35.0) this.rudderAngle = 35.0;
        
        double t1 = this.length/this.forwardSpeed;
        double rotFinal = (1.2/t1)*this.rudderAngle;
        
        if(rotFinal>3.0) rotFinal = 3.0;
            
        if(t<t1){
           rot = t*rotFinal/t1;
        } else {
           rot = rotFinal;
        }
        
        System.out.println("Rate of Turn: " + rot);
        
        return rot > 3.0 ? 3.0 : rot;
    }
    
//    public void setRot(double rot){
//        this.rot = rot;
//    }
    
    public void setNode(String mmsi) {
        this.mmsi = Integer.parseInt(mmsi);
        this.node = new Node(mmsi);
    }

    public void setName(String name) {
        this.shipName = name;
    }

    public void setShipHeading(Integer heading) {
        this.shipHeading = heading;
    }

    public void setShipType(Integer type) {
        this.shipType = type;
    }

    public String getName() {
        return this.shipName;
    }

    public Integer getShipHeading() {
        return this.shipHeading;
    }

    public Integer getShipType() {
        return this.shipType;
    }

    /**
     * Used for the virutal ais ships
     */
    public void updateSimple() {
        Vector3f v = this.node.getLocalTranslation();
        Vector3f o = this.node.getLocalRotation().getRotationColumn(2);

        this.node.setLocalTranslation(v.add(o.mult(0.1f * (float) this.forwardSpeed)));
    }
}
