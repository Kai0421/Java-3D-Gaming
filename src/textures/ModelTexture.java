package textures;

public class ModelTexture {
	private int textureID;
	
	public ModelTexture(int id){
		setTextureID(id);
	}

	/******************************** Setters And Getters ***************************/
	public int getID() {
		return textureID;
	}

	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}
}
