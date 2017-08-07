export default {
    addPriceDataDefine: [
        {
            id: "ssrAp1", vm: 'normalOrQuickBoard', component: 'inputButtonSwitcher', attribute:
            { label: '', itemSource: [{ displayName: '普通报价', vmValue: true, isDefault: true }, { displayName: '快速报价', vmValue: false }] }
        }, {
            id: "ssrAp2", vm: 'direction', component: 'inputButtonSelector', attribute: {
                label: '交易方向', isRequired: true, vmType: "Object", itemSource: [
                    { displayName: '买入', vmValue: 'IN' }, { displayName: '卖出', vmValue: 'OUT', isDefault: true }]
            }
        }, {
            id: "ssrAp3", vm: 'billTypeMedium', component: 'inputButtonSelector', containerClass: "inline-block", attribute: {
                label: '票据类型', isRequired: true, vmType: "Object", itemSource: [
                    { displayName: '电银', vmValue: { billMedium: 'ELE', billType: 'BKB' }, isDefault: true },
                    { displayName: '纸银', vmValue: { billMedium: 'PAP', billType: 'BKB' } },
                    { displayName: '电商', vmValue: { billMedium: 'ELE', billType: 'CMB' } },
                    { displayName: '纸商', vmValue: { billMedium: 'PAP', billType: 'CMB' } }]
            },
            equals: (e1, e2) => {
                if (!e1 && !e2) return true;
                if (e1 && !e2) return false;
                if (!e1 && e2) return false;
                return e1.billMedium && e1.billType && e1.billMedium === e2.billMedium && e1.billType === e2.billType;
            }
        },
        // {
        //     id: "ssrAp4", vm: 'quotePriceType', component: 'dropLabel', default: e => e.code === 'GG', attribute:
        //     { label: '承兑行', isRequired: true, displayPath: 'name', inputFlex: 40 }
        // },
        {
            id: "ssrAp4", vm: 'qptOrAh', component: 'addPriceAcceptinghouseQuotepricetype', default: e => e.code === 'GG', attribute: {
                label: ['承兑行', '承兑方'], qptDisplayPath: 'name', inputFlex: 80, vmType: 'quotePriceType',
            }
        },
        {
            id: "ssrAp5", vm: 'amount', component: 'inputLabelDrop', attribute: {
                label: '票面金额', placeholder: '不限', itemSource: [
                    { displayName: '元', regexp: /^(\d+)$|^(\d+\.\d{1,2})$/, procFunc: e => e },
                    {
                        displayName: '万元', isDefault: true, regexp: /^(\d+)$|^(\d+\.\d{1,6})$/, procFunc: e => {
                            var num = (e + "").toString().split('.');
                            var addZero = function padRight(str, length) {
                                if (str.length >= length)
                                    return str;
                                else
                                    return padRight(str + "0", length);
                            };
                            if (num.length === 1) return +(e + "0000");
                            if (num.length != 2) return 0;

                            if (num[1].length > 4) {
                                return num[0] + num[1].substr(0, 4) + '.' + num[1].substr(4, num[1].length - 1);
                            } else {
                                return num[0] + addZero(num[1], 4);
                            }
                        }
                    }
                ]
            }
        },
        { id: "ssrAp6", vm: 'dueDate', component: 'inputDatePicker', attribute: { label: '到期日期' } },
        {
            id: "ssrAp7", vm: 'price', component: 'inputLabelDrop', attribute: {
                label: '期望价格', placeholder: '电议', itemSource: [
                    {
                        displayName: '‰', regexp: /^(\d+)$|^(\d+\.\d{1,2})$/,  procFunc: e => {
                            // 月利率‰ * 12 = 年利率%
                            var result = (+e) * 1.20;
                            if (result) result = result.toFixed(3);
                            return result;
                        }
                    },
                    { displayName: '%', regexp: /^(\d+)$|^(\d+\.\d{1,3})$/, isDefault: true,procFunc: e => e }
                ]
            }
        },
        { id: "ssrAp8", vm: 'quoteProvinces', component: 'dropLabel', attribute: { label: '交易地点', placeholder: '不限', displayPath: 'name' } },
        { id: "ssrAp9", vm: 'memo', component: 'inputLabel', mutiLine: 'true', attribute: { label: '备注' } },
        // { id: "ssrAp10", vm: '', component: 'inputButtonSelector', attribute: { label: '票面' } },

        { component: 'spiltRow' },


        // { id: "ssrAp11", vm: 'additionalInfo.quoteCompanyName', component: 'inputLabel', attribute: { label: '报价方', isDisabled: true, isRequired: true } },
        // { id: "ssrAp12", vm: 'additionalInfo.contactName', component: 'inputLabel', attribute: { label: '联系人', isDisabled: true, isRequired: true } },
        // { id: "ssrAp13", vm: 'additionalInfo.contactTelephone', component: 'inputLabel', attribute: { label: '联系电话', isDisabled: true, isRequired: true } },
        { id: "ssrAp13", vm: 'joininguser', component: 'addPriceJoininguser', attribute: {} },
        { component: 'tipContent', content: '*报价有效期截止到报价日当天22：00' }
    ],

    addPriceQuickDataDefine: [
        {
            id: "ssrApq1", vm: 'normalOrQuickBoard', component: 'inputButtonSwitcher', attribute:
            { label: '', itemSource: [{ displayName: '普通报价', vmValue: true }, { displayName: '快速报价', vmValue: false, isDefault: true }] }
        }, {
            id: "ssrApq2", vm: 'direction', component: 'inputButtonSelector', attribute:
            { label: '交易方向', isRequired: true, vmType: "Object", itemSource: [{ displayName: '卖出', vmValue: 'OUT', isDefault: true }] }
        }, {
            id: "ssrApq3", vm: 'billTypeMedium', component: 'inputButtonSelector', containerClass: "inline-block", attribute: {
                label: '票据类型', isRequired: true, vmType: "Object", itemSource: [
                    { displayName: '电银', vmValue: { billMedium: 'ELE', billType: 'BKB' }, isDefault: true },
                    { displayName: '纸银', vmValue: { billMedium: 'PAP', billType: 'BKB' } },
                    { displayName: '电商', vmValue: { billMedium: 'ELE', billType: 'CMB' } },
                    { displayName: '纸商', vmValue: { billMedium: 'PAP', billType: 'CMB' } }]
            }
        },
        {
            id: "ssrAp4", vm: 'qptOrAh', component: 'addPriceAcceptinghouseQuotepricetype', default: e => e.code === 'GG', attribute: {
                label: ['承兑行', '承兑方'], qptDisplayPath: 'name', inputFlex: 80, vmType: 'quotePriceType',
            }
        },
        // { id: "ssrApq4", vm: '', component: 'inputButtonSelector', attribute: { label: '票面' } },
        {
            id: "ssrApq5", vm: 'amount', component: 'inputLabelDrop', attribute: {
                label: '票面金额', isRequired: true, itemSource: [
                    { displayName: '元', regexp: /^(\d+)$|^(\d+\.\d{1,2})$/, procFunc: e => e },
                    {
                        displayName: '万元', isDefault: true, regexp: /^(\d+)$|^(\d+\.\d{1,6})$/, procFunc: e => {
                            var num = (e + "").toString().split('.');
                            var addZero = function padRight(str, length) {
                                if (str.length >= length)
                                    return str;
                                else
                                    return padRight(str + "0", length);
                            };
                            if (num.length === 1) return +(e + "0000");
                            if (num.length != 2) return 0;

                            if (num[1].length > 4) {
                                return num[0] + num[1].substr(0, 4) + '.' + num[1].substr(4, num[1].length - 1);
                            } else {
                                return num[0] + addZero(num[1], 4);
                            }
                        }
                    }
                ]
            }
        },
        {
            id: "ssrApq8", vm: 'base64Img', component: 'inputFileSelect', attribute: {
                label: '票面', isRequired: true, labelFlex: 20, inputFlex: 55,
                imgHeight: "100px", imgWidth: "200px", isShowThumb: true,
            }
        },
        { id: "ssrApq6", vm: 'memo', component: 'inputLabel', mutiLine: 'true', attribute: { label: '备注' } },
        { id: "ssrApq7", vm: 'joininguser', component: 'inputHidden', attribute: {} },
        { component: 'tipContent', content: '*报价有效期截止到报价日当天22：00' }
    ],

    viewModelDataDefine: {
        addPrice: {
            // "id": "1", 
            "direction": "direction",
            "billType": "billTypeMedium.billType",
            "billMedium": "billTypeMedium.billMedium",
            "memo": "memo",
            "quoteCompanyDto": { "id": "joininguser.joiningCompanyDto.id" },
            "contactDto": { "id": "joininguser.joiningUserDto.id" },
            "operatorId": "currentUser",
            // "createDate": "", "lastUpdateDate": "", "effectiveDate": "", "expiredDate": "", 
            // 草稿？
            // "quoteStatus": "DFT", 
            // "containsAdditionalInfo": "containsAdditionalInfo",
            // "additionalInfo": {
            //     'quoteCompanyName': 'additionalInfo.quoteCompanyName', 
            //     'contactName': 'additionalInfo.contactName', 
            //     'contactTelephone': 'additionalInfo.contactTelephone',
            // },
            "quoteProvinces": "quoteProvinces",
            "quotePriceType": "qptOrAh.quotePriceType.code",
            // "acceptingHouse": {
            //     "id": "acceptingHouse.id",
            //     "companyType": "acceptingHouse.companyType",
            //     "iamCompanyID": "acceptingHouse.iamCompanyID",
            //     "companyName": "acceptingHouse.companyName",
            //     "address": "acceptingHouse.address",
            //     "manager": "acceptingHouse.manager",
            //     "registrationNumber": "acceptingHouse.registrationNumber",
            //     "companyNamePY": "acceptingHouse.companyNamePY",
            //     "companyNamePinYin": "acceptingHouse.companyNamePinYin",
            //     "fromExternal": "acceptingHouse.fromExternal"
            // },
            "acceptingHouse": "qptOrAh.acceptingHouse",
            "dueDate": "dueDate",
            "price": "price.value",
            "amount": "amount.value",
            "base64Img": "base64Img",

            "quoteToken": "quoteToken"
        },

        editPriod: {
            'direction': e => e.direction,
            'billTypeMedium': e => { return { billType: e.billType, billMedium: e.billMedium }; },
            'qptOrAh': e => {
                var vm = {};
                if (e.acceptingHouse) vm.acceptingHouse = e.acceptingHouse;
                if (e.quotePriceType) vm.quotePriceType = { code: e.quotePriceType };
                return;
            },
            'price': e => e.price,
            'amount': e =>  (e.amount / 10000),
            'dueDate': e => new Date(e.dueDate),
            'quoteProvinces': e => e.quoteProvinces,
            'memo': e => e.memo,
            'joininguser': e => { return { joiningCompanyDto: e.quoteCompanyDto, joiningUserDto: e.contactDto }; }
        },

        addPriceVaildRule: [
            { prop: "amount", displayName: "票面金额", errorMessage: "票面金额格式不正确，输入仅支持数字，万元小数点后六位，元小数点后两位。", param: [] },
            { prop: "amount.value", rule: "rangeClose", displayName: "票面金额", errorMessage: "票面金额超出有效范围。", param: { min: 0.01, max: 9999999999999.99, pattern: 1 } },
            { prop: "price", displayName: "期望价格", errorMessage: "期望价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位。", param: [] },
            { prop: "price.value", rule: "rangeClose", displayName: "期望价格", errorMessage: "期望价格超出有效范围。", param: { min: 0.001, max: 99.999, pattern: 1 } },
            { prop: "memo", rule: "maxLength", displayName: "备注", errorMessage: "备注最多50个字符。", param: 50 }
        ]
    },

    initVaildRuleFunc: (originRule, dialogDataDefine) => {

        var checkFunctionParam = [vm => {
            // 只校验inputString的内容，校验失败的情况下value的值视为无效。 
            if (!vm || !vm.selectedItem) {
                console.error("checkFunctionParam: !vm || !vm.selectedItem");
                return false;
            }

            if (!vm.selectedItem.regexp || vm.selectedItem.regexp.constructor.name !== "RegExp") {
                console.error('checkFunctionParam: !vm.selectedItem.regexp || vm.selectedItem.regexp.constructor.name !== "RegExp"');
                return false;
            }

            return vm.selectedItem.regexp.test(vm.inputString);
        }];

        // 票面金额格式不正确，输入仅支持数字，万元小数点后六位，元小数点后两位。
        originRule.findItem(e => e.prop === 'amount').param = checkFunctionParam;

        // 期望价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位。
        originRule.findItem(e => e.prop === 'price').param = checkFunctionParam;

        var vaildRules = dialogDataDefine.findWhere(e => e.attribute && e.attribute.isRequired)
            .map(e => {
                return {
                    prop: e.vm,
                    displayName: e.attribute.label,
                    rule: 'required',
                    errorMessage: `${e.attribute.label}值不能为空`,
                }
            });

        vaildRules.push.apply(vaildRules, originRule);

        return vaildRules;
    }
};

