const init = Symbol('init');
const openDialogWhenCheckHasValueFailed = Symbol('openDialogWhenCheckHasValueFailed');
const checkPriceHasValueFunc = Symbol('checkPriceHasValueFunc');
const checkPriceVaildValueFunc = Symbol('checkPriceVaildValueFunc');

const dataDefine = {
    addPrice: {},

    managePage: {},

    searchAcceptingHouse: {},
    searchCompanies: {},
    updatePrice: {},
    setStatus: {}
};

class babQuoteService {
    constructor($q, httpService, userService, componentCommonService, commonService, configConsts) {

        this.$q = $q;

        this.httpService = httpService;
        this.userService = userService;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;

        this.configConsts = configConsts;

        this[init]();
    };

    getAuthHeaders() {
        return this.userService.getAuthHeaders();
    };

    // SSR/SSC/NPC
    // 直贴交易/全国直贴报价/全国转帖报价
    //babInitData(pageName) {
    //    pageName = pageName || 'SSR';

    //    var defer = this.$q.defer();

    //    var headers = this.getAuthHeaders();

    //    this.httpService.postService(this.configConsts.bab_init_data, { babQuoteType: pageName }, headers).then(res => {
    //        if (res && res.result instanceof Array && res.result.length === 1) res = res.result[0];
    //        defer.resolve(res);
    //    }, res => {
    //        defer.reject(res);
    //    });

    //    return defer.promise;
    //};

    babInitData(pageName) {
        pageName = pageName || 'SSR';

        // var headers = this.getAuthHeaders();  
        return this.userService.getAuthHeadersAsync().then(headers => {
            if (!headers || !headers.token) return this.$q.resolve({ result: undefined });
            return this.httpService.postService(this.configConsts.bab_init_data, { babQuoteType: pageName }, headers);
        }, res => this.$q.reject(res)).then(res => {
            if (res && res.result instanceof Array && res.result.length === 1) res = res.result[0];
            return this.$q.resolve(res);
        }, res => this.$q.reject(res));
    };

    //babMngInitData(pageName) {
    //    if (!pageName) console.error("babQuoteService.babMngInitData: pageName is Null or undefined.");

    //    var url = dataDefine.managePage.urlMap.get(pageName);

    //    var defer = this.$q.defer();

    //    var headers = this.getAuthHeaders();

    //    this.httpService.postService(url, { babQuoteType: pageName }, headers).then(res => {
    //        if (res && res.result instanceof Array && res.result.length === 1) res = res.result[0];
    //        defer.resolve(res);
    //    }, res => {
    //        defer.reject(res);
    //    });

    //    return defer.promise;
    //};

    babMngInitData(pageName) {
        if (!pageName) console.error("babQuoteService.babMngInitData: pageName is Null or undefined.");
        var url = dataDefine.managePage.urlMap.get(pageName);

        // var headers = this.getAuthHeaders();
        return this.userService.getAuthHeadersAsync().then(headers => { 
            if (!headers || !headers.token) return this.$q.resolve({ result: undefined });
            return this.httpService.postService(url, { babQuoteType: pageName }, headers);
        }, res => this.$q.reject(res)).then(res => {
            if (res && res.result instanceof Array && res.result.length === 1) res = res.result[0];
            return this.$q.resolve(res);
        }, res => this.$q.reject(res));
    };

    babAddprice(type, dto) {

        console.log(`babQuoteService.babAddprice type:${type}`);

        var url = dataDefine.addPrice.urlMap.get(type);

        var headers = this.getAuthHeaders();

        if (dto) {
            if (dto instanceof Array) {
                dto[0].operatorId = headers ? headers.userid : undefined;
            }
        }

        console.debug(dto);

        return this.httpService.postService(url, dto, headers);
    };

