package gsmcontrol;

import java.io.Serializable;

public class Recurso implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Nombre, teléfono, horario disponible, dias disponibles, fechas
	// disponibles,  fecha y estado, para realizar dos constructores.
	private String nombre;
	private String telefono;
	private String horario;
	private String dias;
	private String fechas;
	private String fecha;
	private String estado = ""; // Inicializamos el estado para la condición del
								// AdapterPersonalizado

	public Recurso(String nombre, String telefono) {
		super();
		this.nombre = nombre;
		this.telefono = telefono;
	}

/*
	public Recurso(String nombre, String telefono, String horario, String dias, String fechas) {
		super();
		this.nombre = nombre;
		this.telefono = telefono;
		this.horario = horario;
		this.dias = dias;
		this.fechas = fechas;
	}
*/
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

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getDias() {
		return dias;
	}

	public void setDias(String dias) {
		this.dias = dias;
	}

	public String getFechas() {
		return fechas;
	}

	public void setFechas(String fechas) {
		this.fechas = fechas;
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
