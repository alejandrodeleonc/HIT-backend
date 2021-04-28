package Controllers;

import Models.Url;
import Models.User;
import Services.UrlDetailInfoServices;
import Services.UrlServices;
import Services.UrlShortenerService;
import Services.UserServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.Javalin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.*;


public class ApiController {

    private Javalin app;

    public ApiController(Javalin app) {
        this.app = app;
    }

    public void routes() {
        app.routes(() -> {
            path("/api/v1/mantenimiento/", () -> {

                post("registrar/longUrl", ctx -> {
                    int status = 200;
                    Map<String, Object> res = new HashMap();
//                    System.out.println(ctx.);
                    String msg = "";
                    User user = UserServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    try {

                        JsonParser parser = new JsonParser();


                        JsonObject urlJson = parser.parse(ctx.body()).getAsJsonObject();
                        String longUrl = urlJson.get("url").toString();
                        System.out.println("longUrl -> " +longUrl);
                        boolean aux = true;
                        String shortUrl = UrlShortenerService.getInstancia().shortenURL(longUrl );
                        int i = 0;

                        Url url = new Url(longUrl, shortUrl + String.valueOf(new Random().nextInt(10000)));

                        UrlServices.getInstancia().crear(url);



                        if (user != null) {
                            user.addAnUrl(url);
                            UserServices.getInstancia().editar(user);
                        }

                        msg = "Su url se ha creado con exito";
                        res.put("shortUrl", url.getShortUrl());


                    } catch (Exception e) {
                        msg = "No se pudo crear su url, revisa si ya no lo has creado";
                        System.out.println(e);
                        status = 409;
                    }

                    res.put("msg", msg);
                    ctx.status(status);
                    ctx.json(res);


                });


                get("misUrlsMasVisitadas/:cantidad", ctx -> {
                    User user = null;
                    int status = 200;
                    Map<String, Object> res = new HashMap();
                    int cantidad = Integer.valueOf(ctx.pathParam("cantidad"));

                    try {
                        user = UserServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));


                    } catch (Exception e) {
                        res.put("msg", "Aun no se ha registrado");
                        status = 403;
                    }


                    if (user != null) {

                        List<Url> urlList = UserServices.getInstancia().myUrlsWithMoreHits(user, cantidad);

                        if (urlList != null) {
                            res.put("urls", urlList);

                        } else {
                            res.put("msg", "Aun no tine url registradas");
                        }
                    }

                    ctx.status(status);
                    ctx.json(res);

                });


                get("agentesWebMasUtilizados/:cantidad", ctx -> {
                    User user = null;
                    int status = 200;
                    Map<String, Object> res = new HashMap();

                    int cantidad =  Integer.valueOf(ctx.pathParam("cantidad"));

                    try {
                        user = UserServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));


                    } catch (Exception e) {
                        res.put("msg", "Aun no se ha registrado");
                        status = 403;
                    }


                    if (user != null) {
                        List<String> urls = UrlDetailInfoServices.getInstancia().getTheTopAgents(cantidad);
                        System.out.println(user);
                        res.put("top", urls);
                    }

                    ctx.status(status);
                    ctx.json(res);

                });


            });


        });
    }
}


