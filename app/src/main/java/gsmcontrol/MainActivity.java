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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.gsmcontrol.R;

//Implementamos LoadingTaskFinishedListener para hacer la consulta al historial (si procede).
//Nos obliga a implementar también el LoadingTaskFinishedListener de ConsultaBBDD cuando intentamos
//acceder a él si decide refrescar el usuario
public class MainActivity extends Activity implements LoadingTaskFinishedListener {

	String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Si no está conectado, informa de ello
		if (!isOnline()) {
			Toast.makeText(
					this, R.string.no_internet,Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, R.string.data_ok, Toast.LENGTH_LONG).show();
		}

		// Cambiamos el título de la barra por el de la aplicación
		this.getActionBar().setTitle(R.string.app_name);

		// Mostramos el botón en la barra de la aplicación
		this.getActionBar().setDisplayHomeAsUpEnabled(true);

		// Recuperamos el número de teléfono
		tel = this.getIntent().getStringExtra("telefono");

		// Accedemos al TextView y le asignamos un texto
		TextView tv_texto = (TextView) findViewById(R.id.tv_mostrar);
		tv_texto.setText(R.string.resources);

		// TestView que mostrará el número
		TextView tv_numeroTextView = (TextView) findViewById(R.id.tv_numero);
		tv_numeroTextView.setText(tel);

		// Recuperamos el ArrayList generado en la tarea asíncrona CosultaBBDD
		ArrayList<Recurso> recursos = (ArrayList<Recurso>) this.getIntent().getSerializableExtra("recursos");

		if (recursos.isEmpty()) {
			tv_texto.setText(R.string.no_resource);
		}
		else {

			// Accedemos al ListView y le asignamos el adaptador personalizado con nuestra distribución
			ListView lv_lista = (ListView) findViewById(R.id.lv_recursos);
			AdapterPersonalizado adaptador = new AdapterPersonalizado(this,
					android.R.layout.simple_list_item_1, recursos);
			lv_lista.setAdapter(adaptador);
			lv_lista.setOnItemClickListener(new OnItemClickListener() {

				// Cuando se seleciona un elemento del ListView, aparecer� un
				// di�logo con la opci�n de llamar al recurso seleccionado
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					// Accedemos al recurso seleccionado
					ListView lv_lista = (ListView) findViewById(R.id.lv_recursos);
					Recurso recurso = (Recurso) lv_lista.getItemAtPosition(position);
					// Llamamos al método que mostrará un diálogo en función del
					// recurso seleccionado y pasado como parámetro
					mostrarDialog(recurso);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle(R.string.app_exit);
				builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
				builder.create().show();
				break;
			case R.id.cambio_numero:
				//Abrimos la Activity inicial, en la que se preguntará de nuevo
				// el teléfono. Enviamos una bandera para que la Activity borre
				// el fichero donde está guardado el teléfono anterior y escriba uno nuevo
				Intent number = new Intent(MainActivity.this, ActivityComprobaciones.class);
				number.putExtra("Cambio","nuevo");
				startActivity(number);
				break;
			case R.id.admin:
				// Abrimos la Activity que contiene el WebBrowser hacia la
				// administración de GSMControl. Le pasamos el teléfono para
				// las opciones del menú lateral del Web Browser integrado
				Intent web = new Intent(MainActivity.this, WebActivity.class);
				web.putExtra("telefono", tel);
				startActivity(web);
				break;
			case R.id.info:
				// Abrimos la Activity que contiene la información de la
				// aplicación. Le pasamos el teléfono para el menú lateral
				// de dicha Activity
				Intent info = new Intent(MainActivity.this, Informacion.class);
				info.putExtra("telefono", tel);
				startActivity(info);
				break;
			case R.id.history:
				// Se abriría un historial con los accesos a los recursos
				// El historial de acceso está en la BBDD. Mostrar últimos 15 accesos.
				//De esto se encargará el Activity PantallaCarga.
				Intent carga = new Intent(MainActivity.this, PantallaCarga.class);
				carga.putExtra("telefono", tel);
				startActivity(carga);
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

	// Método que muestra un diálogo con la opción de llamar al recurso pasado
	// como parámetro. El recurso tiene que hacerse final para pueda ser
	// accedido por el OnClick del diálogo
	public void mostrarDialog(final Recurso recurso) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.make_call);
		String msg = "¿Activar " + recurso.getNombre() + "?";
		alert.setMessage(msg);

		alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				try {
					startActivity(new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + recurso.getTelefono())));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.

			}
		});

		alert.show();
	}

	// Función a la que se llama desde la tarea asíncrona para indicar que ha finalizado
	@Override
	public void onTaskFinished(ArrayList<Recurso> listado) {
		startApp(listado);
	}

	// Con esta función, se da paso a la Activity del Historial
	private void startApp(ArrayList<Recurso> listado) {
		Intent intent = new Intent(MainActivity.this, Historial.class);
		intent.putExtra("telefono", tel);
		intent.putExtra("historial", listado);
		startActivity(intent);
	}
}
