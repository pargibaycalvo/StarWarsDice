package com.example.pargibaycalvo.simondice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    int MAX_VOLUME = 100; //volumen máximo de referencia
    int soundVolume = 100; //volumen que queremos poner
    float volume = (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME)));

    int Puntuación = 20;//puntuacion por pulsacion

    int lvl = 0;
    Button botonescolor[];
    Integer partidas[]=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //musica de fondo para la app
        final MediaPlayer musicafondo = MediaPlayer.create(this, R.raw.imperial);
        musicafondo.setLooping(true);
        musicafondo.setVolume(volume, volume);
        new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                musicafondo.start();
            }
        }, 1000);

        //botones funcionales
        botonescolor = new Button[]{
                (Button)findViewById(R.id.btnred),
                (Button)findViewById(R.id.btngreen),
                (Button)findViewById(R.id.btnblue),
                (Button)findViewById(R.id.btnyellow)
        };

        //texto score
        TextView score = (TextView)findViewById(R.id.textView);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View view) {
        // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //           .setAction("Action", null).show();
        // }
        //});
    }
        //partida lanzada
        protected void partida(){
            lvl+=1;
            int time=1000;

            partidas= new Integer[lvl];

            for(int p=0; p<lvl; p++){
                final int i = (int)(Math.random()*4);
                partidas[p]=i;

                Handler handler = new Handler();
                final Handler handler1 = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        botonescolor[i].setPressed(true);
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                botonescolor[i].setPressed(false);
                            }
                        },500);
                    }
                }, time*i+500);
            }
            for(int e=0; partidas.length>e; e++){
                System.out.println(partidas[e]);
            }

        }


        //musica al presionar los botones de los colores
        public void buttonSoundgreen(View view) {
            MediaPlayer mpG = MediaPlayer.create(this, R.raw.blaster);
            mpG.start();

        }
        public void buttonSoundblue(View view){
            MediaPlayer mpB = MediaPlayer.create(this, R.raw.blaster);
            mpB.start();

        }
        public void buttonSoundyellow(View view){
            MediaPlayer mpY = MediaPlayer.create(this, R.raw.blaster);
            mpY.start();

        }
        //pulsado el boton de start salta un mensaje con sonido
        public void startgame(View view){
            Toast toast1 = Toast.makeText(getApplicationContext(),"Que la fuerza te acompañe", Toast.LENGTH_SHORT);
            toast1.show();
            MediaPlayer mp = MediaPlayer.create(this, R.raw.swvader02);
            mp.start();

            //boton funcional start
            final Button play = (Button) findViewById(R.id.btnstart);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    partida();
                    play.setCursorVisible(false);
                }
            });

        }
        //pulsado en boton de reset salta un mensaje con sonido
        public void resetgame(View view){
            Toast toast1 = Toast.makeText(getApplicationContext(),"Reiniciando Partida", Toast.LENGTH_SHORT);
            toast1.show();
            MediaPlayer mp = MediaPlayer.create(this, R.raw.off);
            mp.start();

            //boton reset funcional
            final Button reset = (Button) findViewById(R.id.btnrestart);
            reset.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    partida();
                    reset.setCursorVisible(true);
                }
            });
        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