    babUpdateprice(type, dto) {

        console.log(`babQuoteService.babUpdateprice type:${type}`);

        var url = dataDefine.updatePrice.urlMap.get(type);

        var headers = this.getAuthHeaders();

        // if (dto) {
        //     dto.operatorId = headers ? headers.userid : undefined;
        // }

        console.debug(dto);

        return this.httpService.postService(url, dto, headers);
    };

    babSetStatus(type, dto) {

        console.log(`babQuoteService.babSetStatus type:${type}`);

        var url = dataDefine.setStatus.urlMap.get(type);

        var headers = this.getAuthHeaders();

        if (dto) {
            if (dto instanceof Array) {
                dto[0].operatorId = headers ? headers.userid : undefined;
            }
        }

        console.debug(dto);

        return this.httpService.postService(url, dto, headers);
    };

    babSearchAcceptingHouse(dto, searchFromYellow) {
        // quoteQuery/searchSSRCompanies

        // AcceptingCompany/searchByNameOrPY
        // AcceptingCompany/searchByNameOrPYFromYellow

        // console.log(`babQuoteService.babSearchAcceptingHouse searchFromYellow:${searchFromYellow}`);

        var url = dataDefine.searchAcceptingHouse.urlMap.get(searchFromYellow ? true : false);

        // console.debug(dto);

        return this.httpService.postService(url, dto, this.getAuthHeaders());
    };

    babInitMngFootCurrent(dto) {

        var fn = dto => {
            var headers = this.getAuthHeaders();

            return this.httpService.postService(this.configConsts.bab_mng_foot_current, dto, headers).then(res => {
                if (res && res.return_code !== 0) {
                    console.error(res);
                    return undefined;
                }

                return res.result;
            }, res => {
                console.error(res);
                return undefined;
            });
        };

        return this.httpService.unique('babQuoteService.babInitMngFootCurrent', dto, fn);

        //    .then(res => {
        //    this.currentItemSource = res;
        //    return res;
        //},res => {
        //    return this.currentItemSource;
        //});
    };

    babInitMngFootTendency(dto) {

        var fn = dto => {
            var headers = this.getAuthHeaders();

            return this.httpService.postService(this.configConsts.bab_mng_foot_tendency, dto, headers).then(res => {
                if (res && res.return_code !== 0) {
                    console.error(res);
                    return undefined;
                }

                return res.result;
            }, res => {
                console.error(res);
                return undefined;
            });
        };

        return this.httpService.unique('babQuoteService.babInitMngFootTendency', dto, fn);

        //    .then(res => {
        //    this.tendencyItemSource = res;
        //    return res;
        //},res => {
        //    return this.tendencyItemSource;
        //});;
    };

    babSearchCompanies(pageName, keyWord) {
        pageName = pageName || "SSR";
        var url = dataDefine.searchCompanies.urlMap.get(pageName);
        return this.httpService.postService(url, keyWord, this.getAuthHeaders());
    };

