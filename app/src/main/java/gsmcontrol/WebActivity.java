package gsmcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import app.gsmcontrol.R;

//Implementamos LoadingTaskFinishedListener para hacer la consulta al historial (si procede)
public class WebActivity extends Activity {

	// Navegador web integrado
	WebView browser;
	String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		// Recuperamos el teléfono pasado.
		tel = this.getIntent().getStringExtra("telefono");

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
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
			case R.id.cambio_numero:
				//Abrimos la Activity inicial, en la que se preguntará de nuevo
				// el teléfono. Enviamos una bandera para que la Activity borre
				// el fichero donde está guardado el teléfono anterior y escriba uno nuevo
				AlertDialog.Builder NumberBuilder = new AlertDialog.Builder(WebActivity.this);
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
						Intent number = new Intent(WebActivity.this, ActivityComprobaciones.class);
						number.putExtra("Cambio","nuevo");
						startActivity(number);
					}
				});
				NumberBuilder.create().show();
				break;
			case R.id.info:
				// Abrimos la Activity que contiene la información de la
				// aplicación y finalizamos esta Activity para evitar que
				// quede en segundo plano eternamente. Le pasamos el
				// teléfono para el menú de dicha Activity
				Intent info = new Intent(WebActivity.this, Informacion.class);
				info.putExtra("telefono", tel);
				startActivity(info);
				finish();
				break;
			case R.id.history:
				// Se abriría un historial con los accesos a los recursos
				// El historial de acceso está en la BBDD. Mostrar últimos 15 accesos.
				// De esto se encargará el Activity PantallaCarga.
				Intent carga = new Intent(WebActivity.this, PantallaCarga.class);
				carga.putExtra("telefono", tel);
				startActivity(carga);
				break;
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
