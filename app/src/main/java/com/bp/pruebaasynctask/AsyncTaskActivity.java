package com.bp.pruebaasynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AsyncTaskActivity extends AppCompatActivity {

    private Button launchAsync;
    private ProgressBar progressBar;
    private AsyncTaskClass downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.async_task);

        final String uri = "";

        launchAsync = (Button) findViewById(R.id.launch_async);
        launchAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Instancia de la clase AsyncTaskClass que extiende de AsyncTask.
                downloadTask = new AsyncTaskClass();
                //Para ejecutar la AsyncTask se emplea el método execute().
                downloadTask.execute(uri);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }


    /* Clase que extiende de AsyncTask y que contiene los métodos necesarios para
    * realizar la tarea en segundo plano. */
    public class AsyncTaskClass extends AsyncTask<String, Integer, String> {

        /* Actualiza la interfaz de la aplicación antes de ejecutar la tarea en
        * segundo plano. */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            launchAsync.setVisibility(View.GONE); //Oculta el botón.
            progressBar.setVisibility(View.VISIBLE); //Muestra la barra de progreso.
        }

        /* Realiza la tarea pesada en un thread a parte del UIThread. Recibe como parámetro una
         * tabla sin acotar de cadenas de caracteres. */
        @Override
        protected String doInBackground(String... params) {
            String uri = params[0];
            String result = "";
            /* Tarea a realizar en segundo plano. En este caso un bucle dónde también
            * se para el thread. Simula una tarea pesada. */
            for (int i = 1; i <= 10; ++i) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /* Con cada iteracción se actualiza la progressBar. La llamada a
                * publishProgress fuerza la ejecución de onProgressUpdate. */
                publishProgress(i * 10);
                result += i;
            }
            return result; //Se devuelve el resultado a onPostExecute.
        }

        /* Permite actualizar el progreso de la tarea en ejecución. En este caso
        * con un progressBar. */
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressBar.setProgress(progress[0]);
        }

        /* Actualiza la interfaz con el resultado final de la ejecución de
        * doInBackground... */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //... en este caso muestra un toast...
            Toast.makeText(AsyncTaskActivity.this, "Fin de la ejecución del procesamiento como tarea de fondo", Toast.LENGTH_LONG).show();
            //... hace visible el botón de nuevo...
            launchAsync.setVisibility(View.VISIBLE);
            //... y oculta la barra de progreso.
            progressBar.setVisibility(View.GONE);
        }
    }
}
