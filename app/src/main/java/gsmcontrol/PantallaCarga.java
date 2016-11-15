package gsmcontrol;

import gsmcontrol.ConsultaHistorial.LoadingTaskFinishedListener;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;
import app.gsmcontrol.R;

public class PantallaCarga extends Activity implements LoadingTaskFinishedListener {

	String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PantallaCarga.this.getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pantalla_carga);

		// Si no está conectado, informa de ello
		if (!isOnline()) {
			Toast.makeText(
					this,R.string.no_internet,Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, R.string.loading_history, Toast.LENGTH_LONG)
					.show();
		}

		// Recuperamos el número de teléfono
		tel = this.getIntent().getStringExtra("telefono");
		ProgressBar proceso = (ProgressBar) findViewById(R.id.progressBar1);

		// Necesitaremos tarea asíncrona para realizar la consulta en segundo plano.
		// Ejecutamos la tarea asíncrona, donde accederemos a la
		// BBDD para recuperar el historial de accesos
		new ConsultaHistorial(tel, PantallaCarga.this).execute("0");
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

	// Función a la que se llama desde la tarea asíncrona para indicar que ha finalizado
	@Override
	public void onTaskFinished(ArrayList<Recurso> listado) {
		startApp(listado);
	}

	// Con esta función, se da paso a la Activity del Historial
	private void startApp(ArrayList<Recurso> listado) {
		Intent intent = new Intent(PantallaCarga.this, Historial.class);
		intent.putExtra("telefono", tel);
		intent.putExtra("historial", listado);
		startActivity(intent);
		finish();
	}
}
