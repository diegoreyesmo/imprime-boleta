package diegoreyesmo.imprimeboleta;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import diegoreyesmo.imprimeboleta.dto.DocumentoRequestDTO;
import diegoreyesmo.imprimeboleta.dto.DocumentoResponseDTO;
import diegoreyesmo.imprimeboleta.dto.LoginRequestDTO;
import diegoreyesmo.imprimeboleta.dto.LoginResponseDTO;


public class ClienteWs {

    private String TAG = "TAG-ClienteWs";
    private RequestQueue queueLogin;
    private RequestQueue queueValidaSesion;
    private AppCompatActivity context;
    private int timeout = 10000; // millis
    private String urlBase;
    private String loginPath;
    private String documentPath;
//private ObjectMapper objectMapper;

    public ClienteWs(AppCompatActivity context) {
        this.context = context;
//        objectMapper  = new ObjectMapper();
        queueLogin = Volley.newRequestQueue(context);
        queueValidaSesion = Volley.newRequestQueue(context);
        urlBase = context.getString(R.string.url_base);
        loginPath = context.getString(R.string.login_path);
        documentPath = context.getString(R.string.document_path);
    }

    public void login(LoginRequestDTO loginRequestDTO) {
        Log.d(TAG, "Inicio llamada WS login ingresar...");
        try {
            JSONObject postparams = new JSONObject();
            postparams.put("username", loginRequestDTO.getUsername());
            postparams.put("password", loginRequestDTO.getPassword());
            postparams.put("idPos", loginRequestDTO.getIdPos());
            JsonObjectRequest jsonObjReq = generarRequestLogin(postparams);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queueLogin.add(jsonObjReq);
            Log.d(TAG, "Fin llamada WS login ingresar.");
        } catch (JSONException e) {
            Log.d(TAG, "Fin llamada WS login ingresar.");
        }
    }

    private JsonObjectRequest generarRequestLogin(JSONObject postparams) {
        Log.d(TAG, "generarRequestLogin: " + urlBase + loginPath);
        return new JsonObjectRequest(Request.Method.POST,
                urlBase + loginPath, postparams,
                response -> {
                    if (response != null) {
                        Log.d(TAG, loginPath + "[response] " + (response.toString() == null ? "null" : response.toString()));
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        LoginResponseDTO loginResponse = gson.fromJson(response.toString(), LoginResponseDTO.class);
                    }
                    // TODO cerrar login intent
                },
                error -> {
                    error.printStackTrace();
                }) {

            @Override
            public Map getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                Log.d(TAG, loginPath + "[getHeaders]" + headers.toString());
                return headers;
            }
        };

    }

    public void documento(DocumentoRequestDTO documentoRequestDTO) {
        Log.d(TAG, "Inicio llamada WS documento...");
        try {
            JSONObject postparams = new JSONObject();
            postparams.put("username", documentoRequestDTO.getUsername());
            postparams.put("password", documentoRequestDTO.getPassword());
            postparams.put("idPos", documentoRequestDTO.getIdPos());
            postparams.put("tipodocumento", documentoRequestDTO.getTipodocumento());
            postparams.put("monto", documentoRequestDTO.getMonto());
            JsonObjectRequest jsonObjReq = generarRequestDocumento(postparams);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queueLogin.add(jsonObjReq);
            Log.d(TAG, "Fin llamada WS documento.");
        } catch (JSONException e) {
            Log.d(TAG, "Fin llamada WS documento.");
        }
    }

    private JsonObjectRequest generarRequestDocumento(JSONObject postparams) {
        Log.d(TAG, "generarRequestDocumento: " + urlBase + documentPath);
        return new JsonObjectRequest(Request.Method.POST,
                urlBase + documentPath, postparams,
                response -> {
                    if (response != null) {
                        Log.d(TAG, documentPath + "[response] " + (response.toString() == null ? "null" : response.toString()));
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        DocumentoResponseDTO documentoResponseDTO = gson.fromJson(response.toString(), DocumentoResponseDTO.class);
                        Log.d(TAG, documentoResponseDTO.toString());
                    }
                },
                error -> {

                    error.printStackTrace();
                }) {

            @Override
            public Map getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                Log.d(TAG, documentPath + "[getHeaders]" + headers.toString());
                return headers;
            }
        };

    }
}
