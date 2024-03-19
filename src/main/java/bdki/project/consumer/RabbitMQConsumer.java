package bdki.project.consumer;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bdki.project.constant.LogInfoFormat;

@Service
public class RabbitMQConsumer {

    private Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private Marker MARKER;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LogInfoFormat logInfoFormat;

    @RabbitListener(queues = { "${rabbitmq.queue.name}" })
    public void consume(Message message) throws JsonProcessingException {
        try {
            // Menerima pesan dari RabbitMQ
            String jsonMessage = new String(message.getBody());
            LOGGER.info(String.format("received message : " + jsonMessage));
            JSONObject jsonObject = new JSONObject(jsonMessage);
            JSONArray arrayData = jsonObject.getJSONArray("rawNotification");

            // Memproses setiap rawNotification dalam pesan
            String notificationType = String.valueOf(jsonObject.get("notificationType"));

            // Memastikan arrayData tidak kosong sebelum memanggil postApiCallback()
            if (arrayData != null && arrayData.length() > 0) {
                switch (notificationType) {
                    case "REGISTRATION-ACCOUNT-BINDING":
                        postApiCallback(arrayData, notificationType);
                        break;
                    case "PAYMENT-INTEGRATION":
                        postApiCallback(arrayData, notificationType);
                        break;
                    default:
                        break;
                }
            } else {
                LOGGER.warn("ArrayData is empty or null.");
            }

        } catch (Exception e) {
            // Penanganan kesalahan
            e.printStackTrace();
            String req = objectMapper.writeValueAsString(message);
            String res = " Error : " + e.getMessage().toString();
            String head = "";
            // String queryParams = "";
            String url = "consume - RabbitMQConsumer";

            String logInfo = logInfoFormat.setLogInfo("Access consume - RabbitMQConsumer", req, res, url, head
                    );

            LOGGER.info(MARKER, logInfo);
        }
     }

    @SuppressWarnings({ "unchecked"})
    public void postApiCallback(JSONArray arrayData, String notificationType) throws JsonProcessingException {
    String url = "";
    Map<String, Object> bodyRequest = new HashMap<>();
    HttpHeaders headers = new HttpHeaders();

    try {

        for (int i = 0; i < arrayData.length(); i++) {
            bodyRequest = objectMapper.readValue(arrayData.getJSONObject(i).get("bodyRequest").toString(),
                    Map.class);
            url = "http://ue-dev.bankdki.co.id/api/transactionservice/transaction/cashin";
            JSONArray headerArray = arrayData.getJSONObject(i).getJSONArray("header");
            String method = "POST";

            // Mengambil dan menetapkan header untuk HTTP request
            for (int j = 0; j < headerArray.length(); j++) {
                headers.add(headerArray.getJSONObject(j).getString("key"),
                        headerArray.getJSONObject(j).getString("value"));
            }

            // Menyiapkan HttpEntity dengan body request dan headers
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(bodyRequest, headers);
            HttpMethod httpMethod;

            // menangani berbagai metode HTTP
            switch (method) {
                case "GET":
                    httpMethod = HttpMethod.GET;
                    JSONArray requestParamsArray = arrayData.getJSONObject(i).getJSONArray("requestParams");
                    // Membuat query string untuk request GET
                    StringBuilder params = new StringBuilder();
                    for (int k = 0; k < requestParamsArray.length(); k++) {
                        params.append(requestParamsArray.getJSONObject(k).getString("key"))
                                .append("=")
                                .append(requestParamsArray.getJSONObject(k).getString("value"))
                                .append("&");
                    }
                    if (params.length() > 0) {
                        params.deleteCharAt(params.length() - 1);
                        url += "?" + params.toString();
                    }
                    url = UriComponentsBuilder.fromHttpUrl(url).encode().toUriString();
                    break;
                default:
                    httpMethod = HttpMethod.POST;
                    break;
            }

            // Melakukan pemanggilan API menggunakan RestTemplate
            ResponseEntity<String> responseCallback = restTemplate.exchange(url, httpMethod, request, String.class);

            // Membuat log informasi
            String req = objectMapper.writeValueAsString(bodyRequest);
            String res = responseCallback.getBody();
            String head = objectMapper.writeValueAsString(headers);
            // JSONArray requestParamsArray = arrayData.getJSONObject(i).getJSONArray("requestParams");
            // String requestParamsString = objectMapper.writeValueAsString(requestParamsArray);

            String logInfo = logInfoFormat.setLogInfo("Access Rabbit MQ Consumer " + notificationType, req, res,
                    url, head);

            LOGGER.info(MARKER, logInfo);
        }
    } catch (Exception e) {
        // penanganan kesalahan
        String req = objectMapper.writeValueAsString(bodyRequest);
        String res = "Error : " + e.getMessage().toString();
        String head = objectMapper.writeValueAsString(headers);
        // String requestParamsString = "[]";

        String logInfo = logInfoFormat.setLogInfo("Access Rabbit MQ Consumer " + notificationType, req, res, url,
                head);

        LOGGER.info(MARKER, logInfo);
    }
}

}
