
import Controllers.ApiController;
import Controllers.AuthController;
import Models.Url;
import Models.UrlDetailInfo;
import Models.User;
import Services.*;
import com.sun.istack.NotNull;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.staticfiles.Location;

import javax.persistence.Column;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import static io.javalin.apibuilder.ApiBuilder.*;


public class Main {


    public static void main(String[] args) throws ParseException {

        //JAVALIN CONFIG
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
            config.enableCorsForAllOrigins();
            config.requestCacheSize = new Long(9999999);
            config.addStaticFiles("src/main/resources/public", Location.EXTERNAL);
        });


        app.start(8000);
        DBStart.getInstancia().init();

        String string = "1998-12-15";
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        Date date = format.parse(string);
//                parse(string);

        User user = new User("Alejandro De Leon", date, "Calle 7", "admin", "12345", "alejandrodelonc15@gmail.com");
        UserServices.getInstancia().crear(user);

        app.routes(() -> {
            path("", () -> {
                get(":shortUrl", ctx -> {
                    int status = 200;
                    Boolean redirect = false;

                    Map<String, Object> res = new HashMap();
                    String msg = "";

                    String shortUrl = ctx.pathParam(":shortUrl");
                    System.out.println();
                    Url url = null;

                    try {
                        url = UrlServices.getInstancia().findBy("SHORTURL", "'" + UrlShortenerService.getInstancia().getDomain() + "/" + shortUrl + "'");
                        if (url == null) {
                            msg = "URL invalida";
                        }else{

                        redirect = true;
                        }

                    } catch (Exception e) {
                        msg = "URL invalida";
                        status = 409;
                    }


                    if (redirect == true && url != null) {
                        UrlDetailInfo details = new UrlDetailInfo(ctx.ip(), ctx.header("User-Agent"), new Date());
                        UrlDetailInfoServices.getInstancia().crear(details);

                        url.addDetail(details);
                        res.put("urlToRedirect", url.getLongUrl());

                        redirect = true;
                        url.addRate();
                        UrlServices.getInstancia().editar(url);
                    }


                    res.put("msg", msg);
                    ctx.status(status);
                    ctx.json(res);
                });
            });
        });


        new ApiController(app).routes();
        new AuthController(app).routes();

    }
}







