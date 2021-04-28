package Controllers;

import Services.UserServices;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.Javalin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import Models.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;


import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AuthController {
    private Javalin app;
    public final static String LLAVE_SECRETA = "cRfUjXn2r5u8x/A?D(G-KaPdSgVkYp3s6v9y$B&E)H@MbQeThWmZq4t7w!z%C*F-";

    public AuthController(Javalin app) {
        this.app = app;
    }

    public void routes() {
        app.routes(() -> {
            path("/api/v1/auth", () -> {
                post("/login", ctx -> {
                    int status = 200;
                    Map<String, Object> res = new HashMap();
                    String msg;
                    JsonParser parser = new JsonParser();
                    System.out.println(ctx.body());
                    try {
                        JsonObject user = parser.parse(ctx.body()).getAsJsonObject();
                        User userOfBD = UserServices.getInstancia().findBy("USERNAME", "'" + user.get("username").getAsString() + "'"); //Se busca el usuario en Base de datos

                        if (UserServices.getInstancia().verifyLogin(user, userOfBD)) { //Se compara los usuarios a ver si las credenciales son iguales
                            LoginResponse lr = this.generacionJsonWebToken(userOfBD.getUsername());
                            res.put("user", userOfBD);
                            res.put("token", lr.getToken());
                            msg = "Bienvenido " + userOfBD.getName();
                        } else {
                            msg = "Usuario o contraseña incorrectos";
                            status = 406;
                        }

                    } catch (Exception e) {
                        System.out.println("entra al catch");
                        System.out.println(e);
                        status = 400;
                        msg = "Datos propocionados incorrectos";
                    }

                    res.put("msg", msg);
                    ctx.json(res);
                    ctx.status(status);

                });


                post("/registrar", ctx -> {
                    int status = 200;
                    Map<String, Object> res = new HashMap();
                    String msg = "";
                    Gson g = new Gson();

                    try {
                        User user = g.fromJson(ctx.body(), User.class);
                        if (user != null) {
                            if(UserServices.getInstancia().crear(user)) {
                            msg = "Su usuario ha sido creado con exito";
                            User userOfBD = UserServices.getInstancia().find(user.getId());
                                LoginResponse lr = this.generacionJsonWebToken(userOfBD.getUsername());
                                res.put("user", userOfBD);
                                res.put("token", lr.getToken());

                            }else{
                            msg = "No puede utilizar este usuario";
                            status = 409;

                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        status = 406;
                         msg = "Datos propocionados incorrectos";
                    }

                    res.put("msg", msg);
                    ctx.json(res);
                    ctx.status(status);

                });


                get("/persona", ctx -> {


                    User user = UserServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                        ctx.json(user);
                        ctx.status(200);

                });

            });
        });
    }


    public class LoginResponse {
        String token;

        public LoginResponse() {
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }

    public LoginResponse generacionJsonWebToken(String usuario) {
        LoginResponse loginResponse = new LoginResponse();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(LLAVE_SECRETA);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        // creando la trama.
        String jwt = Jwts.builder()
                .setIssuer("PRUEBA HIT")
                .setSubject("VERSION 1")
                .claim("usuario", usuario)
                .signWith(signatureAlgorithm, signingKey)
                .compact();
        loginResponse.setToken(jwt);
        return loginResponse;
    }

}









