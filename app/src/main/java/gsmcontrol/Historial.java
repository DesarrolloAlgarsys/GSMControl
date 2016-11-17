package gsmcontrol;

import gsmcontrol.ConsultaHistorial.LoadingTaskFinishedListener;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import app.gsmcontrol.R;

public class Historial extends Activity implements LoadingTaskFinishedListener {

	String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historial);

		// Mostramos un Toast para indicar que el historial ha sido cargado, de
		// modo que el usuario sepa también así si se ha refrescado si ya estaba en él

		// Si no está conectado, informa de ello
		if (!isOnline()) {
			Toast.makeText(
					this,R.string.no_internet,Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, R.string.history_ok, Toast.LENGTH_SHORT).show();
		}
		// Cambiamos el título de la barra por el de la aplicación
		this.getActionBar().setTitle(R.string.app_name);

		// Mostramos el botón en la barra de la aplicación
		this.getActionBar().setDisplayHomeAsUpEnabled(true);

		// Recuperamos el número de teléfono
		tel = this.getIntent().getStringExtra("telefono");

		// Recuperamos el ArrayList generado en la tarea asíncrona
		// CosultaHistorial
		ArrayList<Recurso> historial = (ArrayList<Recurso>) this.getIntent().getSerializableExtra("historial");

		// Accedemos al TextView
		TextView tv_texto = (TextView) findViewById(R.id.tv_historial);

		// TestView que mostrará el número
		TextView tv_numeroTextView = (TextView) findViewById(R.id.tv_nombre);
		tv_numeroTextView.setText(tel);

		if (historial.isEmpty()) {
			// Le asignamos un texto u otro según haya o no datos
			tv_texto.setText(R.string.history_empty);
		} else {
			// Le asignamos un texto u otro según haya o no datos
			tv_texto.setText(R.string.last_access);

			// Accedemos al ListView y le asignamos el adaptador personalizado con nuestra distribución
			ListView lv_lista = (ListView) findViewById(R.id.lv_recursos);
			AdapterHistorial adaptador = new AdapterHistorial(this, android.R.layout.simple_list_item_1, historial);
			lv_lista.setAdapter(adaptador);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.historial, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			case R.id.cambio_numero:
				//Abrimos la Activity inicial, en la que se preguntará de nuevo
				// el teléfono. Enviamos una bandera para que la Activity borre
				// el fichero donde está guardado el teléfono anterior y escriba uno nuevo
				AlertDialog.Builder NumberBuilder = new AlertDialog.Builder(Historial.this);
				NumberBuilder.setTitle(R.string.change_number);
				NumberBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				NumberBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent number = new Intent(Historial.this, ActivityComprobaciones.class);
						number.putExtra("Cambio","nuevo");
						startActivity(number);
					}
				});
				NumberBuilder.create().show();
				break;
			case R.id.admin:
				// Abrimos la Activity que contiene el WebBrowser hacia la
				// administración de GSMControl. Le pasamos el teléfono para
				// las opciones del menú lateral del Web Browser integrado y
				// finalizamos esta Activity para evitar que quede en
				// segundo plano eternamente
				Intent web = new Intent(Historial.this, WebActivity.class);
				web.putExtra("telefono", tel);
				startActivity(web);
				finish();
				break;
			case R.id.info:
				Intent info = new Intent(Historial.this, Informacion.class);
				info.putExtra("telefono", tel);
				startActivity(info);
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Función que comprueba si está conectado o no
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}

		return false;
	}

	// Controlaremos que, si da al botón atrás, se acabe esta Activity y que no se quede en segundo plano
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Se vuelve a la parte inicial de la aplicación, terminando este activity
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// Función a la que se llama desde la tarea asíncrona para indicar que ha finalizado
	@Override
	public void onTaskFinished(ArrayList<Recurso> historial) {
		startApp(historial);
	}

	// Con esta función, se da paso a la Activity del Historial (refrescada)
	private void startApp(ArrayList<Recurso> historial) {
		Intent intent = new Intent(Historial.this, Historial.class);
		intent.putExtra("telefono", tel);
		intent.putExtra("historial", historial);
		startActivity(intent);
		// Finalizamos la Activity, ya que la hemos actualizado
		finish();
	}
}
