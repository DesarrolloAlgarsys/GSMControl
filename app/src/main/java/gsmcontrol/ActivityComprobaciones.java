package gsmcontrol;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.Toast;

import app.gsmcontrol.R;

public class ActivityComprobaciones extends Activity implements gsmcontrol.ConsultaTelefono.LoadingTaskFinishedListener {

	//Telefono para pruebas. Al finalizar pruebas, dejar en blanco
	String tel ="610228472";
	//String tel = "";
	boolean bandera = false;
	Bundle extras;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Ocultamos la barra para esta pantalla
		ActivityComprobaciones.this.getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comprobaciones);
		extras=getIntent().getExtras();

		if (extras!=null){
			tel="";
		}
		else {
			// Intentamos leer el fichero.
			leerFichero();
		}

		// En caso de no existir el fichero:
		// Comprobaremos el número en la base de datos y, en caso de ser
		// correcto, lo guardamos en el fichero e iniciamos la SplashScreen
		if (tel.equals("")) {
			// Pedimos el teléfono, que posteriormente comprobaremos con la base de datos
			pedirTelefono();
		}
		// Si el número ya existe en el fichero, iniciaremos la SplashScreen
		else {
			startApp();
		}
	}

	// Función que comprueba si está conectado o no
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public void leerFichero() {
		// Leemos fichero (si lo hay) y guardamos el teléfono que hay guardado en él.
		BufferedReader fin;
		try {
			fin = new BufferedReader(new InputStreamReader(openFileInput("telefono.txt")));
			tel = fin.readLine();
			fin.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void escribirFichero() {
		// Escribimos en el fichero el teléfono introducido
		OutputStreamWriter fout;
		try {
			fout = new OutputStreamWriter(openFileOutput("telefono.txt", Context.MODE_PRIVATE));
			fout.write(tel);
			fout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Función para pedir el teléfono al usuario en caso de no tenerlo ya guardado
	public void pedirTelefono() {
		// Pediremos el teléfono mediante un diálogo
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.login);
		alert.setMessage(R.string.ask_number);

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		// Cambiamos el teclado por uno numérico
		input.setInputType(InputType.TYPE_CLASS_PHONE);

		alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Editable value = input.getText();
						tel = value.toString();
						// Ocultamos el teclado
						InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
						realizarllamada();
					}
				});
		alert.setNegativeButton("Cancelar",	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Si selecciona Cancelar, se sale de la aplicacion, ya
						// que necesita el número para realizar la consulta a la BBDD
						finish();
					}
				});
		alert.setOnCancelListener(new OnCancelListener() {

			// Controlamos si sale del di�logo pulsando atr�s en el tel�fono o
			// pulsando fuera del di�logo para cerrarlo sin elegir opci�n
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// Si cancela, se sale de la aplicación, ya que necesita
				// el número para realizar la consulta a la BBDD
				finish();
			}
		});
		alert.show();
	}

	// Mostrará un diálogo advirtiendo de la realización de una llamada de
	// comprobación para verificar el número introducido
	public void realizarllamada() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setIcon(android.R.drawable.sym_action_call);
		alert.setTitle(R.string.validation_call);
		alert.setMessage(R.string.validation_text);
		alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				try {
					Intent llamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:628101064"));
					startActivity(llamada);
					// Actualizamos la bandera para el onResume()
					bandera = true;
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Si selecciona Cancelar, se sale de la aplicación, ya
				// que es necesario verificar el número
				finish();
			}
		});
		alert.setOnCancelListener(new OnCancelListener() {

			// Controlamos si sale del di�logo pulsando atr�s en el tel�fono o
			// pulsando fuera del di�logo para cerrarlo sin elegir opci�n
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// Si cancela, se sale de la aplicación, ya que necesita
				// el número para realizar la consulta a la BBDD
				finish();
			}
		});
		alert.show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (bandera) {
			// Dormimos al proceso un poco para que la base de datos llegue a
			// guardar el registro de la llamada
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), R.string.checking_number, Toast.LENGTH_LONG).show();
			// Ejecutamos la tarea asíncrona, donde accederemos a la BBDD
			new ConsultaTelefono(tel, ActivityComprobaciones.this).execute("0");
			// Actualizamos la bandera de nuevo para mostrarlo únicamente cuando
			// se le cambia a true al llamar
			bandera = false;
		}
	}

	// Controlaremos que, si da al botón atrás se salga app
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// Función a la que se llama desde la tarea asíncrona para indicar que ha finalizado
	@Override
	public void onTaskFinished(boolean comprobado) {
		if (comprobado) {
			escribirFichero();
			startApp();
		} else {
			// Si no está conectado, informa de ello
			if (!isOnline()) {
				Toast.makeText(this,R.string.no_internet, Toast.LENGTH_LONG).show();
			}
			else {
				// Si no se ha verificado, informa de ello y vuelve a pedir el teléfono
				Toast.makeText(this, R.string.wrong_number, Toast.LENGTH_LONG).show();
				pedirTelefono();
			}
		}
	}

	// Con esta función, se da paso a la SplashScreen
	private void startApp() {
		Intent intent = new Intent(ActivityComprobaciones.this, SplashScreen.class);
		startActivity(intent);
		finish(); // Finalizamos para que no se pueda volver a la pantalla
	}
}