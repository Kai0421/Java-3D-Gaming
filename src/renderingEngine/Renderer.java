package renderingEngine;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import toolbox.Maths;
import entities.Entity;

public class Renderer {
	
	private static final float FOV = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	// Set up the projection matrix
	private Matrix4f projectionMatrix;
	
	//construstor and take in the staicshader cus this shader is not going to change
	public Renderer(StaticShader shader){
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	// It will be called once every frame, it will just prepare the openGL to render the game
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(1, 0, 0, 1); // (1, 0, 0, 1); this will set the backgroud to red color
	}
	
	public void render(Entity entity, StaticShader shader){
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID()); // first bind the vao, whenever vao is needed to be used, the program need to bind it first
		GL20.glEnableVertexAttribArray(0); // I programmed the VertexAttributeArray to start from the begining in the loader class
		GL20.glEnableVertexAttribArray(1); // This enable index 1 in VAO on the texture attributes
		
		//Load up the entity's transformation(scale, rotation, and position) in matrix form
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		
		//Load the transformation matrix to the shader
		shader.loadTransformationMatrix(transformationMatrix);
		
		//Put it into a texture bank, have to activate the first texture bank
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID()); // takes in the type of texture which is 2DTexture and the textureID
		
		// now to render it (Older method)
		//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount()); 
		// (render the triangles, where the data should start which is 0, and the number of vertices it needs to render)
		/**Newer Method**/
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0); // after that finishing need to disable the attributes list
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0); // then unbind the array instead putting vaoIDs in here put 0 to unbind.
	}
	
	// Create the Projection
	private void createProjectionMatrix(){
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length);
		projectionMatrix.m33 = 0;
		
	}
	
}
