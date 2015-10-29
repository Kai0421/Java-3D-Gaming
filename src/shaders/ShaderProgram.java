package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram {
	
	private int programID, vertexShaderID, fragmentShaderID;
	
	//this private floatbuffer is to load up the matrices, so whenever we want to reuse the matrices again will be in the buffer
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16); // using 4x4 matrices so it 16 float
	
	public ShaderProgram(String vertexFile, String fragmentFile){
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram(); // using GL20 create program get the ID of the program
		
		//Now attach the programID and the shader ID together and fragment ID with ProgramID
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	// this gets all the uniform location
	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	//this method load up at matrix into the shader code into a mat4 variable
	protected void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	// this method will be used whenever the program need to started
	public void start(){
		GL20.glUseProgram(programID);
	}
	
	// this is to stop the program
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	// for memory management 
	public void cleanUp(){
		stop(); // this make sure that the program is stop before they are being detached
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
		
	}
	
	// this method is to bind an attribute, and it cannot be done outside of this class because it needs the programID
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
		
	protected abstract void bindAttributes();
	

	//load up a float, it takes in a location of the uniform variable, and take in the value that we want to load into that uniform
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	//Vec3 cus its using 3float
	// this method will load the vector into a uniform. Vec3 uniform variable in GLSL load to the uniform
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	// load up a boolean. In shader code there is no boolean. This is gonna load up either a 0 or 1 depending its true or false
	protected void loadBoolean(int location, boolean value){
		float toLoad = 0;
		
		if(value) // if true load up a one.
		{
			toLoad = 1;
		}
		
		//load up 1 float to that location in the shader code 
		GL20.glUniform1f(location, toLoad);
	}
	
	
	//this will return the sharderID of either vertex or fragment shader.
	private static int loadShader(String file, int type){
		//type in the parameter is to determined is either a vertex or a fragment
		StringBuilder shaderSource = new StringBuilder();
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null){
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch(IOException e){
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS)== GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		
		return shaderID;
	}
}
