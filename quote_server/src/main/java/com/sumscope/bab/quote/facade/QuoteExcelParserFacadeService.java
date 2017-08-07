package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.commons.enums.*;
import com.sumscope.bab.quote.commons.exception.BABExceptionCode;
import com.sumscope.bab.quote.facade.converter.InvalidsExcelParser;
import com.sumscope.bab.quote.model.dto.*;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.BABSheetName;
import com.sumscope.bab.quote.commons.enums.BABTradeType;
import com.sumscope.bab.quote.commons.enums.WebSSCBillType;
import com.sumscope.bab.quote.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.iam.edmclient.edmsource.dto.IamProvinceAndIdDTO;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 为了避免在Facade层写入太多的业务逻辑代码，本facadeService类用来处理具体的Excel文件解析功能。
 */
@Component
public class QuoteExcelParserFacadeService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EdmHttpClientHelperWithCache edmHttpClientHelperWithCache;

    /**
     * 根据excel文件内容解析生成报价单Dto列表，在解析过程中发现的不合法数值等内容，
     * 记录在调用方初始化生产的invalids列表中，如果整个文件有错误则直接抛出验证异常。
     * @param excelFileDto excel文件内容，从web端传入
     * @param quoteType    报价单类型
     * @return 解析出的报价单数据 异常信息，列表的实际dto根据参数决定
     */
    public ExcelParserResultDto parserQuotes(ExcelFileDto excelFileDto, BABQuoteType quoteType, String userId) {
        ExcelParserResultDto resultDto = new ExcelParserResultDto();
        List<AbstractQuoteDto> abstractQuoteDto = new ArrayList<>();
        int start = excelFileDto.getData().indexOf("base64,") + "base64,".length();
        byte[] excelFileContents = Base64.getDecoder().decode(excelFileDto.getData().substring(start));
        String lastName = null;
        try {
            String fileName = excelFileDto.getFileName();
            if(fileName.contains(".")) {
                lastName = fileName.substring(fileName.indexOf('.') + 1, fileName.length());
            }
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger,"文件格式错误，不是.xlsx或者.xls文件！",e);
        }
        InputStream in = new ByteArrayInputStream(excelFileContents);
        //注意：poi包对excel新/老版本转成workbook对象不同，老版本对应HSSFWorkbook，而新版本对应的是XSSFWorkbook
        //为了兼容新老版本,需要调用官方文档的提供的方法，也即是本类中createWorkbook()方法中实现的那样
        if (lastName.endsWith(Constant.NEW_EXCEL)) {
            //获取XSSFWorkbook下的第一页,将字段转换成对应的dto，并验证数据的正确性
            XSSFWorkbook workbook = (XSSFWorkbook) createWorkbook(in);
            abstractQuoteDto = converterQuoteDtoByQuoteType(quoteType, workbook, resultDto,userId);
        }
        if(lastName.endsWith(Constant.OLD_EXCEL)) {
            //获取HSSFWorkbook下的第一页,将字段转换成对应的dto，并验证数据的正确性
            HSSFWorkbook workbook = (HSSFWorkbook) createWorkbook(in);
            abstractQuoteDto = converterQuoteDtoByQuoteType(quoteType, workbook, resultDto,userId);
        }
        checkAbstractQuoteDtoIsNull(resultDto, abstractQuoteDto);//检验是否是空模板
        resultDto.setQuotes(abstractQuoteDto!=null ? abstractQuoteDto : null);
        return resultDto;
    }

    private void checkAbstractQuoteDtoIsNull(ExcelParserResultDto resultDto, List<AbstractQuoteDto> abstractQuoteDto) {
        if(abstractQuoteDto!=null && abstractQuoteDto.size()>0){
            setRemoveAbstractQuoteDto(abstractQuoteDto);
        }
        //空模板情况
        if((abstractQuoteDto==null || abstractQuoteDto.size()<=0) &&
                (resultDto.getInvalids()==null || resultDto.getInvalids().size()<=0 )){
            List<String> list = new ArrayList<>();
            list.add("此模板为空模板，请填入对应数据后再批量导入，谢谢配合！");
            resultDto.setInvalids(list);
        }
    }

    private void setRemoveAbstractQuoteDto(List<AbstractQuoteDto> abstractQuoteDto) {
        for(int i=0;i<abstractQuoteDto.size();i++){
            AbstractQuoteDto dto = abstractQuoteDto.get(i);
            if(isBooleanCondition(dto)){
                abstractQuoteDto.remove(dto);
                i--;
            }
        }
    }

    private boolean isBooleanCondition(AbstractQuoteDto dto) {
        return isBooleanAbstractQuoteDtoEnums(dto) || isBooleanAbstractQuoteDtoDate(dto);
    }

    private boolean isBooleanAbstractQuoteDtoDate(AbstractQuoteDto dto) {
        return dto.getDirection()==null && dto.getEffectiveDate()==null&&dto.getAdditionalInfo().getAcceptingHouseName()==null;
    }

    private boolean isBooleanAbstractQuoteDtoEnums(AbstractQuoteDto dto) {
        return dto.getBillMedium()==null && dto.getBillType()==null&& dto.getDirection()==null && dto.getEffectiveDate()==null;
    }

    /**
     * 报价价格类型 国股 城商等
     */
    public static BABQuotePriceType babQuotePriceTypeByCell(String quotePriceType) {
        for (BABQuotePriceType priceType : BABQuotePriceType.values()) {
            if (quotePriceType.equals(priceType.getDisplayName())) {
                return priceType;
            }
        }
        return null;
    }

    /**
     * 承兑行类别 央企 国企等
     */
    public static BABAcceptingCompanyType babAcceptingCompanyTypeByCell(String acceptingCompanyType) {
        for (BABAcceptingCompanyType companyType : BABAcceptingCompanyType.values()) {
            if (acceptingCompanyType.equals(companyType.getDisplayName())) {
                return companyType;
            }
        }
        return null;
    }

    /**
     * 票据介质枚举 纸票 电票等
     */
    public static BABBillMedium babBillMediumByCell(String babBillMedium) {
        for (BABBillMedium billMedium : BABBillMedium.values()) {
            if (babBillMedium.equals(billMedium.getDisplayName())) {
                return billMedium;
            }
        }
        return null;
    }

    /**
     * 交易模式 买断等
     */
    public static BABTradeType babTradeTypeByCell(String babTradeType) {
        for (BABTradeType tradeType : BABTradeType.values()) {
            if (babTradeType.equals(tradeType.getDisplayName())) {
                return tradeType;
            }
        }
        return null;
    }

    /**
     * 报价方向枚举
     */
    public static Direction directionByCell(String babDirection) {
        if(babDirection==null){
            return null;
        }
        if(babDirection.equals("出")){
            return Direction.OUT;
        }
        if(babDirection.equals("收")){
            return Direction.IN;
        }
        return null;
    }

    /**
     * ssc和npc报价公共部分Dto
     */
    private static void setAbstractQuoteDto(Row row, AbstractCountryQuoteDto quoteDto, ExcelParserResultDto resultDto, int i) {
        InvalidsExcelParser.setInvalidsEffectiveDate(resultDto,i,row,quoteDto,5);
        InvalidsExcelParser.setInvalidsGgPrice(row, quoteDto, resultDto, i);
        InvalidsExcelParser.setInvalidsCsPrice(row, quoteDto, resultDto, i);
        InvalidsExcelParser.setInvalidsNsPrice(row, quoteDto, resultDto, i);
        InvalidsExcelParser.setInvalidsNxPrice(row, quoteDto, resultDto, i);
        InvalidsExcelParser.setInvalidsNhPrice(row, quoteDto, resultDto, i);
        InvalidsExcelParser.setInvalidsWzPrice(row, quoteDto, resultDto, i);
        InvalidsExcelParser.setInvalidsCzPrice(row, quoteDto, resultDto, i);
        InvalidsExcelParser.setInvalidsCwPrice(row, quoteDto, resultDto, i);
        InvalidsExcelParser.setInvalidsMemo(resultDto,i,row,quoteDto,14);
    }

    /**
     * 解决新老版本兼容性,转成对应的Workbook实例
     */
    public Workbook createWorkbook(InputStream inp) {
        try {
            InputStream inputStream ;
            if (!inp.markSupported()) {
                inputStream= new PushbackInputStream(inp, 8);
            }else{
                inputStream = inp;
            }
            if (POIFSFileSystem.hasPOIFSHeader(inputStream)) {
                return new HSSFWorkbook(inp);
            }
            if (POIXMLDocument.hasOOXMLHeader(inputStream)) {
                return new XSSFWorkbook(OPCPackage.open(inp));
            }
        } catch (Exception e) {
            throw new BusinessRuntimeException(BABExceptionCode.EXCEL_ERROR, e);
        }
        throw new BusinessRuntimeException(BABExceptionCode.EXCEL_ERROR, "excel版本目前poi无法解析!!!");
    }

    /**
     *获取List<AbstractQuoteDto>方法，并验证模板的正确性
     */
    public List<AbstractQuoteDto> converterQuoteDtoByQuoteType(BABQuoteType quoteType, Workbook workbook, ExcelParserResultDto resultDto,String userId) {
        List<AbstractQuoteDto> list = new ArrayList<>();
        String sheetName = workbook.getSheetName(0);
        if (quoteType == BABQuoteType.SSR) {
            if (InvalidsExcelParser.setInvalidsSSRSheetName(resultDto, sheetName)) {
                return null;
            }
            list.addAll(converterSSRQuoteDto(workbook.getSheetAt(0), resultDto,userId));
        }
        if (quoteType == BABQuoteType.SSC) {
            if (InvalidsExcelParser.setInvalidsSSCSheetName(resultDto, sheetName)) {
                return null;
            }
            list.addAll(converterSSCQuoteDto(workbook.getSheetAt(0), resultDto,userId));
        }
        if (quoteType == BABQuoteType.NPC) {
            if (InvalidsExcelParser.setInvalidsNPCSheetName(resultDto, sheetName)) {
                return null;
            }
            list.addAll(converterNPCQuoteDto(workbook.getSheetAt(0), resultDto,userId));
        }
        return list;
    }

    /**
     * SSR 批量导入 sheet to list<SSRQuoteDto> ,resultDto记录错误信息
     */
    public List<SSRQuoteDto> converterSSRQuoteDto(Sheet sheet, ExcelParserResultDto resultDto,String userId) {
        List<SSRQuoteDto> list = new ArrayList<>();
        int i=-1;
        for (Row row : sheet) {
            i++;
            if (doIndexRowOne(row)){
                continue;
            }
            SSRQuoteDto quoteDto = new SSRQuoteDto();
            QuoteAdditionalInfoDto dto = getQuoteAdditionalInfoDto(row,resultDto,i);
            quoteDto.setAdditionalInfo(dto);
            InvalidsExcelParser.setInvalidsDirection(resultDto, i, row, quoteDto);
            quoteDto.setContainsAdditionalInfo(true);
            quoteDto.setQuoteStatus(BABQuoteStatus.DSB);
            quoteDto.setOperatorId(userId);
            String billType = null;
            billType = InvalidsExcelParser.setInvalidsBillType(resultDto, i, row, billType);
            //银行票据
            setBKBSSRQuoteDto(resultDto,i, row, quoteDto, billType);
            //商业票据
            setCMBSSRQuoteDto(resultDto,i, row, quoteDto, dto, billType);
            list.add(quoteDto);
        }
        return list;
    }

    /**
     *ssr 商票
     */
    private void setCMBSSRQuoteDto(ExcelParserResultDto resultDto,int i, Row row, SSRQuoteDto quoteDto, QuoteAdditionalInfoDto dto, String billType) {
        if ("电商".equals(billType) || "纸商".equals(billType)) {
            if("电商".equals(billType)){
                quoteDto.setBillMedium(BABBillMedium.ELE);
            }
            if("纸商".equals(billType)){
                quoteDto.setBillMedium(BABBillMedium.PAP);
            }
            quoteDto.setBillType(BABBillType.CMB);
            InvalidsExcelParser.setInvalidsAcceptingHouseName(resultDto,i,row,dto);
            InvalidsExcelParser.setInvalidsBABAcceptingCompanyType(resultDto,i,row,dto);
            quoteDto.setAdditionalInfo(dto);
            InvalidsExcelParser.setInvalidsAmount(resultDto,i,row,quoteDto,7);
            InvalidsExcelParser.setInvalidsDueDate(resultDto,i,row,quoteDto,8);
            InvalidsExcelParser.setInvalidsPrice(resultDto,i,row,quoteDto,9);
            quoteDto.setQuoteProvinces(setProvinceInfoByProvinceName(row, 10, resultDto));
            InvalidsExcelParser.setInvalidsMemo(resultDto,i,row,quoteDto,11);
            resultDto.setBillType(BABBillType.CMB);
        }
    }
    /**
     *ssr 银票
     */
    private void setBKBSSRQuoteDto(ExcelParserResultDto resultDto,int i, Row row, SSRQuoteDto quoteDto, String billType) {
        if ("电银".equals(billType) || "纸银".equals(billType)) {
            if("电银".equals(billType)){
                quoteDto.setBillMedium(BABBillMedium.ELE);
            }
            if("纸银".equals(billType)){
                quoteDto.setBillMedium(BABBillMedium.PAP);
            }
            quoteDto.setBillType(BABBillType.BKB);
            InvalidsExcelParser.setInvalidsQuotePriceType(resultDto,i,row,quoteDto);
            InvalidsExcelParser.setInvalidsAmount(resultDto,i,row,quoteDto,6);
            InvalidsExcelParser.setInvalidsDueDate(resultDto,i,row,quoteDto,7);
            InvalidsExcelParser.setInvalidsPrice(resultDto,i,row,quoteDto,8);
            quoteDto.setQuoteProvinces(setProvinceInfoByProvinceName(row, 9, resultDto));
            InvalidsExcelParser.setInvalidsMemo(resultDto,i,row,quoteDto,10);
            resultDto.setBillType(BABBillType.BKB);
        }
    }

    /**
     *附加信息
     */
    private QuoteAdditionalInfoDto getQuoteAdditionalInfoDto(Row row,ExcelParserResultDto resultDto,int i) {
        QuoteAdditionalInfoDto dto = new QuoteAdditionalInfoDto();
        InvalidsExcelParser.getInvalidsQuoteCompanyName(row, resultDto, i, dto);
        InvalidsExcelParser.getInvalidsContactName(row, resultDto, i, dto);
        InvalidsExcelParser.getInvalidsContactTelephone(row, resultDto, i, dto);
        return dto;
    }

    /**
     * SSC 批量导入 sheet to list<SSCQuoteDto> ,resultDto记录错误信息
     */
    public List<SSCQuoteDto> converterSSCQuoteDto(Sheet sheet, ExcelParserResultDto resultDto, String userId) {
        List<SSCQuoteDto> list = new ArrayList<>();
        int i=-1;
        for (Row row : sheet) {
            i++;
            if (doIndexRowOne(row)) {
                continue;
            }
            SSCQuoteDto quoteDto = new SSCQuoteDto();
            quoteDto.setContainsAdditionalInfo(true);
            QuoteAdditionalInfoDto dto = getQuoteAdditionalInfoDto(row,resultDto,i);
            quoteDto.setAdditionalInfo(dto);
            setInvalidsQuoteProvinces(resultDto, i, row, quoteDto);
            quoteDto.setQuoteStatus(BABQuoteStatus.DSB);
            quoteDto.setOperatorId(userId);
            String billType = null;
            if(sheet.getSheetName().equals(BABSheetName.BAB_SSC_BKB_SHEET_NAME.getDisplayName())){
                billType = InvalidsExcelParser.setInvalidsBillType(resultDto, i, row, billType);
            }
            //银行票据
            setELESSCQuoteDto(resultDto, i, row, quoteDto, billType);
            setCMBSSCQuoteDto(sheet, resultDto, i, row, quoteDto);
            list.add(quoteDto);
        }
        return list;
    }

    /**
     *SSC 银票
     */
    private void setELESSCQuoteDto(ExcelParserResultDto resultDto, int i, Row row, SSCQuoteDto quoteDto, String billType) {
        if (WebSSCBillType.ELE_BKB.getDisplayName().equals(billType) || WebSSCBillType.PAP_BKB.getDisplayName().equals(billType)
                || WebSSCBillType.MINOR_ELE_BKB.getDisplayName().equals(billType) || WebSSCBillType.MINOR_PAP_BKB.getDisplayName().equals(billType)) {
            if(WebSSCBillType.ELE_BKB.getDisplayName().equals(billType)){
                quoteDto.setBillMedium(BABBillMedium.ELE);
            }
            if(WebSSCBillType.MINOR_ELE_BKB.getDisplayName().equals(billType)){
                quoteDto.setBillMedium(BABBillMedium.ELE);
                quoteDto.setMinor(true);
            }
            if(WebSSCBillType.PAP_BKB.getDisplayName().equals(billType)){
                quoteDto.setBillMedium(BABBillMedium.PAP);
            }
            if(WebSSCBillType.MINOR_PAP_BKB.getDisplayName().equals(billType)){
                quoteDto.setBillMedium(BABBillMedium.PAP);
                quoteDto.setMinor(true);
            }
            quoteDto.setBillType(BABBillType.BKB);
            setAbstractQuoteDto(row, quoteDto,resultDto,i);
            resultDto.setBillType(BABBillType.BKB);
        }
    }
    /**
     *SSC 商票
     */
    private void setCMBSSCQuoteDto(Sheet sheet, ExcelParserResultDto resultDto, int i, Row row, SSCQuoteDto quoteDto) {
        if(sheet.getSheetName().equals(BABSheetName.BAB_SSC_CMB_SHEET_NAME.getDisplayName())) {
            //商业票据
            quoteDto.setBillMedium(BABBillMedium.PAP);
            quoteDto.setBillType(BABBillType.CMB);
            InvalidsExcelParser.setInvalidsEffectiveDate(resultDto, i, row, quoteDto,4);
            InvalidsExcelParser.setInvalidsYbhPrice(resultDto, i, row, quoteDto);
            InvalidsExcelParser.setInvalidsWbhPrice(resultDto, i, row, quoteDto);
            InvalidsExcelParser.setInvalidsMemo(resultDto, i, row, quoteDto,7);
            resultDto.setBillType(BABBillType.CMB);
        }
    }

    private boolean doIndexRowOne(Row row) {
        if (row.getRowNum() < 1) {
            return true;
        } //表格内容从第2行开始.
        return false;
    }

    /**
     * NPC 批量导入 sheet to list<NPCQuoteDto> ,resultDto记录错误信息
     */
    public List<NPCQuoteDto> converterNPCQuoteDto(Sheet sheet, ExcelParserResultDto resultDto, String userId) {
        List<NPCQuoteDto> list = new ArrayList<>();
        int i=-1;
        for (Row row : sheet) {
            i++;
            if (doIndexRowOne(row)) {
                continue;
            }
            NPCQuoteDto quoteDto = new NPCQuoteDto();
            quoteDto.setOperatorId(userId);
            quoteDto.setQuoteStatus(BABQuoteStatus.DSB);
            quoteDto.setContainsAdditionalInfo(true);
            QuoteAdditionalInfoDto dto = getQuoteAdditionalInfoDto(row,resultDto,i);
            quoteDto.setAdditionalInfo(dto);
            InvalidsExcelParser.setInvalidsBillMedium(resultDto, i, row, quoteDto);
            InvalidsExcelParser.setInvalidsTradeType(resultDto, i, row, quoteDto);
            setAbstractQuoteDto(row, quoteDto,resultDto,i);
            list.add(quoteDto);
        }
        resultDto.setBillType(BABBillType.BKB);
        return list;
    }

    //根据省份名称获取省份信息
    public QuoteProvinceDto setProvinceInfoByProvinceName(Row row, int i, ExcelParserResultDto resultDto) {
        QuoteProvinceDto province = new QuoteProvinceDto();
        Map<String, IamProvinceAndIdDTO> provinceDto = edmHttpClientHelperWithCache.getIamProvinceAndIdDTO();
        if (row.getCell(i)!=null && row.getCell(i).getStringCellValue() != null) {
            IamProvinceAndIdDTO iamProvinceAndIdDTO = provinceDto.get(row.getCell(i).getStringCellValue());
            if (iamProvinceAndIdDTO != null && !"".equals(iamProvinceAndIdDTO)) {
                province.setProvinceName(row.getCell(i).getStringCellValue());
                province.setProvinceCode(iamProvinceAndIdDTO.getCode());
            } else {
                if(row.getCell(i)==null || "".equals(row.getCell(i).getStringCellValue())
                        || row.getCell(i).getStringCellValue().equals("不限")){
                    province=null;
                }else{
                    resultDto.getInvalids().add(row.getCell(i).getStringCellValue());
                }
            }
        }else{
            province=null;
        }
        return province;
    }
    public void setInvalidsQuoteProvinces(ExcelParserResultDto resultDto, int i, Row row, SSCQuoteDto quoteDto) {
        try {
            quoteDto.setQuoteProvinces(setProvinceInfoByProvinceName(row, 3, resultDto));
        } catch (Exception e) {
            if(resultDto.getInvalids()!=null && resultDto.getInvalids().size()>0){
                resultDto.getInvalids().add("第"+i+"行，第"+(3+1)+"列交易地点错误！");
            }else{
                List<String> listInvalids = new ArrayList<>();
                listInvalids.add("第"+i+"行，第"+(3+1)+"列交易地点错误！");
                resultDto.setInvalids(listInvalids);
            }
        }
    }
}
