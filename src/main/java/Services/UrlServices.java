package Services;

import Models.Url;

public class UrlServices extends DBManage<Url>{
    private static UrlServices instancia;

    private UrlServices() { super(Url.class); }

    public static UrlServices getInstancia(){
        if(instancia==null){
            instancia = new UrlServices();
        }
        return instancia;
    }
}
