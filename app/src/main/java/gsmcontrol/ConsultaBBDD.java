package gsmcontrol;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

public class ConsultaBBDD extends AsyncTask<String, Integer, Integer> {

	public interface LoadingTaskFinishedListener {
		void onTaskFinished(ArrayList<Recurso> recursos);
	}

	// Listener que indicará cuándo acaba la tarea
	private LoadingTaskFinishedListener finishedListener;

	// Instanciamos variables e inicializamos el ArrayList por primera vez
	private String tel;
	private ArrayList<Recurso> recursos = new ArrayList<Recurso>();
	private JSONArray ja;
	private String data;

	public ConsultaBBDD(String tel, LoadingTaskFinishedListener finishedListener) {
		this.tel = tel;
		this.finishedListener = finishedListener;
	}

	@Override
	protected Integer doInBackground(String... params) {
		if (resourcesDontAlreadyExist()) {
			downloadResources();
		}

		return 0;
	}

	private boolean resourcesDontAlreadyExist() {

		return true; // Devolvemos true para mostrar el Splash en de forma permanente (hasta que acabe)
	}

	private void downloadResources() {

		// Accedemos al Web Service y guardamos el resultado en un JSONArray
		ja = null;

		// Pasamos a la función como parámetro la ruta de acceso al WebService
		data = httpGetData("http://gsmcontrol.es/testdrive/webservices/consulta.php?tel=" + tel);
		if (data.length() > 0) {
			try {
				ja = new JSONArray(data);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Recorremos el array devuelto y vamos creando y añadiendo los recursos al ArrayList de recursos.
			// Cogemos el JSONArray en la posición iésima porque el JSONArray
			// obtenido contiene a su vez un JSONArray en cada una de sus posiciones
			for (int i = 0; i < ja.length(); i++) {
				try {
					// Accedemos al JSONArray de la posición iésima del obtenido anteriormente
					JSONArray ja2 = ja.getJSONArray(i);
					// Creamos el recurso a partir del JSONArray de la posición iésima
					Recurso recurso = new Recurso(ja2.getString(0), ja2.getString(1));
					// Añadimos el nuevo recurso al ArrayList de recursos
					recursos.add(recurso);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		// Mostramos la SplashScreen durante dos segundos, tiempo más que
		// suficiente para acceder a la base de datos
		int count = 2;
		for (int i = 0; i < count; i++) {
			// Dormimos un segundo cada vez para mostrar la SplashScreen ese tiempo
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ignore) {}
		}
	}

	// Función que devuelve la respuesta del Web Service
	private String httpGetData(String rutaAcceso) {

		String response = "";
		rutaAcceso = rutaAcceso.replace(" ", "%20");
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(rutaAcceso);

		ResponseHandler<String> responsehandler = new BasicResponseHandler();
		try {
			response = httpclient.execute(httpget, responsehandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		// Decimos al listener que hemos terminado y le pasamos el ArrayList de recursos del usuario
		finishedListener.onTaskFinished(recursos);
	}
}
