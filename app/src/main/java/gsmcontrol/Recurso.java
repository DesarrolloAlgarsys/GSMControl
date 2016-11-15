package gsmcontrol;

import java.io.Serializable;

public class Recurso implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Nombre, teléfono, fecha y estado, para realizar dos constructores.
	private String nombre;
	private String telefono;
	private String fecha;
	private String estado = ""; // Inicializamos el estado para la condición del
								// AdapterPersonalizado

	public Recurso(String nombre, String telefono) {
		super();
		this.nombre = nombre;
		this.telefono = telefono;
	}

	public Recurso(String nombre, String fecha, String estado) {
		super();
		this.nombre = nombre;
		this.fecha = fecha;
		this.estado = estado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
