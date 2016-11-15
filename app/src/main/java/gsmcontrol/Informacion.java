package gsmcontrol;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import app.gsmcontrol.R;

public class Informacion extends Activity {

	// Menú lateral
	DrawerLayout drawerLayout;
	String[] opciones;
	ArrayList<ItemMenu> opcionesMenu = new ArrayList<ItemMenu>();
	ListView listView;

	String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informacion);

		// Recuperamos el teléfono pasado.
		tel = this.getIntent().getStringExtra("telefono");

		// Recuperamos las opciones del menú lateral
		opciones = this.getResources().getStringArray(R.array.opciones);

		// Menú lateral
		listView = (ListView) findViewById(R.id.list_view4);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout4);

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
					Intent web = new Intent(Informacion.this, WebActivity.class);
					web.putExtra("telefono", tel);
					startActivity(web);
					finish();
					break;

				case 2:
					// Se abriría un historial con los accesos a los recursos
					// El historial de acceso está en la BBDD. Mostrar últimos 15 accesos.
					// De esto se encargará el Activity PantallaCarga.
					Intent carga = new Intent(Informacion.this, PantallaCarga.class);
					carga.putExtra("telefono", tel);
					startActivity(carga);
					finish();
					break;

				case 3:
					// No hacemos nada en esta opción desde aquí
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

		// Accedemos a los Textiew y les asignamos el texto
		TextView info = (TextView) findViewById(R.id.tv_info);
		info.setTextSize(12);
		info.setText(R.string.info_text);
		TextView link = (TextView) findViewById(R.id.tv_link);
		link.setTextSize(12);
		link.setTextColor(Color.BLUE);
		link.setText(R.string.info_link);

		link.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Accederemos al link al pulsarlo
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://www.gsmcontrol.es"));
				startActivity(intent);
			}
		});
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
				// TODO: Animación de icono de menú
				// http://stackoverflow.com/questions/26230417/animation-actionbar-material-design
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
