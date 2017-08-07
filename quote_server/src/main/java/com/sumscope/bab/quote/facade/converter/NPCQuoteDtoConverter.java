package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.commons.util.Utils;
import com.sumscope.bab.quote.externalinvoke.RedisCheckHelper;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.dto.NPCQuoteDto;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.model.dto.SSCQuoteDto;
import com.sumscope.bab.quote.model.model.NPCQuoteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/12.
 * NPC报价模型转换器
 */
@Component
public class NPCQuoteDtoConverter extends QuoteDtoConverter {
    @Autowired
    private RedisCheckHelper redisUtils;

    public NPCQuoteDto convertToNPCQuoteDto(NPCQuoteModel model) {
//        ValidationUtil.validateModel(model);

        NPCQuoteDto dto = (NPCQuoteDto)super.mapAbstractModelToDto(new NPCQuoteDto(), model);
        dto = (NPCQuoteDto)super.mapAbstractCountryModel2AbstractDto(dto, model);

        return dto;
    }

    public List<NPCQuoteDto> convertToNPCQuoteDtos(List<NPCQuoteModel> models) {
        if (models == null) {
            return null;
        }

        List<NPCQuoteDto> lst = new ArrayList<>();
        for (NPCQuoteModel model : models) {
            NPCQuoteDto dto = convertToNPCQuoteDto(model);
            if (dto != null) {
                lst.add(dto);
            }
        }

        return lst;
    }

    public List<NPCQuoteDto> mergeList(List<NPCQuoteModel> models, List<NPCQuoteModel> oldModels) {
        List<NPCQuoteDto> npcQuoteDto = convertToNPCQuoteDtos(models);
        List<NPCQuoteDto> oldNPCQuoteDto = convertToNPCQuoteDtos(oldModels);
        for(NPCQuoteDto dto:oldNPCQuoteDto){
            dto.setFromHistory(true);
        }
        if (npcQuoteDto == null || npcQuoteDto.isEmpty()) {
            npcQuoteDto = oldNPCQuoteDto;
        }
        else {
            npcQuoteDto.addAll(oldNPCQuoteDto);
        }
        return npcQuoteDto;
    }

    public NPCQuoteModel convertToNPCQuoteModel(NPCQuoteDto dto) {
        ValidationUtil.validateModel(dto);
        redisUtils.checkTokenModelDto(Utils.validateStr(dto.getQuoteToken()));
        NPCQuoteModel model = (NPCQuoteModel)super.mapAbstractDtoToModel(new NPCQuoteModel(), dto);
        model = (NPCQuoteModel)super.mapAbstractCountryDto2AbstractModel(model, dto);
        doPriceInvalid(dto);
        // 只有未发布的状态才能更改生效日和到期日，其他则不更改
        if(dto.getQuoteStatus() == BABQuoteStatus.DFT && dto.getEffectiveDate()!=null){
            model.setExpiredDate(model.getExpiredDate()!=null ? model.getExpiredDate() : QuoteDateUtils.getExpiredTimeOfDate(dto.getEffectiveDate()));
        }
        return model;
    }



    public List<NPCQuoteModel> convertToNPCQuoteModes(List<NPCQuoteDto> dtos) {
        if (dtos == null) {
            return null;
        }
        List<NPCQuoteModel> lst = new ArrayList<>();
        for (NPCQuoteDto dto : dtos) {
            dto.setBillType(dto.getBillType()!=null ? dto.getBillType() : BABBillType.BKB);//NPC默认为BKB
            NPCQuoteModel model = convertToNPCQuoteModel(dto);
            if (model != null) {
                lst.add(model);
            }
        }

        return lst;
    }
}
