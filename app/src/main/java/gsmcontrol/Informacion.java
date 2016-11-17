package gsmcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import app.gsmcontrol.R;

public class Informacion extends Activity {

	String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informacion);

		// Recuperamos el teléfono pasado.
		tel = this.getIntent().getStringExtra("telefono");

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.informacion, menu);
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
				AlertDialog.Builder NumberBuilder = new AlertDialog.Builder(Informacion.this);
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
						Intent number = new Intent(Informacion.this, ActivityComprobaciones.class);
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
				Intent web = new Intent(Informacion.this, WebActivity.class);
				web.putExtra("telefono", tel);
				startActivity(web);
				finish();
				break;
			case R.id.history:
				// Se abriría un historial con los accesos a los recursos
				// El historial de acceso está en la BBDD. Mostrar últimos 15 accesos.
				// De esto se encargará el Activity PantallaCarga.
				Intent carga = new Intent(Informacion.this, PantallaCarga.class);
				carga.putExtra("telefono", tel);
				startActivity(carga);
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
