package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

// Entity is a instance of a texturedModel
public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ, scale;
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale){
		
		setModel(model);
		setPosition(position);
		setRotX(rotX);
		setRotY(rotY);
		setRotZ(rotZ);
		setScale(scale);
	}	
	
	/************************************* Functions ********************************/
	// the following methods is to helps to move the entity around
	public void increasePosition(float dx, float dy, float dz){
		position.x += dx;
		position.y += dy;
		position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz){
		rotX += dx;
		rotY += dy;
		rotZ += dz;
	}
	
	/******************************** Setters And Getters ***************************/
	
	public TexturedModel getModel() {
		return model;
	}
	
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public float getRotX() {
		return rotX;
	}
	
	public void setRotX(float rotX) {
		this.rotX = rotX;
	}
	
	public float getRotY() {
		return rotY;
	}
	
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}
	
	public float getRotZ() {
		return rotZ;
	}
	
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
}
