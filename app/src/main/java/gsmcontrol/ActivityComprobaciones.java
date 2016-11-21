package gsmcontrol;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import app.gsmcontrol.R;

public class ActivityComprobaciones extends Activity implements	gsmcontrol.ConsultaTelefono.LoadingTaskFinishedListener {

	String tel = "";
	boolean bandera = false;
	Bundle extras;
	String cambio = "numero ";
	TextView cargando;
	ProgressBar pb;
	String num_inicio="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Ocultamos la barra para esta pantalla
		ActivityComprobaciones.this.getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comprobaciones);
		extras = getIntent().getExtras();
		cargando = (TextView)findViewById(R.id.Cargando);
		pb = (ProgressBar)findViewById(R.id.progressBar);

		new JSONTask().execute("http://gsmcontrol.es/testdrive/webservices/consulta.php?");

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
		// Leemos fichero (si lo hay) y guardamos el teléfono que hay guardado
		// en él.
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

	// Función para pedir el teléfono al usuario en caso de no tenerlo ya
	// guardado
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
						// Canceled.
						// Si selecciona Cancelar, se sale de la aplicación, ya
						// que necesita el número para realizar la consulta a la
						// BBDD
						finish();
					}
				});

		alert.setOnCancelListener(new OnCancelListener() {

			// Controlamos si sale del diálogo pulsando atrás en el teléfono o
			// pulsando fuera del diálogo para cerrarlo sin elegir opción
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
	// comprobación para verificar eúmero introducido
	public void realizarllamada() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setIcon(android.R.drawable.sym_action_call);

		alert.setTitle(R.string.validation_call);
		alert.setMessage(R.string.validation_text);

		alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				try {
					Intent llamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+num_inicio));
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
				// Canceled.
				// Si selecciona Cancelar, se sale de la aplicación, ya
				// que es necesario verificar el número
				finish();
			}
		});

		alert.setOnCancelListener(new OnCancelListener() {

			// Controlamos si sale del diálogo pulsando atrás en el teléfono o
			// pulsando fuera del diálogo para cerrarlo sin elegir opción
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
			pb.setVisibility(View.VISIBLE);
			cargando.setText(R.string.checking_number);
			// Dormimos al proceso un poco para que la base de datos llegue a
			// guardar el registro de la llamada
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Ejecutamos la tarea asíncrona, donde accederemos
			// a la BBDD
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

	// Función a la que se llama desde la tarea asíncrona para indicar que ha
	// finalizado
	@Override
	public void onTaskFinished(boolean comprobado) {
		if (comprobado) {
			escribirFichero();

			startApp();
		} else {
			// Si no está conectado, informa de ello
			if (!isOnline()) {
				Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
			} else {
				// Si no se ha verificado, informa de ello y vuelve a pedir el
				// teléfono
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

	public class JSONTask extends AsyncTask<String,String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			HttpURLConnection connection = null;
			BufferedReader reader = null;

			try {
				URL url = new URL(params[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				InputStream stream = connection.getInputStream();
				reader = new BufferedReader(new InputStreamReader(stream));
				StringBuffer buffer = new StringBuffer();
				String line ="";
				while ((line = reader.readLine()) != null){
					buffer.append(line);
				}

				String finalJson = buffer.toString();

				JSONObject parentObject = new JSONObject(finalJson);
				String TlfInicio = parentObject.getString("telefono_inicio");

				return TlfInicio;

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				if(connection != null) {
					connection.disconnect();
				}
				try {
					if(reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return  null;
		}

		@Override
		protected void onPostExecute(final String result) {
			super.onPostExecute(result);
			if(result != null) {
				num_inicio=result;
				// Intentamos leer el fichero.
				leerFichero();

				if (extras!=null){
					cambio=cambio+extras.getString("Cambio");
					if (cambio.equals("numero nuevo")) {
						// Pedimos el teléfono, que posteriormente comprobaremos con la base
						// de datos
						pedirTelefono();
					}
					else{
						if (tel.equals("")) {
							// Pedimos el teléfono, que posteriormente comprobaremos con la base
							// de datos
							pedirTelefono();
						}

						// Si el número ya existe en el fichero
						if (!tel.equals("")) {
							// Iniciaremos la SplashScreen
							startApp();
						}
					}
				}
				else {

					// En caso de no existir el fichero:
					// Comprobaremos el número en la base de datos y, en caso de ser
					// correcto, lo guardamos en el fichero e iniciamos la SplashScreen

					if (tel.equals("")) {
						// Pedimos el teléfono, que posteriormente comprobaremos con la base
						// de datos
						pedirTelefono();
					}

					// Si el número ya existe en el fichero
					if (!tel.equals("")) {
						// Iniciaremos la SplashScreen
						startApp();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
			}
		}
	}

}