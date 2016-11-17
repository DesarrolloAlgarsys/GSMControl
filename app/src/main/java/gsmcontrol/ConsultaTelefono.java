package gsmcontrol;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class ConsultaTelefono extends AsyncTask<String, Integer, Integer> {

	public interface LoadingTaskFinishedListener {
		void onTaskFinished(boolean comprobado);
	}

	// Listener que indicará cuándo acaba la tarea
	private LoadingTaskFinishedListener finishedListener;

	// Instanciamos variables
	private String tel;
	private JSONObject jo;
	private String data;
	// Inicializamos la bandera a false y la cambiaremos si la comprobación es correcta
	private boolean comprobado = false;

	public ConsultaTelefono(String tel, LoadingTaskFinishedListener finishedListener) {
		this.tel = tel;
		this.finishedListener = finishedListener;
	}

	@Override
	protected Integer doInBackground(String... params) {
		comprobar();
		return 0;
	}

	private void comprobar() {

		// Accedemos al Web Service y guardamos el resultado en un JSONArray
		jo = null;

		// Pasamos a la función como parámetro la ruta de acceso al WebService
		data = httpGetData("http://janus.algarsys.com/llamada/gsm_ws.php?phone=" + tel);

		try {
			jo = new JSONObject(data);
			// Si no devuelve un 0, entonces cambiamos la bandera a true
			if (!jo.toString().contains("0")) {
				comprobado = true;
			}
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Realizamos la consulta durante dos segundos
		int count = 2;
		for (int i = 0; i < count; i++) {
			// Dormimos un segundo cada vez
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException ignore) {}
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
		// Decimos al listener que hemos terminado y le pasamos la bandera
		finishedListener.onTaskFinished(comprobado);
	}
}
