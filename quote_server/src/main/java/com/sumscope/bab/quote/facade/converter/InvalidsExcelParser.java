package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABSheetName;
import com.sumscope.bab.quote.facade.QuoteExcelParserFacadeService;
import com.sumscope.bab.quote.model.dto.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 * excel各个字段验证类
 */
public final class InvalidsExcelParser {

    @Autowired
    private static QuoteExcelParserFacadeService excel;

    private InvalidsExcelParser() {
    }


    /**
     * 验证联系联系方式
     */
    public static void getInvalidsContactTelephone(Row row, ExcelParserResultDto resultDto, int i, QuoteAdditionalInfoDto dto) {
        try {
            if (row.getCell(2) != null) {
                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
            }
            String regex = "1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";
            String valueByRow = getValueByRow(row, 2);
            boolean validateParam = valueByRow != null ? (valueByRow).matches(regex) : false;
            if (!validateParam) {
                if ("".equals(row.getCell(2).getStringCellValue())) {
                    dto.setContactTelephone(null);
                    return;
                }
                setTelephone(resultDto, i);
            } else {
                dto.setContactTelephone(getValueByRow(row, 2));
            }
        } catch (Exception e) {
            setTelephone(resultDto, i);
        }
    }

    private static void setTelephone(ExcelParserResultDto resultDto, int i) {
        setInvalidsResultDto(resultDto, getErrorMsg(i, 2 + 2, "联系方式"));
    }

    private static String getErrorMsg(int i, int j, String msg) {
        String template = "第%d行，第%d列%s错误！";
        return String.format(template, i, j, msg);
    }

    private static void setInvalidsResultDto(ExcelParserResultDto resultDto, String e) {
        if (resultDto.getInvalids() != null && resultDto.getInvalids().size() > 0) {
            resultDto.getInvalids().add(e);
        } else {
            List<String> list = new ArrayList<>();
            list.add(e);
            resultDto.setInvalids(list);
        }
    }

