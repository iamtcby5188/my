export default {
    addPriceDataDefine: [
        {
            id: "npcAp1", vm: 'billMedium', component: 'inputButtonSwitcher', containerFlex: "100", attribute: {
                noLabel: true, isRequired: true, vmType: "Object", labelFlex: 0, itemSource: [
                    { displayName: '纸票', vmValue: 'PAP', isDefault: true },
                    { displayName: '电票', vmValue: 'ELE' }]
            }
        },
        {
            id: "npcAp2", vm: 'tradeType', component: 'inputButtonSelector', containerFlex: "25", attribute: {
                noLabel: true, isRequired: true, vmType: "Object", labelFlex: 0, itemSource: [
                    { displayName: '买断', vmValue: 'BOT', isDefault: true },
                    { displayName: '回购', vmValue: 'BBK' }]
            }
        },
        { id: "npcAp3", component: 'labelHtmlContent', containerFlex: "15", html: "<span style='line-height:42px;'>单位：%</span>", attribute: {} },

        { id: "npcAp4", vm: 'prices.ggPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '国股', labelFlex: 5, inputFlex: 45 } },
        { id: "npcAp5", vm: 'prices.csPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '城商', labelFlex: 5, inputFlex: 45 } },
        { id: "npcAp6", vm: 'prices.nsPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '农商', labelFlex: 5, inputFlex: 45 } },
        { id: "npcAp7", vm: 'prices.nxPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '农信', labelFlex: 5, inputFlex: 45 } },
        { id: "npcAp8", vm: 'prices.nhPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '农合', labelFlex: 5, inputFlex: 45 } },
        { id: "npcAp9", vm: 'prices.czPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '村镇', labelFlex: 5, inputFlex: 45 } },
        { id: "npcAp10", vm: 'prices.wzPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '外资', labelFlex: 5, inputFlex: 45 } },
        { id: "npcAp11", vm: 'prices.cwPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '财务公司', labelFlex: 5, inputFlex: 45 } },

        { component: 'spiltRow' },

        { id: "npcAp14", vm: 'effectiveDate', component: 'inputDatePicker', attribute: { label: '生效日期', inputFlex: 30, defaultValue: new Date()  } },
        { id: "sscAp15", vm: 'joininguser', component: 'addPriceJoininguser', attribute: {} },

        { id: "npcAp18", vm: 'memo', component: 'inputLabel', mutiLine: 'true', attribute: { label: '备注', inputFlex: 80 } }
    ],

    viewModelDataDefine: {
        addPrice: {
            // "id": "1", 
            // "direction": "IN", 
            // "billType": "BKB", 

            "billMedium": "billMedium",
            "memo": "memo",

            "contactDto": { "id": "joininguser.joiningUserDto.id" },
            "quoteCompanyDto": { "id": "joininguser.joiningCompanyDto.id" },

            // "operatorId": "currentUser", 
            // "createDate": "", "lastUpdateDate": "", "effectiveDate": "", "expiredDate": "",                 
            // "quoteStatus": "DFT", 
            // "containsAdditionalInfo": "true", "additionalInfo": {},
            // "minor": "true",
            "effectiveDate": "effectiveDate",
            "ggPrice": "prices.ggPrice.value",
            "csPrice": "prices.csPrice.value",
            "nsPrice": "prices.nsPrice.value",
            "nxPrice": "prices.nxPrice.value",
            "nhPrice": "prices.nhPrice.value",
            "czPrice": "prices.czPrice.value",
            "wzPrice": "prices.wzPrice.value",
            "cwPrice": "prices.cwPrice.value",
            "tradeType": "tradeType",

            "quoteToken": "quoteToken"
        },

        editPriod: {
            'billMedium': e => e.billMedium,
            'tradeType': e => e.tradeType,
            'amount': e => e.amount,
            'effectiveDate': e => {return new Date(e.effectiveDate);},
            'quoteProvinces': e => e.quoteProvinces,
            'memo': e => e.memo,
            'joininguser': e => { return { joiningCompanyDto: e.quoteCompanyDto, joiningUserDto: e.contactDto }; },
      
            'prices.ggPrice.inputValue': e => e.ggPrice,
            'prices.csPrice.inputValue': e => e.csPrice,
            'prices.nsPrice.inputValue': e => e.nsPrice,
            'prices.nxPrice.inputValue': e => e.nxPrice,
            'prices.nhPrice.inputValue': e => e.nhPrice,
            'prices.czPrice.inputValue': e => e.czPrice,
            'prices.wzPrice.inputValue': e => e.wzPrice,
            'prices.cwPrice.inputValue': e => e.cwPrice,
            'prices.ybhPrice.inputValue': e => e.ybhPrice,
            'prices.wbhPrice.inputValue': e => e.wbhPrice
        },

        addPriceVaildRule: [
            { prop: "prices.ggPrice.value", rule: "rangeClose", displayName: "国股价格", errorMessage: "国股价格超出有效范围。", param: {min: 0.001, max: 99.999, pattern: 1} },
            { prop: "prices.csPrice.value", rule: "rangeClose", displayName: "城商价格", errorMessage: "城商价格超出有效范围。", param: {min: 0.001, max: 99.999, pattern: 1} },
            { prop: "prices.nsPrice.value", rule: "rangeClose", displayName: "农商价格", errorMessage: "农商价格超出有效范围。", param: {min: 0.001, max: 99.999, pattern: 1} },
            { prop: "prices.nxPrice.value", rule: "rangeClose", displayName: "农信价格", errorMessage: "农信价格超出有效范围。", param: {min: 0.001, max: 99.999, pattern: 1} },
            { prop: "prices.nhPrice.value", rule: "rangeClose", displayName: "农合价格", errorMessage: "农合价格超出有效范围。", param: {min: 0.001, max: 99.999, pattern: 1} },
            { prop: "prices.czPrice.value", rule: "rangeClose", displayName: "村镇价格", errorMessage: "村镇价格超出有效范围。", param: {min: 0.001, max: 99.999, pattern: 1} },
            { prop: "prices.wzPrice.value", rule: "rangeClose", displayName: "外资价格", errorMessage: "外资价格超出有效范围。", param: {min: 0.001, max: 99.999, pattern: 1} },
            { prop: "prices.cwPrice.value", rule: "rangeClose", displayName: "财务公司价格", errorMessage: "财务公司价格超出有效范围。", param: {min: 0.001, max: 99.999, pattern: 1} },

            { prop: "prices.ggPrice",  displayName: "国股价格", errorMessage: "国股价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位"},
            { prop: "prices.csPrice",  displayName: "城商价格", errorMessage: "城商价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位"},
            { prop: "prices.nsPrice",  displayName: "农商价格", errorMessage: "农商价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位"},
            { prop: "prices.nxPrice",  displayName: "农信价格", errorMessage: "农信价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位"},
            { prop: "prices.nhPrice",  displayName: "农合价格", errorMessage: "农合价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位"},
            { prop: "prices.czPrice",  displayName: "村镇价格", errorMessage: "村镇价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位"},
            { prop: "prices.wzPrice",  displayName: "外资价格", errorMessage: "外资价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位"},
            { prop: "prices.cwPrice",  displayName: "财务公司价格", errorMessage: "财务公司价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位"},

            { prop: "memo", rule: "maxLength", displayName: "备注", errorMessage: "备注最多50个字符。", param: 50 }
        ],

        initVaildRuleFunc: (originRule) => {

            var checkFunctionParam = [vm => {
                    if (!vm || !vm.inputValue) return true; 
                                                
                    return /^(\d+)$|^(\d+\.\d{1,3})$/.test(vm.inputValue);
            }];

            ["prices.ggPrice","prices.csPrice","prices.nsPrice","prices.nxPrice","prices.nhPrice","prices.czPrice","prices.wzPrice","prices.cwPrice"].forEach(e=>{
                originRule.findItem(e1=>e1.prop === e).param = checkFunctionParam;
            });

            return originRule;
        }
    }
};

