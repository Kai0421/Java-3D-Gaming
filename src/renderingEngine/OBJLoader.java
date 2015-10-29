package renderingEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader {

	// return a raw model to Loader class method load to VAOs to load the model
	public static RawModel loadObjModel(String fileName, Loader loader){
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not load the OBJ file!");
			e.printStackTrace();
		}
		
		//BufferedReader is created to allow to read from this file 
		BufferedReader reader = new BufferedReader(fr);
		
		String line; // Thats where we gonna read in each line of the file
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		float[] verticesArray = null, normalsArray = null, texturesArray = null;
		int[] indicesArray = null;
		
		try{
			while(true)
			{
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				System.out.println(line + "\n");
				
				//V = vertex position
				if(line.startsWith("V "))
				{
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					
					//once you get the vertex add it into the array of vertices
					vertices.add(vertex);
				}
				else if(line.startsWith("Vt "))
				{
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}
				else if(line.startsWith("Vn "))
				{
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}
				else if(line.startsWith("f "))
				{
					texturesArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			
			while(line != null)
			{
				if(!line.startsWith("f "))
				{
					line = reader.readLine();
					System.out.println(reader.readLine() + "\n");
					continue;
				}
				
				
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
				
				line = reader.readLine();
			}
			// when finished close the reader
			reader.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()]; // convert the indices list into int array
		
		// loop throught the vertices array copy across the data
		int vertexPointer = 0;
		for(Vector3f vertex : vertices)
		{
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++)
		{
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticesArray, texturesArray, indicesArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] texturesArray, float[] normalsArray){
		int currentVexterPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVexterPointer);
		
		//get texture that corresponds to the vertex
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		
		// add it into textures array and add it into the same in the vertex position *2 because texture coords have 2 float
		// and adding 1 for the second texture float for the y position. and 1 - the currentex because open gl start the at top left corner where as blender starts at bottom left
		texturesArray[currentVexterPointer * 2] = currentTex.x;
		texturesArray[currentVexterPointer * 2 + 1] = 1 - currentTex.y;
		
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVexterPointer * 3] = currentNorm.x;
		normalsArray[currentVexterPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVexterPointer * 3 + 2] = currentNorm.z;	
	}
}