    babClosingAddpriceDialog(type, params, status) {
        var vaildResult = true;

        if (type === 'SSC') {
            if (params.result.billTypeMedium.billMedium === 'PAP' && params.result.billTypeMedium.billType === 'CMB') {
                let rule = { prop: ["prices.ybhPrice.value", "prices.wbhPrice.value"], rule: "requiredAny", displayName: "", errorMessage: "有保函、无保函价格不能同时为空", param: [] };
                params.dataDefine.addPriceVaildRule.push(rule);
            } else {
                let rule = { prop: ["prices.ggPrice.value", "prices.csPrice.value", "prices.nsPrice.value", "prices.nxPrice.value", "prices.nhPrice.value", "prices.czPrice.value", "prices.wzPrice.value", "prices.cwPrice.value"], rule: "requiredAny", displayName: "", errorMessage: "8种价格不能同时为空", param: [] };
                params.dataDefine.addPriceVaildRule.push(rule);
            }
        }

        if (type === 'NPC') {
            let rule = { prop: ["prices.ggPrice.value", "prices.csPrice.value", "prices.nsPrice.value", "prices.nxPrice.value", "prices.nhPrice.value", "prices.czPrice.value", "prices.wzPrice.value", "prices.cwPrice.value"], rule: "requiredAny", displayName: "", errorMessage: "8种价格不能同时为空", param: [] };
            params.dataDefine.addPriceVaildRule.push(rule);
        }

        if (type === 'SSR') {
            let now = new Date().getTime();
            let maxDate = params.result.billTypeMedium.billMedium === "PAP" ? (now + 190 * 24 * 60 * 60 * 1000) : (now + 390 * 24 * 60 * 60 * 1000);
            let message = params.result.billTypeMedium.billMedium === "PAP" ? "纸票到期日不能超过6个月" : "电票到期日不能超过12个月"
            let rule = { prop: "dueDate", rule: "rangeClose", displayName: "", errorMessage: message, param: { min: (now - 24 * 60 * 60 * 1000), max: maxDate, pattern: 1 } };
            params.dataDefine.addPriceVaildRule.push(rule);

            let billType = params.result.billTypeMedium.billType === 'CMB';
            let rul = { prop: "qptOrAh.acceptingHouse.id", rule: "required", displayName: "", errorMessage: "商票承兑方填写有误" };
            !!billType ? params.dataDefine.addPriceVaildRule.push(rul) : 1;
        }
        params.dataDefine.addPriceVaildRule.forEach(rule => {
            if (!vaildResult) return;

            vaildResult = this.commonService.checkViewModel(params.result, rule, () => {
                this.componentCommonService.openErrorDialog({
                    title: '错误',
                    message: rule.errorMessage,
                    theme: params.theme
                });
            });
        });

        if (!vaildResult) {
            return this.$q.reject(false);
        }

        console.debug(params.result);

        var dto = this.commonService.getDto(params.result, params.dataDefine.addPrice);

        //时间处理
        if (dto.dueDate && dto.dueDate.constructor.name === 'Date') dto.dueDate = dto.dueDate.getTime();
        if (dto.effectiveDate && dto.effectiveDate.constructor.name === 'Date') dto.effectiveDate = dto.effectiveDate.getTime();

        if (status) {
            dto.quoteStatus = status;
        } else {
            if (dto.effectiveDate) {
                var paramDate = new Date(dto.effectiveDate);
                var curDate = new Date();
                if (paramDate.getYear() === curDate.getYear() &&
                    paramDate.getMonth() === curDate.getMonth() &&
                    paramDate.getDate() === curDate.getDate()) {
                    dto.quoteStatus = "DSB";
                } else {
                    dto.quoteStatus = "DFT"
                }
            } else {
                dto.quoteStatus = "DFT";
            }
        }

        //特殊处理，省code的赋值
        if (dto.quoteProvinces) {
            if (dto.quoteProvinces.code) {
                dto.quoteProvinces.provinceCode = dto.quoteProvinces.code;
            } else {
                dto.quoteProvinces = undefined;
            }
        }

        this.commonService.deleteEmptyProperty(dto);

        return this.babAddprice(type, [dto]).then(res => {
            return true;
        }, res => {
            let message;
            if (res.return_message && res.return_message.exceptionCode && res.return_message.exceptionMessage) {
                message = res.return_message.exceptionName + '，' + res.return_message.exceptionMessage;
            }
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: message || '报价失败',
                theme: params.theme
            });

            return false;
        });
    };

    babClosingEditpriceDialog(type, params) {

        var vaildResult = true;

        if (type === 'SSR') {
            let now = new Date().getTime();
            let maxDate = params.result.billTypeMedium.billMedium === "PAP" ? (now + 190 * 24 * 60 * 60 * 1000) : (now + 390 * 24 * 60 * 60 * 1000);
            let message = params.result.billTypeMedium.billMedium === "PAP" ? "纸票到期日不能超过6个月" : "电票到期日不能超过12个月"
            let rule = { prop: "dueDate", rule: "rangeClose", displayName: "SSR到期日", errorMessage: message, param: { min: (now - 24 * 60 * 60 * 1000), max: maxDate, pattern: 1 } };
            params.dataDefine.addPriceVaildRule = params.dataDefine.addPriceVaildRule.findWhere(e => e.displayName !== "SSR到期日");
            params.dataDefine.addPriceVaildRule.push(rule);

            let billType = params.result.billTypeMedium.billType === 'CMB';
            let rul = { prop: "qptOrAh.acceptingHouse.id", rule: "required", displayName: "商票承兑方", errorMessage: "商票承兑方填写有误" };
            params.dataDefine.addPriceVaildRule = params.dataDefine.addPriceVaildRule.findWhere(e => e.displayName !== "商票承兑方");
            !!billType ? params.dataDefine.addPriceVaildRule.push(rul) : 1;
        }

        params.dataDefine.addPriceVaildRule.forEach(rule => {
            if (!vaildResult) return;

            vaildResult = this.commonService.checkViewModel(params.result, rule, () => {
                this.componentCommonService.openErrorDialog({
                    title: '错误',
                    message: rule.errorMessage,
                    theme: params.theme
                });
            });
        });

        if (!vaildResult) {
            return this.$q.reject(false);
        }

        console.debug(params.originItem);
        debugger;
        for(var prop in params.result){
            if(params.result[prop] === undefined &&　params.originItem.hasOwnProperty(prop) && params.originItem[prop] !== undefined){
                params.originItem[prop] = undefined;
            }
        }

        var dto = this.commonService.getDto(params.result, params.dataDefine.addPrice);

        if (params.originItem.containsAdditionalInfo) {
            ['contactDto', 'quoteCompanyDto'].forEach(e => { delete dto[e]; });
        };

        //时间处理
        if (dto.dueDate && dto.dueDate.constructor.name === 'Date') dto.dueDate = dto.dueDate.getTime();
        if (dto.effectiveDate && dto.effectiveDate.constructor.name === 'Date') dto.effectiveDate = dto.effectiveDate.getTime();

        //特殊处理，省code的赋值
        if (dto.quoteProvinces) {
            if (dto.quoteProvinces.code) {
                dto.quoteProvinces.provinceCode = dto.quoteProvinces.code;
            }
            else {
                dto.quoteProvinces = undefined;
            }
        }

        dto = angular.merge(angular.copy(params.originItem), dto);

        this.commonService.deleteEmptyProperty(dto);


        if (type === 'SSR') {
            ['createDate', 'effectiveDate', 'expiredDate', 'lastUpdateDate'].forEach(e => { delete dto[e]; });
        }
        else {
            ['createDate', 'expiredDate', 'lastUpdateDate'].forEach(e => { delete dto[e]; });
        }

        return this.babUpdateprice(type, dto).then(res => {
            if (res && res.return_code !== 0) {
                this.componentCommonService.openErrorDialog({
                    title: return_message && return_message.exceptionName || '错误',
                    message: return_message && return_message.exceptionMessage || '报价失败',
                    theme: params.theme
                });

                console.error(res);
                return false;
            }

            return true;
        }, res => {
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: '报价失败',
                theme: params.theme
            });

            return false;
        });
    };

    babConvertPriceForSscVm(result, priceVmDefine) {
        if (!result) return;

        if (result.prices) {
            var vmDefine = priceVmDefine.get(result.unit);

            if (vmDefine) {
                for (var prop in result.prices) {
                    if (!result.prices.hasOwnProperty(prop)) continue;

                    if (result.prices[prop] && result.prices[prop].inputValue) {

                        result.prices[prop].value = vmDefine.procFunc(result.prices[prop].inputValue);
                    }
                    else{
                        result.prices[prop].value = "";
                    }
                }
            }
        }
    };

    babConvertPriceForSsrVm(result) {
        if (!result) return;

        if (result.prices) {
            for (var prop in result.prices) {
                if (!result.prices.hasOwnProperty(prop)) continue;

                if (result.prices[prop] && result.prices[prop].inputValue) {
                    result.prices[prop].value = +result.prices[prop].inputValue;
                }
            }
        }
    };

    [openDialogWhenCheckHasValueFailed](theme) {
        this.componentCommonService.openErrorDialog({
            title: '错误',
            message: '没有包含有效报价或者报价格式不正确',
            theme: theme || 'default',
        });
    };

    [checkPriceHasValueFunc](vm, prop) {
        var value = this.commonService.getPropertyX(vm, prop);
        if (value && value !== 0 && parseFloat(value.value)) return true;
    };

    checkPriceVaildValueFunc(vm, prop, theme) {
        var value = this.commonService.getPropertyX(vm, prop);
        if (!value) return undefined;
        if(value.value === 0){
            vm[prop] = undefined;
            return undefined;
        }
        if (parseFloat(value.value) < 0.012 || parseFloat(value.value) > 24) {
            var defer = this.$q.defer();

            this.componentCommonService.openConfirmDialog(undefined, {
                title: '',
                message: '您的价格已偏离市场，是否继续报单',
                theme: theme || 'default',
                okCallback: () => {
                    defer.resolve(true);
                },
                cancelCallback: () => {
                    defer.resolve(false);
                }
            });

            return defer.promise;
        }

        return undefined;
    };

    checkViewModelPrices(vm, defineArray, theme) {
        var defer = this.$q.defer();

        // var defineArray = ["prices.ggPrice", "prices.csPrice", "prices.nsPrice", "prices.nxPrice", "prices.nhPrice", "prices.czPrice", "prices.wzPrice", "prices.cwPrice"];

        defineArray.forEach(item => {
            if (defer.promise.$$state.status === 1) return;

            if (this[checkPriceHasValueFunc](vm, item)) {
                defer.resolve(defineArray);
            }
        });

        if (defer.promise.$$state.status === 0) {
            defer.reject(false);
        }

        return defer.promise.then(res => {
            if (!res) return this.$q.reject(false);

            var promise = undefined;
            res.forEach(item => {
                if (promise) return;
                promise = this.checkPriceVaildValueFunc(vm, item, theme);
            });

            return promise || this.$q.resolve(true);
        }, res => {
            this[openDialogWhenCheckHasValueFailed](theme);
            return this.$q.reject(false);
        });
    };

    [init]() {
        console.debug('babQuoteService initialized.');

        dataDefine.addPrice.urlMap = new Map([
            ['SSR', this.configConsts.bab_insert_ssr],
            ['SSC', this.configConsts.bab_insert_ssc],
            ['NPC', this.configConsts.bab_insert_npc]
        ]);

        dataDefine.managePage.urlMap = new Map([
            ["SSR", this.configConsts.bab_ssrmng_init],
            ["SSC", this.configConsts.bab_sscmng_init],
            ["NPC", this.configConsts.bab_npcmng_init],
        ]);

        dataDefine.searchAcceptingHouse.urlMap = new Map([
            [false, this.configConsts.acceptinghouse_search],
            [true, this.configConsts.acceptinghouse_search_from_yellow],
        ]);

        dataDefine.searchCompanies.urlMap = new Map([
            ["SSR", this.configConsts.bab_company_ssr],
            ["SSC", this.configConsts.bab_company_ssc],
            ["NPC", this.configConsts.bab_company_npc],
        ]);

        dataDefine.updatePrice.urlMap = new Map([
            ["SSR", this.configConsts.bab_update_ssr],
            ["SSC", this.configConsts.bab_update_ssc],
            ["NPC", this.configConsts.bab_update_npc],
        ]);

        dataDefine.setStatus.urlMap = new Map([
            ["SSR", this.configConsts.bab_setstatus_ssr],
            ["SSC", this.configConsts.bab_setstatus_ssc],
            ["NPC", this.configConsts.bab_setstatus_npc],
        ]);
    };
};

export default ['$q', 'httpService', 'userService', 'componentCommonService', 'commonService', 'configConsts', babQuoteService];