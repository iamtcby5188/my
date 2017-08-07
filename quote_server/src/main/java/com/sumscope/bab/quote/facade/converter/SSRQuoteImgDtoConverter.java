package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.model.dto.SSRQuoteImgDto;
import com.sumscope.bab.quote.model.model.SSRQuoteModel;
import org.springframework.stereotype.Component;

/**
 * Created by fan.bai on 2017/2/3.
 * SSRQuoteImgDto 转换器
 */
@Component
public class SSRQuoteImgDtoConverter {
    /**
     * 转换model至dto
     *
     * @param model 模型数据
     * @return SSRQuoteImgDto
     */
    public SSRQuoteImgDto convertToDto(SSRQuoteModel model) {
        SSRQuoteImgDto dto = new SSRQuoteImgDto();
        dto.setId(model.getId());
        dto.setBase64Img(model.getBase64Img());
        return dto;
    }
}
