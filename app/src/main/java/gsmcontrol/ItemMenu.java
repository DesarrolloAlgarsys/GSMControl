package gsmcontrol;

public class ItemMenu {

	private String opcion;
	private int idImagen;

	public ItemMenu(String opcion, int idImagen) {
		super();
		this.opcion = opcion;
		this.idImagen = idImagen;
	}

	public String getOpcion() {
		return opcion;
	}

	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}

	public int getIdImagen() {
		return idImagen;
	}

	public void setIdImagen(int idImagen) {
		this.idImagen = idImagen;
	}

}
