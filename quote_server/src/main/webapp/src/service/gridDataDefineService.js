/**
 * Created by jiannan.niu on 2016/12/15.
 */

class gridDataDefineService {

    constructor () {

        this.BABQuotePriceType = {
            "GG": "国股",
            "CS": "城商",
            "NS": "农商",
            "NX": "农信",
            "NH": "农合",
            "CZ": "村镇",
            "WZ": "外资",
            "CW": "财务公司",
            "YBH": "有保函",
            "WBH": "无保函",
            "CET": "央企",
            "SOE": "国企",
            "LET": "地方性企业",
            "OTH": "其他"
        };

        this.Direction = {
            "IN": "买入",
            "OUT": "卖出",
            "UDF": "未定义"
        };

        this.BABBillType = {
            "BKB": "银",
            "CMB": "商"
        };

        this.BABBillMedium = {
            "PAP": "纸",
            "ELE": "电"
        };

        this.BABQuoteStatus = {
            "DFT": "询价中",
            "DSB": "已发布",
            "DLD": "已成交",
            "CAL": "已撤销",
            "DEL": "已删除"
        };

        this.sort = {
            "due_date": "due_date",
            "price": "price",
            "amount": "amount",
            "last_update_datetime": "last_update_datetime"
        };

        this.sortField = {
            "到期日": "due_date",
            "剩余天数": "due_date",
            "利率": "price",
            "金额": "amount",
            "时间": "last_update_datetime"
        };

        this.amount = [
            {name: 'LESS_30', start: 0, end: 299999},
            {name: 'A30_50', start: 300000, end: 500000},
            {name: 'A50_100', start: 500000, end: 1000000},
            {name: 'A100_300', start: 1000000, end: 3000000},
            {name: 'A300_500', start: 3000000, end: 5000000},
            {name: 'A500_1000', start: 5000000, end: 10000000},
            {name: 'A1000_5000', start: 10000000, end: 50000000},
            {name: 'LAGE_5000', start: 50000000, end: Infinity}
        ];

        this.billType = {
            "PAP_BKB": {"billType": "BKB", "billMedium": "PAP", "bMinor": false},
            "ELE_BKB": {"billType": "BKB", "billMedium": "ELE", "bMinor": false},
            "MINOR_PAP_BKB": {"billType": "BKB", "billMedium": "PAP", "bMinor": true},
            "MINOR_ELE_BKB": {"billType": "BKB", "billMedium": "ELE", "bMinor": true},
            "CMB": {"billType": "CMB", "billMedium": undefined, "bMinor": false},
        };

        this.tradType = {
            "BOT": "买断",
            "BBK": "回购"
        };

        this.PriceMngStatus = {
            "DFT": "未发布",
            "DSB": "已发布",
        }

        this.quotString = {
            dir:{
                "IN":"收",
                "OUT":"出",
            },
            billMedium:{
                "ELE":"电",
                "PAP":"纸"
            },
            billType:{
                "CMB":"商",
                "BKB":"银"
            },
            priceType:{
                "GG": "国股",
                "CS": "城商",
                "NS": "农商",
                "NX": "农信",
                "NH": "农合",
                "CZ": "村镇",
                "WZ": "外资",
                "CW": "财务公司",
                "YBH": "有保函",
                "WBH": "无保函"
            }
        };

        this.Shibor={
            "SHIBOR_O_N":"O/N",
            "SHIBOR_1W":"1W",
            "SHIBOR_2W":"2W",
            "SHIBOR_1M":"1M",
            "SHIBOR_3M":"3M",
            "SHIBOR_6M":"6M",
            "SHIBOR_9M":"9M",
            "SHIBOR_1Y":"1Y",
        };

    }
};

export default gridDataDefineService;