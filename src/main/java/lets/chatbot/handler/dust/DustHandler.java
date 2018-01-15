package lets.chatbot.handler.dust;

import com.squareup.okhttp.OkHttpClient;
import lets.chatbot.handler.MessageHandler;
import lets.chatbot.handler.dust.dustInfo.DustResponse;
import lets.chatbot.handler.dust.location.LocationResponse;
import lets.chatbot.vo.RequestMessage;
import lets.chatbot.vo.ResponseMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

public class DustHandler implements MessageHandler {
    private LocationResponse locationResponse;
//    private DustApiConfiguration dustApiConfiguration;
    private RestClientException errCause = null;

    public ResponseMessage handle(RequestMessage message) {

        ResponseMessage response = new ResponseMessage();
        ResponseEntity<DustResponse> dustResponse;
        String[] inputText = message.getText().split(" ");

        try {
            System.out.println("inputText size: " + inputText.length);
            if (inputText.length == 1) {
                response = setResponseMessage(response, message);
                response.setText("지역을 입력 하세요");
                return response;
            }

            if (!connectLocationServer(inputText[1])) {
                response = setResponseMessage(response, message);
                response.setText("잘못된 지역을 입력하셨습니다");
                return response;
            } else {

                try {
                    dustResponse = connectDustServer();
                    response = checkDustInfoResp(dustResponse, response, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            response = new ResponseMessage();
            response.setText(e.getMessage());
        }
        return response;
    }

    private ResponseMessage checkDustInfoResp(ResponseEntity<DustResponse> dustResponse, ResponseMessage response, RequestMessage message) {
        if (dustResponse == null) {
            response = setResponseMessage(response, message);
            response.setText("Server Error: " + errCause.getMessage());
        } else {
            response = setResponseMessage(response, message);
            response.setText("[미세먼지] 수치: " + dustResponse.getBody().getDustValue().getValue() +
                    " / 등급: " + dustResponse.getBody().getDustGrade().getGrade() + " 입니다");
        }
        return response;
    }

    private boolean connectLocationServer(String location) {
        OkHttpClient client = new OkHttpClient();
        ClientHttpRequestFactory requestFactory = new OkHttpClientHttpRequestFactory(client);

        URI locationUri = UriComponentsBuilder.newInstance().scheme("http")
   
                .queryParam("q", location)
                .queryParam("output", "json")
                .build()
                .encode()
                .toUri();

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        locationResponse = restTemplate.getForObject(locationUri, LocationResponse.class);
        System.out.println("Location response: " + locationResponse.toString());

        return locationResponse.checkLocCnt();
    }

    private ResponseMessage setResponseMessage(ResponseMessage response, RequestMessage message) {
        response.setType("message");
        response.setChannel(message.getChannel());
        return response;
    }

    private ResponseEntity<DustResponse> connectDustServer() throws IOException {
        ResponseEntity<DustResponse> responseEntity = null;
        org.springframework.http.HttpEntity<String> httpEntity;

        OkHttpClient client = new OkHttpClient();
        ClientHttpRequestFactory requestFactory = new OkHttpClientHttpRequestFactory(client);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")

                .queryParam("lon", locationResponse.getChannel().getItem().get(0).getLng())
                .queryParam("lat", locationResponse.getChannel().getItem().get(0).getLat())
                .queryParam("version", 1)
                .build()
                .encode()
                .toUri();

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        httpEntity = new org.springframework.http.HttpEntity<>(makeDustHeader());

        //RestTemplate 로 HTTP request 전달
        try {
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, DustResponse.class);
            System.out.println("Dust response: " + responseEntity.toString());
        } catch (RestClientException e) {
            errCause = e;
            System.out.println("Occur error");
        }
        return responseEntity;
    }

//	private org.springframework.http.HttpEntity<String> addHeaderForHttpEntity() {
//		return new org.springframework.http.HttpEntity<String>(makeDustHeader());
//	}

    private org.springframework.http.HttpHeaders makeDustHeader() {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
