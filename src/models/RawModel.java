package models;

public class RawModel {
	
	// This class is going to represent a 3D model stored in memory
	private int vaoID; // Vertex Array Object ID
	private int vertexCount;
	
	public RawModel(int vaoID, int vertexCount){
		setVaoID(vaoID);
		setVertexCount(vertexCount);
	}

	/******************************** Setters And Getters ***************************/
	public int getVaoID() {
		return vaoID;
	}

	public void setVaoID(int vaoID) {
		this.vaoID = vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}
	
}
