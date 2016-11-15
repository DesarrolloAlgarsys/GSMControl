package gsmcontrol;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import app.gsmcontrol.R;


//Implementamos LoadingTaskFinishedListener para hacer la consulta al historial (si procede)
public class WebActivity extends Activity {

	// Navegador web integrado
	WebView browser;

	// Menú lateral
	DrawerLayout drawerLayout;
	String[] opciones;
	ArrayList<ItemMenu> opcionesMenu = new ArrayList<ItemMenu>();
	ListView listView;

	String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		// Recuperamos el teléfono pasado.
		tel = this.getIntent().getStringExtra("telefono");

		// Recuperamos las opciones del menú lateral
		opciones = this.getResources().getStringArray(R.array.opciones);

		// Menú lateral
		listView = (ListView) findViewById(R.id.list_view3);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout3);

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
					// Se vuelve a la parte inicial de la aplicación, terminando
					// esta activity. Además, borramos la caché y el
					// historial del browser para no llenar el móvil de datos.
					// Asimismo, borramos las Cookies para evitar que se abra el
					// WebActivity logeado al acceder de nuevo a él
					browser.clearHistory();
					browser.clearFormData();
					browser.clearCache(true);
					CookieSyncManager.createInstance(WebActivity.this);
					CookieManager cookieManager = CookieManager.getInstance();
					cookieManager.removeAllCookie();
					finish();
					break;

				case 1:
					// No hacemos nada en esta opción desde aquí
					break;

				case 2:
					// Se abriría un historial con los accesos a los recursos
					// El historial de acceso está en la BBDD. Mostrar últimos 15 accesos.
					// De esto se encargará el Activity PantallaCarga.
					Intent carga = new Intent(WebActivity.this, PantallaCarga.class);
					carga.putExtra("telefono", tel);
					startActivity(carga);
					break;

				case 3:
					// Abrimos la Activity que contiene la información de la
					// aplicación y finalizamos esta Activity para evitar que
					// quede en segundo plano eternamente. Le pasamos el
					// teléfono para el menú lateral de dicha Activity
					Intent info = new Intent(WebActivity.this, Informacion.class);
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

		browser = (WebView) findViewById(R.id.web);
		browser.setWebViewClient(new WebViewClient() {
			// Evitamos con este m�todo que el enlace se abra fuera de nuestra
			// app en el navegador de android
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});

		WebSettings webSettings = browser.getSettings();
		webSettings.setJavaScriptEnabled(true);

		browser.loadUrl("http://gsmcontrol.es/testdrive/");

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

	// Controlaremos que, si da al botón atrás (y no hay historial para ir
	// atrás), se acabe esta Activity y que no se quede en segundo plano
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && !browser.canGoBack()) {
			// Se vuelve a la parte inicial de la aplicación, terminando
			// este activity. Además, borramos la caché y el
			// historial del browser para no llenar el móvil de datos.
			// Asimismo, borramos las Cookies para evitar que se abra el
			// WebActivity logeado al acceder de nuevo a él
			browser.clearHistory();
			browser.clearFormData();
			browser.clearCache(true);
			CookieSyncManager.createInstance(this);
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.removeAllCookie();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
