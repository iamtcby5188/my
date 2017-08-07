export default {
    addPriceDataDefine: [
        {
            id: "sscAp1", vm: 'billTypeMedium', component: 'inputButtonSwitcher', containerFlex: "100", attribute: {
                isRequired: true, vmType: "Object", itemSource: [
                    { displayName: '纸票', vmValue: { billMedium: 'PAP', billType: 'BKB', minor: false }, isDefault: true },
                    { displayName: '电票', vmValue: { billMedium: 'ELE', billType: 'BKB', minor: false } },
                    { displayName: '小纸票', vmValue: { billMedium: 'PAP', billType: 'BKB', minor: true } },
                    { displayName: '小电票', vmValue: { billMedium: 'ELE', billType: 'BKB', minor: true } },
                    { displayName: '商票', vmValue: { billMedium: 'PAP', billType: 'CMB', minor: false } }]
            }
        },
        {
            id: "sscAp2", vm: 'unit', component: 'inputButtonSelector', containerFlex: "100", attribute: {
                noLabel: true, isRequired: true, vmType: "Object", labelFlex: 10, itemSource: [
                    // { displayName: '‰', vmValue: "millesimal", isDefault: true },
                    { displayName: '%', vmValue: "percent" ,isDefault: true}]
            }
        },

        { id: "sscAp3", vm: 'prices.ggPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '国股', labelFlex: 5, inputFlex: 45 } },
        { id: "sscAp4", vm: 'prices.csPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '城商', labelFlex: 5, inputFlex: 45 } },
        { id: "sscAp5", vm: 'prices.nsPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '农商', labelFlex: 5, inputFlex: 45 } },
        { id: "sscAp6", vm: 'prices.nxPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '农信', labelFlex: 5, inputFlex: 45 } },
        { id: "sscAp7", vm: 'prices.nhPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '农合', labelFlex: 5, inputFlex: 45 } },
        { id: "sscAp8", vm: 'prices.czPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '村镇', labelFlex: 5, inputFlex: 45 } },
        { id: "sscAp9", vm: 'prices.wzPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '外资', labelFlex: 5, inputFlex: 45 } },
        { id: "sscAp10", vm: 'prices.cwPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '财务公司', labelFlex: 5, inputFlex: 45 } },
        { id: "sscAp11", vm: 'prices.ybhPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '有保函', labelFlex: 5, inputFlex: 45 } },
        { id: "sscAp12", vm: 'prices.wbhPrice.inputValue', component: 'inputLabel', containerFlex: "50", attribute: { label: '无保函', labelFlex: 5, inputFlex: 45 } },

        { component: 'spiltRow' },

        // { id: "sscAp13", vm: 'quoteProvinces', component: 'dropLabel', attribute: { label: '地点', placeholder: '不限', displayPath: 'name' } },
        { id: "sscAp14", vm: 'effectiveDate', component: 'inputDatePicker', attribute: { label: '生效日期', inputFlex: 30, defaultValue: new Date() } },

        // { id: "sscAp15", vm: 'additionalInfo.quoteCompanyName', component: 'inputLabel', attribute: { label: '报价方', isDisabled: true, isRequired: true } },
        // { id: "sscAp16", vm: 'additionalInfo.contactName', component: 'inputLabel', attribute: { label: '联系人', isDisabled: true, isRequired: true } },
        // { id: "sscAp17", vm: 'additionalInfo.contactTelephone', component: 'inputLabel', attribute: { label: '联系电话', isDisabled: true, isRequired: true } },
        { id: "sscAp15", vm: 'joininguser', component: 'addPriceJoininguser', attribute: {} },

        { id: "sscAp18", vm: 'memo', component: 'inputLabel', mutiLine: 'true', attribute: { label: '备注', inputFlex: 80 } },
    ],

    viewModelDataDefine: {
        addPrice: {
            // "id": "",
            "billType": "billTypeMedium.billType",
            "billMedium": "billTypeMedium.billMedium",
            "memo": "memo",
            "quoteCompanyDto": { "id": "joininguser.joiningCompanyDto.id" },
            "contactDto": { "id": "joininguser.joiningUserDto.id" },
            // "operatorId": "currentUser",
            // "createDate": "", "lastUpdateDate": "", "effectiveDate": "", "expiredDate": "",
            // "quoteStatus": "DFT", 
            // "containsAdditionalInfo": "false",
            // "additionalInfo": {
            //     'quoteCompanyName': 'additionalInfo.quoteCompanyName', 
            //     'contactName': 'additionalInfo.contactName', 
            //     'contactTelephone': 'additionalInfo.contactTelephone',
            // },
            // "quoteProvinces": "quoteProvinces",
            "effectiveDate": "effectiveDate",
            "minor": "billTypeMedium.minor",
            "ggPrice": "prices.ggPrice.value",
            "csPrice": "prices.csPrice.value",
            "nsPrice": "prices.nsPrice.value",
            "nxPrice": "prices.nxPrice.value",
            "nhPrice": "prices.nhPrice.value",
            "czPrice": "prices.czPrice.value",
            "wzPrice": "prices.wzPrice.value",
            "cwPrice": "prices.cwPrice.value",
            "ybhPrice": "prices.ybhPrice.value",
            "wbhPrice": "prices.wbhPrice.value",

            "quoteToken": "quoteToken"
        },

        editPriod: {
            'billTypeMedium': e => {
                return { billType: e.billType, billMedium: e.billMedium, minor: e.minor };
            },
            'amount': e => e.amount,
            // 'quoteProvinces': e => e.quoteProvinces,
            'memo': e => e.memo,
            'joininguser': e => { return { joiningCompanyDto: e.quoteCompanyDto, joiningUserDto: e.contactDto }; },

            'effectiveDate': e => { return new Date(e.effectiveDate); },
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
            { prop: "prices.ggPrice.value", rule: "rangeClose", displayName: "国股价格", errorMessage: "国股价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },
            { prop: "prices.csPrice.value", rule: "rangeClose", displayName: "城商价格", errorMessage: "城商价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },
            { prop: "prices.nsPrice.value", rule: "rangeClose", displayName: "农商价格", errorMessage: "农商价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },
            { prop: "prices.nxPrice.value", rule: "rangeClose", displayName: "农信价格", errorMessage: "农信价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },
            { prop: "prices.nhPrice.value", rule: "rangeClose", displayName: "农合价格", errorMessage: "农合价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },
            { prop: "prices.czPrice.value", rule: "rangeClose", displayName: "村镇价格", errorMessage: "村镇价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },
            { prop: "prices.wzPrice.value", rule: "rangeClose", displayName: "外资价格", errorMessage: "外资价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },
            { prop: "prices.cwPrice.value", rule: "rangeClose", displayName: "财务公司价格", errorMessage: "财务公司价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },
            { prop: "prices.ybhPrice.value", rule: "rangeClose", displayName: "有保函", errorMessage: "有保函价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },
            { prop: "prices.wbhPrice.value", rule: "rangeClose", displayName: "无保函", errorMessage: "无保函公司价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },

            { prop: "prices.ggPrice", displayName: "国股价格", errorMessage: "国股价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位" },
            { prop: "prices.csPrice", displayName: "城商价格", errorMessage: "城商价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位" },
            { prop: "prices.nsPrice", displayName: "农商价格", errorMessage: "农商价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位" },
            { prop: "prices.nxPrice", displayName: "农信价格", errorMessage: "农信价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位" },
            { prop: "prices.nhPrice", displayName: "农合价格", errorMessage: "农合价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位" },
            { prop: "prices.czPrice", displayName: "村镇价格", errorMessage: "村镇价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位" },
            { prop: "prices.wzPrice", displayName: "外资价格", errorMessage: "外资价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位" },
            { prop: "prices.cwPrice", displayName: "财务公司价格", errorMessage: "财务公司价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位" },
            { prop: "prices.ybhPrice", displayName: "有保函", errorMessage: "有保函价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位" },
            { prop: "prices.wbhPrice", displayName: "无保函", errorMessage: "无保函价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位" },

            { prop: "memo", rule: "maxLength", displayName: "备注", errorMessage: "备注最多50个字符。", param: 50 }
        ],

        initVaildRuleFunc: (originRule) => {

            var checkFunctionParam = [vm => {
                if (!vm || !vm.inputValue) return true;
                if (vm.unit === 'millesimal') {
                    return /^(\d+)$|^(\d+\.\d{1,2})$/.test(vm.inputValue);
                }
                else {
                    return /^(\d+)$|^(\d+\.\d{1,3})$/.test(vm.inputValue);
                }
            }];

            ["prices.ggPrice", "prices.csPrice", "prices.nsPrice", "prices.nxPrice", "prices.nhPrice", "prices.czPrice", "prices.wzPrice", "prices.cwPrice", "prices.ybhPrice", "prices.wbhPrice"].forEach(e => {
                originRule.findItem(e1 => e1.prop === e).param = checkFunctionParam;
            });

            return originRule;
        }
    }
};

