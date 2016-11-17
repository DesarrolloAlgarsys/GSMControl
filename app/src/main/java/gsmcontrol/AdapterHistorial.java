package gsmcontrol;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.gsmcontrol.R;

public class AdapterHistorial extends ArrayAdapter<Recurso> {

	// Esta clase recibe el contexto de la aplicación y el ArrayList de recursos
	private Context contexto;
	private ArrayList<Recurso> recursos;

	public AdapterHistorial(Context context, int resource, ArrayList<Recurso> recursos) {
		super(context, resource, recursos);
		this.contexto = context;
		this.recursos = recursos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Recurso recuperado para cada position del ArrayList de recursos
		Recurso recurso = recursos.get(position);
		// Cargamos el ListView con la distribución que hemos creado
		LayoutInflater constructor = ((Activity) contexto).getLayoutInflater();
		View item = constructor.inflate(R.layout.distribucion_historial, null);

		// Elementos a mostrar dentro de la lista
		TextView nombreRecurso;
		TextView fechaRecurso;
		TextView horaRecurso;
		LinearLayout fondo;

		// Accedemos a los TextViews de la distribución
		nombreRecurso = (TextView) item.findViewById(R.id.tv_historial);
		fechaRecurso = (TextView) item.findViewById(R.id.tv_fecha);
		horaRecurso = (TextView) item.findViewById(R.id.tv_hora);
		fondo = (LinearLayout)item.findViewById(R.id.LinearLayout1);

		// Cambiamos el tamaño y el color de los textos
		nombreRecurso.setTextSize(15);
		fechaRecurso.setTextSize(12);
		horaRecurso.setTextSize(11);

		// Recogemos en un String la fecha y en otro, la hora.
		// Hacemos pequeñas modificaciones para presentarlas en un formato específico
		String fecha = recurso.getFecha().substring(0, 10);
		// Formato decha: dd/mm/aaaa
		String fechaFormateada = fecha.substring(8, 10) + "/" + fecha.substring(5, 7) + "/" + fecha.substring(0, 4);

		// Formato hora: hh:mm
		String hora = recurso.getFecha().substring(11, 16);
		hora += " h";

		// Asignamos a cada TextView lo que corresponda
		nombreRecurso.setText(recurso.getNombre());
		fechaRecurso.setText(fechaFormateada);
		horaRecurso.setText(hora);

		// Asignamos un estilo u otro dependiendo de si está OK o KO
		if (recurso.getEstado().equalsIgnoreCase("OK")) {
			fondo.setBackgroundColor(Color.parseColor("#9FF781"));
			fondo.setAlpha((float) 0.7);
		} else {
			fondo.setBackgroundColor(Color.parseColor("#FE2E2E"));
			fondo.setAlpha((float) 0.7);
		}

		return item;
	}
}
