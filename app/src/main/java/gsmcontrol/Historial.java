package gsmcontrol;

import gsmcontrol.ConsultaHistorial.LoadingTaskFinishedListener;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import app.gsmcontrol.R;

public class Historial extends Activity implements LoadingTaskFinishedListener {

	DrawerLayout drawerLayout;
	String[] opciones;
	ArrayList<ItemMenu> opcionesMenu = new ArrayList<ItemMenu>();
	ListView listView;
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
		// Recuperamos las opciones del menú lateral
		opciones = this.getResources().getStringArray(R.array.opciones);

		// Menú lateral
		listView = (ListView) findViewById(R.id.list_view2);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);

		// Opciones del menú
		opcionesMenu.add(new ItemMenu(opciones[0], R.drawable.movil));
		opcionesMenu.add(new ItemMenu(opciones[1], R.drawable.admin));
		opcionesMenu.add(new ItemMenu(opciones[2], R.drawable.historial));
		opcionesMenu.add(new ItemMenu(opciones[3], R.drawable.info));

		AdapterMenuLateral adaptadorMenu = new AdapterMenuLateral(this,
				android.R.layout.simple_list_item_1, opcionesMenu);

		listView.setAdapter(adaptadorMenu);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					// Se vuelve a la parte inicial de la aplicación, terminando este activity
					finish();
					break;

				case 1:
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

				case 2:
					// No hacemos nada en esta opción desde el Historial
					break;

				case 3:
					// Abrimos la Activity que contiene la información de la
					// aplicación y finalizamos esta Activity para evitar que
					// quede en segundo plano eternamente. Le pasamos el
					// teléfono para el menú lateral de dicha Activity
					Intent info = new Intent(Historial.this, Informacion.class);
					info.putExtra("telefono", tel);
					startActivity(info);
					finish();
					break;

				default:
					break;
				}

				drawerLayout.closeDrawers();
			}
		});

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

	// Menú lateral
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (drawerLayout.isDrawerOpen(listView)) {
				drawerLayout.closeDrawers();
			} else {
				drawerLayout.openDrawer(listView);
			}
			return true;
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
