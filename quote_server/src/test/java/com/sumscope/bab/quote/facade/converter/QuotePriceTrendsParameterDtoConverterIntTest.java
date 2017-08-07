package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsParameterDto;
import com.sumscope.optimus.commons.exceptions.ValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by fan.bai on 2016/12/22.
 */
public class QuotePriceTrendsParameterDtoConverterIntTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private PriceTrendsParameterDtoConverter converter;

    @Test
    public void testConverter(){
        QuotePriceTrendsParameterDto dto = new QuotePriceTrendsParameterDto();
        try {
            converter.convertToModel(dto);
            Assert.assertTrue("数据验证异常，应抛出ValidationException",true);
        }catch (ValidationException ve){
            Assert.assertTrue("数据验证出现异常",ve.getExceptionDetails().size() > 0);
        }
    }
}
