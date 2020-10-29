package diegoreyesmo.imprimeboleta;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.logging.Logger;

import diegoreyesmo.imprimeboleta.dto.DocumentoRequestDTO;
import diegoreyesmo.imprimeboleta.dto.LoginRequestDTO;
import diegoreyesmo.imprimeboleta.ui.Botones;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    Logger logger = Logger.getLogger("MainActivity");
    private Botones botones;
    private Button current;
    private Button parcial;
    private Button total;
    private Button number;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button00;

    private Button buttonClear;
    private Button buttonDelete;
    private Button buttonMore;
    private Button buttonMulti;

    private Button buttonImprimir;

    private boolean flagNumber = true;

    private ClienteWs clienteWs;
    private DocumentoRequestDTO documentoRequestDTO;
    private LoginRequestDTO loginRequestDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.info("[onCreate] inicio");
        setContentView(R.layout.activity_main);
        inicializarComponentes();
        agregarListener();
        clienteWs = new ClienteWs(this);
        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setIdPos(getString(R.string.id_pos));
        loginRequestDTO.setUsername(getString(R.string.username));
        loginRequestDTO.setPassword(getString(R.string.password));
        clienteWs.login(loginRequestDTO);
        documentoRequestDTO = new DocumentoRequestDTO();
        documentoRequestDTO.setIdPos(getString(R.string.id_pos));
        documentoRequestDTO.setUsername(getString(R.string.username));
        documentoRequestDTO.setPassword(getString(R.string.password));
        documentoRequestDTO.setTipodocumento(getString(R.string.tipo_documento));
        current = parcial;
        logger.info("[onCreate] fin");
    }

    private void agregarListener() {
        button0.setOnTouchListener(this);
        button1.setOnTouchListener(this);
        button2.setOnTouchListener(this);
        button3.setOnTouchListener(this);
        button4.setOnTouchListener(this);
        button5.setOnTouchListener(this);
        button6.setOnTouchListener(this);
        button7.setOnTouchListener(this);
        button8.setOnTouchListener(this);
        button9.setOnTouchListener(this);
        button00.setOnTouchListener(this);
        buttonClear.setOnTouchListener(this);
        buttonDelete.setOnTouchListener(this);
        buttonMore.setOnTouchListener(this);
        buttonMulti.setOnTouchListener(this);
        number.setOnTouchListener(this);
        total.setOnTouchListener(this);
        parcial.setOnTouchListener(this);
        buttonImprimir.setOnTouchListener(this);
    }

    private void inicializarComponentes() {
        total = (Button) findViewById(R.id.total);
        parcial = (Button) findViewById(R.id.parcial);
        number = (Button) findViewById(R.id.number);
        buttonImprimir = (Button) findViewById(R.id.imprimir);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        button00 = (Button) findViewById(R.id.button00);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonMore = (Button) findViewById(R.id.buttonMore);
        buttonMulti = (Button) findViewById(R.id.buttonMulti);
        botones = new Botones();
        ArrayList<Button> numeros = new ArrayList<>();
        numeros.add(button0);
        numeros.add(button00);
        numeros.add(button1);
        numeros.add(button2);
        numeros.add(button3);
        numeros.add(button4);
        numeros.add(button5);
        numeros.add(button6);
        numeros.add(button7);
        numeros.add(button8);
        numeros.add(button9);
        botones.setNumeros(numeros);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        logger.info("[onTouch]");
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            // concatenar numero en textview current
            botones.getNumeros().stream().forEach(button -> {
                if (button.equals((Button) view)) {
                    logger.info("boton: " + button.getText());
                    if (flagNumber && current.equals(number)) {
                        current.setText(button.getText().toString());
                        flagNumber = false;
                    } else {
                        String newValue = current.getText().toString().concat(button.getText().toString());
                        if (newValue.length() < 10) {
                            current.setText(newValue);
                        } else {
                            Toast.makeText(this, R.string.excede_maximo, Toast.LENGTH_LONG);
                        }
                    }
                }
            });
            // limpiar textview current
            if (buttonClear.equals((Button) view)) {
                logger.info("boton: clear");
                total.setText("");
            }
            // borra el último caracter
            if (buttonDelete.equals((Button) view)) {
                logger.info("boton: delete");
                String value = current.getText().toString();
                if (!value.isEmpty()) {
                    value = value.substring(0, value.length() - 1);
                    current.setText(value);
                }
            }
            // agregar: multiplica parcial por cantidad y lo agrega al total
            if (buttonMore.equals((Button) view)) {
                logger.info("boton: more");
                int parcialValue = Util.parseInt(parcial.getText().toString());
                int numberValue = Util.parseInt(number.getText().toString());
                int totalValue = Util.parseInt(total.getText().toString());
                totalValue = totalValue + (numberValue * parcialValue);
                total.setText(Util.formatInt(totalValue));
                number.setText("1");
                parcial.setText("");
                current = parcial;
                flagNumber = true;
            }
            // multiplicar: alterna focus entre number y parcial
            if (buttonMulti.equals((Button) view)) {
                if (current.equals(number)) current = parcial;
                else {
                    current = number;
                }
            }

            if (buttonImprimir.equals((Button) view)) {
                int monto = Util.parseInt(total.getText().toString());
                if (monto > 0) {
                    documentoRequestDTO.setMonto(String.valueOf(monto));
                    clienteWs.documento(documentoRequestDTO);
                }

            }

        }
        return false;
    }
}