    /**
     * 验证联系人
     */
    public static void getInvalidsContactName(Row row, ExcelParserResultDto resultDto, int i, QuoteAdditionalInfoDto dto) {
        try {
            dto.setContactName(getValueByRow(row, 1));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 1 + 2, "联系人"));
        }
    }

    /**
     * 验证发价方
     */
    public static void getInvalidsQuoteCompanyName(Row row, ExcelParserResultDto resultDto, int i, QuoteAdditionalInfoDto dto) {
        try {
            dto.setQuoteCompanyName(getValueByRow(row, 0));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 2, "机构名"));
        }
    }

    /**
     * 验证日期
     */
    public static void setInvalidsEffectiveDate(ExcelParserResultDto resultDto, int i, Row row, AbstractCountryQuoteDto quoteDto, int l) {
        try {
            //row.getCell(l).getDateCellValue()
            quoteDto.setEffectiveDate(row.getCell(l) != null ? new Date() : null);
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, l + 2, "日期"));
        }
    }

    /**
     * 验证有保函
     */
    public static void setInvalidsYbhPrice(ExcelParserResultDto resultDto, int i, Row row, SSCQuoteDto quoteDto) {
        try {
            quoteDto.setYbhPrice(getPriceValueByRow(row, 5));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 5 + 2, "有保函价格"));
        }
    }

    /**
     * 验证无保函
     */
    public static void setInvalidsWbhPrice(ExcelParserResultDto resultDto, int i, Row row, SSCQuoteDto quoteDto) {
        try {
            quoteDto.setWbhPrice(getPriceValueByRow(row, 6));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 6 + 2, "无保函价格"));
        }
    }

    /**
     * 验证备注
     */
    public static void setInvalidsMemo(ExcelParserResultDto resultDto, int i, Row row, AbstractCountryQuoteDto quoteDto, int l) {
        try {
            quoteDto.setMemo(getCellValue(row, l));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, l + 2, "说明"));
        }
    }

    /**
     * 验证国股报价
     */
    public static void setInvalidsGgPrice(Row row, AbstractCountryQuoteDto quoteDto, ExcelParserResultDto resultDto, int i) {
        try {
            quoteDto.setGgPrice(getPriceValueByRow(row, 6));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 6 + 2, "国股价格"));
        }
    }

    /**
     * 验证城商报价
     */
    public static void setInvalidsCsPrice(Row row, AbstractCountryQuoteDto quoteDto, ExcelParserResultDto resultDto, int i) {
        try {
            quoteDto.setCsPrice(getPriceValueByRow(row, 7));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 7 + 2, "城商价格"));
        }
    }

    /**
     * 验证农商报价
     */
    public static void setInvalidsNsPrice(Row row, AbstractCountryQuoteDto quoteDto, ExcelParserResultDto resultDto, int i) {
        try {
            quoteDto.setNsPrice(getPriceValueByRow(row, 8));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 8 + 2, "农商价格"));
        }
    }

    /**
     * 验证农信报价
     */
    public static void setInvalidsNxPrice(Row row, AbstractCountryQuoteDto quoteDto, ExcelParserResultDto resultDto, int i) {
        try {
            quoteDto.setNxPrice(getPriceValueByRow(row, 9));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 9 + 2, "农信价格"));
        }
    }

    /**
     * 验证农合报价
     */
    public static void setInvalidsNhPrice(Row row, AbstractCountryQuoteDto quoteDto, ExcelParserResultDto resultDto, int i) {
        try {
            quoteDto.setNhPrice(getPriceValueByRow(row, 10));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 10 + 2, "农合价格"));
        }
    }

    /**
     * 验证外资报价
     */
    public static void setInvalidsWzPrice(Row row, AbstractCountryQuoteDto quoteDto, ExcelParserResultDto resultDto, int i) {
        try {
            quoteDto.setWzPrice(getPriceValueByRow(row, 11));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 11 + 2, "外资价格"));
        }
    }

    /**
     * 验证城镇报价
     */
    public static void setInvalidsCzPrice(Row row, AbstractCountryQuoteDto quoteDto, ExcelParserResultDto resultDto, int i) {
        try {
            quoteDto.setCzPrice(getPriceValueByRow(row, 12));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 12 + 2, "村镇价格"));
        }
    }

    /**
     * 验证财务公司报价
     */
    public static void setInvalidsCwPrice(Row row, AbstractCountryQuoteDto quoteDto, ExcelParserResultDto resultDto, int i) {
        try {
            quoteDto.setCwPrice(getPriceValueByRow(row, 13));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 13 + 2, "财务公司价格"));
        }
    }

    public static boolean setInvalidsNPCSheetName(ExcelParserResultDto resultDto, String sheetName) {
        String npc = BABSheetName.BAB_NPC_BKB_SHEET_NAME.getDisplayName();
        return invalidsSheetName(resultDto, sheetName, npc, npc);
    }

    public static boolean setInvalidsSSCSheetName(ExcelParserResultDto resultDto, String sheetName) {
        String sscBKB = BABSheetName.BAB_SSC_BKB_SHEET_NAME.getDisplayName();
        String sscCMB = BABSheetName.BAB_SSC_CMB_SHEET_NAME.getDisplayName();
        return invalidsSheetName(resultDto, sheetName, sscBKB, sscCMB);
    }

    public static boolean setInvalidsSSRSheetName(ExcelParserResultDto resultDto, String sheetName) {
        String ssrBKB = BABSheetName.BAB_SSR_BKB_SHEET_NAME.getDisplayName();
        String ssrCMB = BABSheetName.BAB_SSR_CMB_SHEET_NAME.getDisplayName();
        return invalidsSheetName(resultDto, sheetName, ssrBKB, ssrCMB);
    }

    /**
     * 判断模板是否正确
     */
    public static boolean invalidsSheetName(ExcelParserResultDto resultDto, String sheetName, String bkbSheetName, String cmbSheetName) {
        if (!bkbSheetName.equals(sheetName) && !cmbSheetName.equals(sheetName)) {
            List<String> invalidsList = new ArrayList<>();
            String invalids;
            if (bkbSheetName.equals(cmbSheetName)) {
                invalids = "模板错误,该模板不是" + bkbSheetName + "模板!";
            } else {
                invalids = "模板错误,该模板不是" + bkbSheetName + "模板或" + cmbSheetName + "模板!";
            }
            invalidsList.add(invalids);
            resultDto.setInvalids(invalidsList);
            return true;
        }
        return false;
    }

    public static String setInvalidsBillType(ExcelParserResultDto resultDto, int i, Row row, String billType) {
        try {
            return row.getCell(4) != null ? row.getCell(4).getStringCellValue().trim() : null;
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, "模板第" + i + "行,第" + (4 + 2) + "列错误！");
        }
        return billType;
    }

    /**
     * 验证报价方向
     */
    public static void setInvalidsDirection(ExcelParserResultDto resultDto, int i, Row row, SSRQuoteDto quoteDto) {
        try {
            quoteDto.setDirection(excel.directionByCell(row.getCell(3) != null ? row.getCell(3).getStringCellValue().trim() : null));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 3 + 2, "报价方向"));
        }
    }


    /**
     * 验证承兑方全称
     */
    public static void setInvalidsAcceptingHouseName(ExcelParserResultDto resultDto, int i, Row row, QuoteAdditionalInfoDto dto) {
        try {
            dto.setAcceptingHouseName(getValueByRow(row, 5));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 5 + 2, "承兑方全称"));
        }
    }

    /**
     * 验证承兑方类别
     */
    public static void setInvalidsBABAcceptingCompanyType(ExcelParserResultDto resultDto, int i, Row row, QuoteAdditionalInfoDto dto) {
        try {
            dto.setCompanyType(row.getCell(6) != null ? excel.babAcceptingCompanyTypeByCell(row.getCell(6).getStringCellValue().trim()) : null);
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 6 + 2, "承兑方类别"));
        }
    }

    /**
     * 验证报价金额
     */
    public static void setInvalidsAmount(ExcelParserResultDto resultDto, int i, Row row, SSRQuoteDto quoteDto, int l) {
        try {
            quoteDto.setAmount(row.getCell(l) != null && row.getCell(l).getNumericCellValue() != 0 ?
                    new BigDecimal(row.getCell(l).getNumericCellValue()).setScale(3, BigDecimal.ROUND_HALF_UP) : null);
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, l + 2, "报价金额"));
        }
    }

    /**
     * 验证票据到期日期
     */
    public static void setInvalidsDueDate(ExcelParserResultDto resultDto, int i, Row row, SSRQuoteDto quoteDto, int l) {
        try {
            quoteDto.setDueDate(row.getCell(l) != null ? row.getCell(l).getDateCellValue() : null);
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, l + 2, "票据到期日期"));
        }
    }

    /**
     * 验证报价价格
     */
    public static void setInvalidsPrice(ExcelParserResultDto resultDto, int i, Row row, SSRQuoteDto quoteDto, int l) {
        try {
            quoteDto.setPrice(row.getCell(l) != null ? String.valueOf(row.getCell(l).getNumericCellValue()) : null);
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, l + 2, "报价价格"));
        }
    }

    /**
     * 验证票据介质 纸票电票
     */
    public static void setInvalidsBillMedium(ExcelParserResultDto resultDto, int i, Row row, NPCQuoteDto quoteDto) {
        try {
            quoteDto.setBillMedium(row.getCell(3) != null ? excel.babBillMediumByCell(row.getCell(3).getStringCellValue().trim()) : null);
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 3 + 2, "票据类型"));
        }
    }

    /**
     * 验证交易模式 买断 回购
     */
    public static void setInvalidsTradeType(ExcelParserResultDto resultDto, int i, Row row, NPCQuoteDto quoteDto) {
        try {
            quoteDto.setTradeType(row.getCell(4) != null ? excel.babTradeTypeByCell(row.getCell(4).getStringCellValue().trim()) : null);
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 4 + 2, "交易模式"));
        }
    }

    /**
     * 验证报价价格类型，银票是不可空
     */
    public static void setInvalidsQuotePriceType(ExcelParserResultDto resultDto, int i, Row row, SSRQuoteDto quoteDto) {
        try {
            quoteDto.setQuotePriceType(excel.babQuotePriceTypeByCell(row.getCell(5).getStringCellValue().trim()));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, 5 + 2, "报价类型"));
        }
    }

    private static String getCellValue(Row row, int i) {
        Cell cell = row.getCell(i);
        if (cell != null) {
            if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                short format = cell.getCellStyle().getDataFormat();
                SimpleDateFormat sdf = null;
                if (format == 14 || format == 31 || format == 57 || format == 58) {
                    //日期
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                } else if (format == 20 || format == 32) {
                    //时间
                    sdf = new SimpleDateFormat("HH:mm");
                } else {
                    //数字
                    return String.valueOf(row.getCell(i).getNumericCellValue());
                }
                double value = cell.getNumericCellValue();
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                return sdf.format(date);
            } else {
                return String.valueOf(cell.getStringCellValue());
            }
        } else {
            return null;
        }
    }

    /**
     * 验证备注
     */
    public static void setInvalidsMemo(ExcelParserResultDto resultDto, int i, Row row, SSRQuoteDto quoteDto, int l) {
        try {
            quoteDto.setMemo(getCellValue(row, l));
        } catch (Exception e) {
            setInvalidsResultDto(resultDto, getErrorMsg(i, l + 2, "说明"));
        }
    }

    private static String getValueByRow(Row row, int i) {
        return row.getCell(i) != null ? row.getCell(i).getStringCellValue() : null;
    }

    private static String getPriceValueByRow(Row row, int i) {
        return row.getCell(i) != null ? String.valueOf(row.getCell(i).getNumericCellValue()) : null;
    }
}
