package gsmcontrol;

import java.io.Serializable;

public class Recurso implements Serializable {

	private static final long serialVersionUID = 1L;

	// Nombre, teléfono, horario disponible, dias disponibles, fechas
	// disponibles,  fecha y estado, para realizar dos constructores.
	private String nombre;
	private String telefono;
	private String fechaInicio;
	private String fechaFin;
	private String dias;
	private String horaInicio;
	private String horaFin;
	private String fecha;
	private String estado = ""; // Inicializamos el estado para la condición del
								// AdapterPersonalizado

	public Recurso(String nombre, String telefono, String fechaInicio, String fechaFin, String dias,  String horaInicio, String horaFin) {
		super();
		this.nombre = nombre;
		this.telefono = telefono;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.dias = dias;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
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

	public String getFechaIni() {
		return fechaInicio;
	}

	public void setFechaIni(String fechaIni) {
		this.fechaInicio = fechaIni;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getDias() {
		return dias;
	}

	public void setDias(String dias) {
		this.dias = dias;
	}

	public String getHoraIni() {
		return horaInicio;
	}

	public void setHoraIni(String horaIni) {
		this.horaInicio = horaIni;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
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
