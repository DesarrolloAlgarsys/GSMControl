package gsmcontrol;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.gsmcontrol.R;

public class AdapterMenuLateral extends ArrayAdapter<ItemMenu> {

	// Esta clase recibe el contexto de la aplicación y el ArrayList de opciones
	private Context contexto;
	private ArrayList<ItemMenu> opciones;

	public AdapterMenuLateral(Context context, int resource,
			ArrayList<ItemMenu> opciones) {
		super(context, resource, opciones);
		this.contexto = context;
		this.opciones = opciones;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Opción recuperada para cada position del ArrayList de Strings
		ItemMenu elemento = opciones.get(position);

		// Cargamos el ListView con la distribución que hemos creado
		LayoutInflater constructor = ((Activity) contexto).getLayoutInflater();
		View item = constructor.inflate(R.layout.distribucion_menu, null);

		// Elementos a mostrar dentro de la lista
		ImageView imagen = null;
		TextView textoOpcion = null;

		// Accedemos a los elementos de la distribución
		imagen = (ImageView) item.findViewById(R.id.imageView1);
		textoOpcion = (TextView) item.findViewById(R.id.tv_opcion);

		// Asignamos la imagen
		imagen.setImageResource(elemento.getIdImagen());

		// Cambiamos el tamaño y color del texto
		textoOpcion.setTextColor(Color.WHITE);
		textoOpcion.setTextSize(15);

		// Asignamos al TextView la opción
		textoOpcion.setText(elemento.getOpcion());

		return item;
	}
}
