package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.facade.QuoteExcelParserFacadeService;
import com.sumscope.bab.quote.model.dto.ExcelParserResultDto;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

/**
 * Created by Administrator on 2017/1/24.
 * excel 导入测试
 */
public class QuoteExcelParserFacadeServiceTest extends AbstractBabQuoteIntegrationTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuoteExcelParserFacadeService quoteExcelParserFacadeService;

    //本测试要求按excel模板填写完毕后,记得让曹良解密excel
    // 不然会解析失败，因为公司电脑office会自动加密，无法解析
    // 切记、切记，要解密，解密后不要打开，直接导入即可 或直接读取即可，如打开还要解密。。
    @Test
    public void quoteExcelParserTest() {
        String ssr_fileName_bkb = "F:\\projects\\excel_test\\ssr_bkb_test.xlsx";
        String ssr_fileName_cmb = "F:\\projects\\excel_test\\ssr_cmb_test.xlsx";
        String ssc_fileName_bkb = "F:\\projects\\excel_test\\ssc_bkb_test.xlsx";
        String ssc_fileName_cmb = "F:\\projects\\excel_test\\ssc_cmb_test.xlsx";
        String npc_fileName = "F:\\projects\\excel_test\\ssc_cmb_test.xlsx";
        String userId="1c497085d6d511e48ec3000c29a13c19";

        //SSR模板有两个,银票 商票
        ExcelParserTest(ssr_fileName_bkb,BABQuoteType.SSR,userId);
        ExcelParserTest(ssr_fileName_cmb,BABQuoteType.SSR,userId);

        //SSC模板有两个
        ExcelParserTest(ssc_fileName_bkb,BABQuoteType.SSC,userId);
        ExcelParserTest(ssc_fileName_cmb,BABQuoteType.SSC,userId);

        //NPC模板有一个
        ExcelParserTest(npc_fileName,BABQuoteType.NPC,userId);
    }

    private void ExcelParserTest(String fileName,BABQuoteType quoteType,String userId){
        try {
            ExcelParserResultDto resultDto=new ExcelParserResultDto();
            if (fileName.endsWith(Constant.NEW_EXCEL)) {
                XSSFWorkbook workbook=(XSSFWorkbook)quoteExcelParserFacadeService.createWorkbook(new FileInputStream(new File(fileName)));
                quoteExcelParserFacadeService.converterQuoteDtoByQuoteType(quoteType, workbook,resultDto,userId);
            }else{
                HSSFWorkbook workbook=(HSSFWorkbook)quoteExcelParserFacadeService.createWorkbook(new FileInputStream(new File(fileName)));
                quoteExcelParserFacadeService.converterQuoteDtoByQuoteType(quoteType, workbook,resultDto,userId);
            }
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger,"当前excel文件没有解密,无法解析",e);
        }
    }

}
