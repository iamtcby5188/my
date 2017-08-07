package com.sumscope.bab.quote.client;

import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.httpclients.commons.ExternalInvocationFailedException;
import com.sumscope.httpclients.commons.HttpInvocationUtil;
import com.sumscope.optimus.commons.exceptions.ExceptionDto;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan.bai on 2017/2/23.
 * BAB Quote用于系统间交互的Http Client
 */
public class BabQuoteHttpClient {

    private static final String SSR_PRICE_TRENDS_URL = "http://%s/bab_quote/interSystem/retrieveCurrentSSRPriceTrends";
    private static final String INSERT_NEW_SSRQUOTES_URL = "http://%s/bab_quote/interSystem/insertSSRQuotes";

    private final String urlWithPort;

    public BabQuoteHttpClient(String url){
        this.urlWithPort = url;
    }

    public List<QuotePriceTrendsDto> retrieveCurrentSSRPriceTrends() throws ExternalInvocationFailedException {
        String fullUrl = getFullUrl(SSR_PRICE_TRENDS_URL);
        String entityString = HttpInvocationUtil.invokeJsonEntityPostRequest(null, fullUrl);

        return processReturnEntityString(entityString,QuotePriceTrendsDto.class);

    }

    private String getFullUrl(String ssrPriceTrendsUrl) {
        return String.format(ssrPriceTrendsUrl,urlWithPort);
    }

    private <T> List<T> processReturnEntityString(String entityString,Class<T> rClass) {
        JSONObject resultJson = new JSONObject(entityString);
        int returnCode = resultJson.getInt("return_code");
        if(returnCode ==0 ){
            int resultCount = resultJson.getInt("result_count");
            if(resultCount>0){
                JSONArray result = resultJson.getJSONArray("result");
                return getDtoListFromEntityString(result,rClass );
            }else{
                return new ArrayList<>();
            }

        }else{
            JSONObject returnMessage = resultJson.getJSONObject("return_message");
            ExceptionDto exceptionDto = JsonUtil.readValue(returnMessage.toString(), ExceptionDto.class);
            throw new QuoteHttpInvocationException("Failed to invoke remote service",exceptionDto);
        }
    }

    private <T> List<T> getDtoListFromEntityString(JSONArray resultJson, Class<T> rClass) {

        List<T> resultList = new ArrayList<>();
        for(int i=0;i<resultJson.length();i++){
            JSONObject jsonObject = resultJson.getJSONObject(i);
            T dto = JsonUtil.readValue(jsonObject.toString(), rClass);
            resultList.add(dto);
        }
        return resultList;
    }

    public List<SSRQuoteDto> insertSSRQuotes(List<SSRQuoteDto> quoteDtos) throws ExternalInvocationFailedException {
        if(quoteDtos != null && quoteDtos.size()>0){
            String paramString = JsonUtil.writeValueAsString(quoteDtos);
            String fullUrl = getFullUrl(INSERT_NEW_SSRQUOTES_URL);
            String entityString = HttpInvocationUtil.invokeJsonEntityPostRequest(paramString, fullUrl);
            return processReturnEntityString(entityString, SSRQuoteDto.class);
        }
        return null;
    }
}
