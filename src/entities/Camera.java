package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
		
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch; // camera rotation xyz axis how high or low the camera is aimed
	private float yaw;	// how much left or right the camera is aiming
	private float roll;	// how much its tilted at one side
	
	public Camera(){
		
	}
	
	/************************************* Functions ********************************/
	// allow the camera to able to move around
	public void move(){
		// Move every frame
		
		//Forward
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			position.z -= 0.02f;
		}
		//Right
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			position.x += 0.02f;
		}
		//Left
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			position.x -= 0.02f;
		}
		//Backward
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			position.z += 0.02f;
		}
	}
	
	
	/******************************** Setters And Getters ***************************/
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
	
}
