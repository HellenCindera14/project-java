package bdki.project.constant;


import org.springframework.stereotype.Service;

@Service
public class  LogInfoFormat {
    
    public String setLogInfo(String nameService, String stringReq, String stringRes, String stringUrl, String stringHeader, String stringQueryParam ){
        String log = "";

        String separator = "\n =================================================================== \n" ;
        String serviceName = "\n" + nameService + "\n";
        String url = "URL \t \t : " + stringUrl + " \n";
        String header = stringHeader == "" ? "Header \t \t : \n" : "Header \t \t : " + stringHeader + " \n";
        String queryParam = stringQueryParam == "" ? "QueryParam \t : \n" : "QueryParam \t : " + stringQueryParam + " \n";
        String request = "Request \t : " + stringReq + " \n";
        String response = "Response \t : " + stringRes + " \n";

        log = separator + serviceName + url + header + queryParam + request + response +  separator;
        
        return log;
    }
}
