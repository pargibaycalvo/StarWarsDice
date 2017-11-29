package com.example.pargibaycalvo.simondice;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    int MAX_VOLUME = 100; //volumen máximo de referencia
    int soundVolume = 90; //volumen que queremos poner
    float volume = (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME)));
    Button botonescolor[];
    Button play;
    Button reset;
    TextView lvl;
    Button btnred, btnblue, btngreen, btnyellow;
    MediaPlayer musicafondo;

    public static final int INTERVALO = 2000; //2 segundos para salir
    public long tiempoPrimerClick;

    ArrayList<Integer> game = new ArrayList();
    ArrayList<Integer> answers = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Desactivamos la opcion de rotacion
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //musica de fondo para la app
        musicafondo = MediaPlayer.create(this, R.raw.imperial);
        musicafondo.setLooping(true);
        musicafondo.setVolume(volume, volume);
        new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                musicafondo.start();
            }
        }, 1000);

        //boton start y reset funcional desde xml
        play = (Button)findViewById(R.id.btnstart);
        reset = (Button)findViewById(R.id.btnrestart);
        reset.setEnabled(false);

        //puntuacion funcional desde xml
        lvl = (TextView)findViewById(R.id.textView);

        //botones funcionales desde xml
        botonescolor = new Button[]{
                (Button)findViewById(R.id.btnred),
                (Button)findViewById(R.id.btngreen),
                (Button)findViewById(R.id.btnblue),
                (Button)findViewById(R.id.btnyellow)
        };

        //musica y texto boton start y reset
        final Toast toast1 = Toast.makeText(getApplicationContext(),"Que la fuerza te acompañe", Toast.LENGTH_SHORT);
        final MediaPlayer mp1 = MediaPlayer.create(this, R.raw.swvader02);
        final Toast toast2 = Toast.makeText(getApplicationContext(),"Siguiente Jugada", Toast.LENGTH_SHORT);
        final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.on);

        //pulsar boton start y inicar el juego
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast1.show();
                mp1.start();
                generategame();
                disabledButtons();
            }
        });

        //boton reset funcional
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast2.show();
                mp2.start();
                generategame();
                disabledButtons();
            }
        });

        //musica para los botones
        final MediaPlayer mpG = MediaPlayer.create(MainActivity.this, R.raw.blaster);

        //comprobar pulsaciones de los botones y sonido al pulsar
        botonescolor[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpG.start();
                check(0);
            }
        });
        botonescolor[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpG.start();
                check(1);
            }
        });
        botonescolor[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpG.start();
                check(2);
            }
        });
        botonescolor[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpG.start();
                check(3);
            }
        });

    }

        //generar partida con activacion de botones y desactivacion (cuando inicie la secuencia de colores estarán
        //desactivados hasta que finalize
        public void generategame(){
            reset.setText("Next Lvl");
            reset.setEnabled(true);
            play.setVisibility(View.INVISIBLE);
            reset.setVisibility(View.INVISIBLE);
            int time=1000;

            final int color = (int)(Math.random()*4);
            game.add(color);

            for(int g=0; g<game.size(); g++){
                Handler hand1 = new Handler();
                final Handler hand2 = new Handler();

                final int end = g;
                hand1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        botonescolor[game.get(end)].setPressed(true);
                        disabledButtons();
                        hand2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                botonescolor[game.get(end)].setPressed(false);
                                enabledButtons();
                            }
                        },500);
                    }
                },time*g+1000);
            }
            disabledButtons();
            lvl.setText(String.valueOf(game.size()));
        }

        //comprobaciones colores, sonido al perder y mensaje
        public void check(int colorCheck){
            final MediaPlayer looser = MediaPlayer.create(this, R.raw.jabba);
            final MediaPlayer winner = MediaPlayer.create(this, R.raw.r2d2yeah);
            final Toast txtlooser = Toast.makeText(getApplicationContext(),"YOU ARE LOOSER", Toast.LENGTH_SHORT);

            if(colorCheck==0){
                answers.add(0);
            }
            else if(colorCheck==1){
                answers.add(1);
            }
            else if(colorCheck==2){
                 answers.add(2);
            }
            else{
                answers.add(3);
            }

            if(game.size()==answers.size()){
                for(int a=0; a<game.size(); a++){
                    if(game.get(a).equals(answers.get(a))){
                    }
                    else{
                        looser.start();
                        txtlooser.show();
                        winner.stop();
                        answers.clear();
                        game.clear();
                        lvl.setText("0");
                        play.setVisibility(View.VISIBLE);
                        reset.setEnabled(false);
                    }
                }
                answers.clear();
                winner.start();
                reset.setVisibility(View.VISIBLE);
            }

        }

        //confirmacion al salir de la app con sonido
        @Override
        public void onBackPressed(){
            final MediaPlayer exit = MediaPlayer.create(this, R.raw.off);
            if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
                super.onBackPressed();
                return;
            }else{
                Toast.makeText(this, "Volveremos a vernos joven Jedi", Toast.LENGTH_SHORT).show();
                musicafondo.stop();
                exit.start();
            }
            tiempoPrimerClick = System.currentTimeMillis();
        }

        //descactivar botones al iniciar el juego
        public void disabledButtons(){

            btnred = (Button)findViewById(R.id.btnred);
            btnred.setEnabled(false);
            btnblue = (Button)findViewById(R.id.btnblue);
            btnblue.setEnabled(false);
            btngreen = (Button)findViewById(R.id.btngreen);
            btngreen.setEnabled(false);
            btnyellow = (Button)findViewById(R.id.btnyellow);
            btnyellow.setEnabled(false);

        }

        //activar botones una vez inicie el juego
        public void enabledButtons(){

            btnred = (Button)findViewById(R.id.btnred);
            btnred.setEnabled(true);
            btnblue = (Button)findViewById(R.id.btnblue);
            btnblue.setEnabled(true);
            btngreen = (Button)findViewById(R.id.btngreen);
            btngreen.setEnabled(true);
            btnyellow = (Button)findViewById(R.id.btnyellow);
            btnyellow.setEnabled(true);

        }

    //configuraciones secundarias (no activadas)
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
