package gsmcontrol;

import gsmcontrol.ConsultaBBDD.LoadingTaskFinishedListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import app.gsmcontrol.R;

public class SplashScreen extends Activity implements LoadingTaskFinishedListener {

	String tel = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Ocultamos la barra para esta pantalla
		SplashScreen.this.getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		leerFichero();

		// Ejecutamos la tarea asíncrona, donde accederemos a la BBDD
		// para recuperar los recursos del número
		new ConsultaBBDD(tel, this).execute("0");
	}

	public void leerFichero() {
		// Leemos fichero y recuperamos el teléfono que hay guardado en él.
		BufferedReader fin;
		try {
			fin = new BufferedReader(new InputStreamReader(openFileInput("telefono.txt")));
			tel = fin.readLine();
			fin.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Función a la que se llama desde la tarea asíncrona para indicar que ha finalizado
	@Override
	public void onTaskFinished(ArrayList<Recurso> recursos) {
		completeSplash(recursos);
	}

	private void completeSplash(ArrayList<Recurso> recursos) {
		startApp(recursos);
		finish(); // Finalizamos para que no se pueda volver a la pantalla
	}

	// Con esta función, se da paso a la Activity principal
	private void startApp(ArrayList<Recurso> recursos) {
		Intent intent = new Intent(SplashScreen.this, MainActivity.class);
		intent.putExtra("telefono", tel);
		intent.putExtra("recursos", recursos);
		startActivity(intent);
	}
}