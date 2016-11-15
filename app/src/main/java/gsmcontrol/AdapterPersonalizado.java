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

		// Accedemos a los TextViews de la distribución
		nombreRecurso = (TextView) item.findViewById(R.id.tv_nombre);

		// Cambiamos el tamaño de los textos
		nombreRecurso.setTextSize(15);

		// Asignamos a cada TextView lo que corresponda
		nombreRecurso.setText(recurso.getNombre());

		return item;
	}
}
