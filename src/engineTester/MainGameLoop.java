package engineTester;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import renderingEngine.DisplayManager;
import renderingEngine.Loader;
import renderingEngine.OBJLoader;
import renderingEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;


public class MainGameLoop {
	
	/**
	 * @param args 
	 */

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		
		
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
		TexturedModel textureModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(textureModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()){
			//entity.increasePosition(0, 0, -0.1f);
			entity.increaseRotation(0, 1, 0);
			
			//Move camera every single frame
			camera.move();
			
			//game logic
			renderer.prepare();
			
			//Here to start the shader program
			shader.start();
			
			//Load up every frame
			shader.loadViewMatrix(camera);
			
			// Render the model to produce on
			renderer.render(entity, shader);
			
			//Here to stop the shader program
 			shader.stop();
			
			DisplayManager.updateDisplay();			
		}

		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
