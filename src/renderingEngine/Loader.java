package renderingEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Loader {

	// Memory Management - delete all the VAOs and VBOs when exit the game
	// And also keep track of the VAOs and VBOs that have created in here.
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	//This Class is going to deal with the loading of 3D model into the memory by storing the data in the VAO (vertex Array Object) 
	public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices){
		int vaoID = createVAO(); //Store vaoID variable 
		bindIndidesBuffer(indices); // bind the indices to the buffer(VBO)
		storeDataInAttributeList(0, 3, positions); // this is for the coordinates (xyz) so 2nd parameter is 3. 1st parameter is 0 because is in VAO index 0
		storeDataInAttributeList(1, 2, textureCoords); // (u,v) texture coordinates only contain 2 points so is 2. 1st parameter is 1 because is in VAO index 1
		unbindVAO() ;
		return new RawModel(vaoID, indices.length); // each position has 3 vertices 
	}
	
	public int loadTexture(String fileName){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+ fileName + ".png"));
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	// When exit the game this method will delete all the vaos and vbos looping through the arrayList
	public void cleanUp(){
		//Delete VAOs
		for(int vao:vaos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		
		//Delete VBOs
		for(int vbo:vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
		
		//Delete Textures
		for(int texture:textures)
		{
			GL11.glDeleteTextures(texture);
		}
	}
	
	//Create an empty VAO then return the ID of the VAO
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID); // activate VAO by using the glBIND and vaoID
		return vaoID;
	}
	
	// Store the data by taking in number of data and the data itself
	private void storeDataInAttributeList(int attributeNumber, int coordinatesSize, float[] data){
		// Vertex Buffer Object (VBO) -  array of integer value, VBO is stored in VAO 
		int vboID = GL15.glGenBuffers(); // glGenBuffer creates an empty VBO and return the ID
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); // Bind the buffer taking in (the type of the buffer, and the vboID)
		
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); //(what data gonna be use for) GL_STATIC_DRAW - never want to edit the data once it's stored in the VBO
		GL20.glVertexAttribPointer(attributeNumber, coordinatesSize, GL11.GL_FLOAT, false, 0, 0);
		  // attributeNumber - number of the attribute list
		  // 3 - is because is 3D effect
		  // GL11.GL_FLOAT - data type
		  // false - data is not normalize
		  // 0 - distance between each vertices, 0 means no data between each vertices
		  // 0 - the offset, 0 is start it at the beginning
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // GL_ARRAY_BUFFER is the type, and 0 is to unbind the buffer set it to 0
		
	}
	
	//VAO will stay bind until the method is called to unbind the VAO
	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	
	// This will load up the indices buffer and bind it to the VAO that need to be render
	private void bindIndidesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data); //this put data into the Float Buffer
		buffer.flip(); // This prepare the buffer to be read from, this also say that the data is finished writing to it
		
		return buffer; // return and use to store it in the VBO
	}
}
