package gsmcontrol;

import java.util.ArrayList;
import java.util.List;

import app.gsmcontrol.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterPersonalizado extends ArrayAdapter<Recurso> {

	// Esta clase recibe el contexto de la aplicación y el ArrayList de recursos
	private Context contexto;
	private ArrayList<Recurso> recursos;
	
	public AdapterPersonalizado(Context context, int resource,ArrayList<Recurso> recursos) {
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
		View item = constructor.inflate(R.layout.distribucion_personalizada, null);

		// Elementos a mostrar dentro de la lista
		TextView nombreRecurso;
		TextView horarioRecurso;
		TextView diasRecurso;
		TextView fechasRecurso;

		// Accedemos a los TextViews de la distribución
		nombreRecurso = (TextView) item.findViewById(R.id.tv_nombre);
		horarioRecurso = (TextView) item.findViewById(R.id.tv_horario);
		diasRecurso = (TextView) item.findViewById(R.id.tv_dias);
		fechasRecurso = (TextView) item.findViewById(R.id.tv_fechas);

		// Cambiamos el tamaño de los textos
		nombreRecurso.setTextSize(22);
		horarioRecurso.setTextSize(10);
		diasRecurso.setTextSize(10);
		fechasRecurso.setTextSize(10);

		// Asignamos a cada TextView lo que corresponda
		nombreRecurso.setText(recurso.getNombre());
		String schedule = contexto.getString(R.string.schedule) + "no disponible"; //recurso.getHorario()
		horarioRecurso.setText(schedule);
		String days = contexto.getString(R.string.week_day) + "no disponible"; //recurso.getDias()
		diasRecurso.setText(days);
		String dates = contexto.getString(R.string.dates) + "no disponible"; //recurso.getFechas()
		fechasRecurso.setText(dates);

		return item;
	}
}
