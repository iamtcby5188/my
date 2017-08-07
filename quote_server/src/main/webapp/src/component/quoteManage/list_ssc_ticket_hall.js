import addPriceSscCtrl from './controller/addPriceSscCtrl';

const initView = Symbol('initView');
const setChartVmQuotePriceType = Symbol('setChartVmQuotePriceType');
const changeFootToCurrent = Symbol('changeFootToCurrent');
const changeFootToTendency = Symbol('changeFootToTendency');

const dataDefine = {

    searchCriteriaDataDefine: [
        {
            id: "ssc1",
            vm: 'billTypeMedium',
            conditionName: '票据类型',
            component: 'inputButtonSelector',
            containerClass: "inline-block",
            default: e => e.code === 'PAP_BKB',
            attribute: { label: '票据类型', displayPath: 'name', vmType: "Object" }
        },
        { id: "ssc2", vm: 'provinceCodes', conditionName: '交易地点', component: 'inputDropdownChips', attribute: { label: '交易地点', displayPath: 'name' } },

        ],

    priceVmDefine: new Map([
        ["percent", { unit: '%', regexp: /^(\d+)$|^(\d+\.\d{1,3})$/, procFunc: e => e }],
        ["millesimal", {
            unit: '‰', regexp: /^(\d+)$|^(\d+\.\d{1,2})$/, isDefault: true, procFunc: e => {
                // 月利率‰ * 12 = 年利率%
                var result = (+e) * 1.20;
                if (result) result = result.toFixed(3);
                return result;
            }
        }]
    ]),

    footDefine: {
        titles: [
            { displayName: '当日价格概览', value: 'current', isDefault: true },
            { displayName: '近期价格走势', value: 'tendency' }
        ],

        current: {
            qptItemSource: {

            }
        },

        quotePriceTypeDefaultValue: {
            other: e => e.code === 'GG',
            CMB: e => e.code === 'YBH'
        },

        billTypeMediumMap: new Map([
            ['PAP_BKB', { billMedium: 'PAP', billType: 'BKB', minor: false }],
            ['ELE_BKB', { billMedium: 'ELE', billType: 'BKB', minor: false }],
            ['MINOR_PAP_BKB', { billMedium: 'PAP', billType: 'BKB', minor: true }],
            ['MINOR_ELE_BKB', { billMedium: 'ELE', billType: 'BKB', minor: true }],
            ['CMB', { billMedium: 'PAP', billType: 'CMB', minor: false }]
        ])
    }
};


class listSscTicketHallCtrl {

    constructor($scope, babQuoteService, componentCommonService, commonService, userService, babSscConsts, configConsts, $q, websocketService, gridColumnFactoryService, gridExcelImportService) {
        this.$scope = $scope;

        this.babQuoteService = babQuoteService;
        this.websocketService = websocketService;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;
        this.userService = userService;
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.gridExcelImportService = gridExcelImportService;

        this.babSscConsts = babSscConsts;
        this.configConsts = configConsts;
        this.$q = $q;

        this[initView]();
    };

    addPrice(event) {

        this.userService.getToken('bab.ssc').then(res => {
            console.debug(`listSscTicketHallCtrl initView. get token: ${res.result[0]}`);
            return this.$q.resolve(res.result[0]);
        }, res => this.$q.reject(res)).then(token => {
            // this.openAddQuote(dataDefine.addPriceDataDefine, event, res);

            return this.componentCommonService.openAddPriceDialog(addPriceSscCtrl, {
                itemSource: dataDefine.addPriceDataDefine,
                theme: this.theme,
                title: '报价',
                event: event,
                button: '.animation-target-addprice',
                onClosing: result => {

                    this.babQuoteService.babConvertPriceForSscVm(result, dataDefine.priceVmDefine);

                    var defineArray = undefined

                    if (result.billTypeMedium && result.billTypeMedium.billType === 'CMB') {
                        defineArray = ["prices.ybhPrice", "prices.wbhPrice"];
                    } else {
                        defineArray = ["prices.ggPrice", "prices.csPrice", "prices.nsPrice", "prices.nxPrice", "prices.nhPrice", "prices.czPrice", "prices.wzPrice", "prices.cwPrice"];
                    }

                    return this.babQuoteService.checkViewModelPrices(result, defineArray, this.theme).then(res => {
                        if (res) {
                            result.quoteToken = token;
                            return this.babQuoteService.babClosingAddpriceDialog('SSC', {
                                theme: this.theme,
                                result: result,
                                dataDefine: angular.copy(dataDefine.viewModelDataDefine)
                            });
                        } else {
                            return this.$q.reject(false);
                        }
                    }, res => this.$q.reject(false));
                },
                onVmChanged: result => { }
            });
        }, res => {
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: '初始化报价表单失败',
                theme: this.theme
            });
            return this.$q.reject(res);
        }).then(ref => {
            this.addPricePanelRef = ref;
        }, answer => {
            console.debug(answer);
        });
    };

    onClickImport(event) {
        this.gridExcelImportService.openImportDialog(undefined, { type: 'ssc' }, this.$scope.initData).then(e => {
            this.mdPanelRef = e;
        });;
    }

    [initView]() {
        console.debug('listSscTicketHallCtrl initView');

        dataDefine.addPriceDataDefine = angular.copy(this.babSscConsts.addPriceDataDefine);
        dataDefine.viewModelDataDefine = angular.copy(this.babSscConsts.viewModelDataDefine);

        this.$scope.vm = {};

        this.chartVm = {};

        this.footTitles = dataDefine.footDefine.titles;

        this.$scope.networkState = this.websocketService.networkState;
        this.$scope.isReconnect = this.websocketService.isReconnect;
        this.$scope.$on('networkStateChange', (event, newValue) => {
            this.$scope.networkState = newValue;
        });
        this.$scope.$on('isReconnectChange', (event, newValue) => {
            this.$scope.isReconnect = newValue;
        });
    };

    $onInit() {
        let userInfo = this.userService.getUserInfo();
        if (userInfo && userInfo.info && userInfo.info['bab.quote.batchimport']) {
            this.importAuth = userInfo.info['bab.quote.batchimport'] === 'true';
        }

        // 获取初始化数据
        this.babQuoteService.babInitData('SSC').then(res => {
            this.$scope.initData = angular.copy(res);

            var initData = this.$scope.initData;

            // initSearchCriteriaDataDefine
            if (res.parameterList) {
                this.$scope.vm.searchCriteriaDefine = this.componentCommonService.createSearchCriteriaVm({
                    source: res.parameterList,
                    dataDefine: dataDefine.searchCriteriaDataDefine,
                    defineMatchFunc: (item, dataDefine) => item && item.conditionName && dataDefine && dataDefine.conditionName && item.conditionName === dataDefine.conditionName
                });
            }

            if (res.parameterList && res.parameterList instanceof Array) {

                dataDefine.footDefine.quotePriceTypeIndexMap = new Map();

                // init foot chartVm
                [
                    {
                        targetMatcher: e => e.conditionName === "承兑行类别", sourceProp: 'other', targetProp: 'conditions',
                        default: dataDefine.footDefine.quotePriceTypeDefaultValue.other
                    },
                    {
                        targetMatcher: e => e.conditionName === "保函", sourceProp: 'CMB', targetProp: 'conditions',
                        default: dataDefine.footDefine.quotePriceTypeDefaultValue.CMB
                    },
                ].forEach(item => {
                    var target = res.parameterList.findItem(item.targetMatcher);

                    if (target) {
                        if (!this.commonService.getPropertyX(this, 'chartVm.qptItemSource')) this.commonService.setPropertyX(this, 'chartVm.qptItemSource', {});
                        let tempSource = target[item.targetProp];
                        tempSource.forEach((e, i) => {
                            e.$id = i;
                            e.isDefault = item.default && typeof item.default === 'function' && item.default(e);
                            dataDefine.footDefine.quotePriceTypeIndexMap.set(e.code, {
                                $displayName: e.name,
                                $displayOrder: i
                            });
                        });
                        this.chartVm.qptItemSource[item.sourceProp] = tempSource;
                    }
                });

                if (!this.chartVm.qptItemSource) console.error('No 承兑行类别 保函 data in this.babQuoteService.babInitData("SSC")');

                // init Addprice
                [
                    {
                        targetMatcher: e => e.conditionName === "交易地点",
                        sourceMatcher: e => e.vm === "quoteProvinces",
                        sourceProp: 'conditions'
                    }
                ].forEach((item, index) => {
                    var target = res.parameterList.findItem(item.targetMatcher);
                    if (target) {
                        var define = dataDefine.addPriceDataDefine.findItem(item.sourceMatcher);

                        if (define) {
                            define.attribute.itemSource = angular.copy(this.commonService.getPropertyX(target, item.sourceProp));
                            define.attribute.itemSource.forEach(e => { e.isDefault = define.default && typeof define.default === "function" && define.default(e); })
                        }
                    }
                });
            }

            var userInfo = this.userService.getUserInfo();

            if (userInfo.info && userInfo.info.joiningUser && userInfo.info.joiningUser instanceof Array) {
                var define = dataDefine.addPriceDataDefine.findItem(e => e.vm === "joininguser");

                if (define) {
                    define.attribute.itemSource = angular.copy(this.commonService.getPropertyX(userInfo.info, "joiningUser"));
                    define.attribute.defaultValue = define.attribute.itemSource[0];
                }
            }

        }, res => {
            console.error('listSscTicketHallCtrl babQuoteService.babInitData.');

            if (res && res.return_message && res.return_message.exceptionCode === "E8888") {
                this.componentCommonService.openErrorDialog({
                    title: res.return_message.exceptionName,
                    message: res.return_message.exceptionMessage,
                    theme: this.theme
                });

                this.$router.navigateByUrl('../login');
            } else {
                this.componentCommonService.openErrorDialog({
                    title: '错误',
                    message: '初始化数据获取失败',
                    theme: this.theme
                });
            }
        });
    };

    onSearchCriteriaChanged() {

        this.chartVm.billTypeMedium = dataDefine.footDefine.billTypeMediumMap.get(this.commonService.getPropertyX(this, '$scope.vm.searchCriteria.billTypeMedium.code'));

        if (this.chartVm.billTypeMedium) this[setChartVmQuotePriceType]();

        this[changeFootToCurrent]();
        this[changeFootToTendency]();
    };

    onSelectChartPage() {
        this.gridColumnFactoryService.resizeGrid('panel-title-container');
        this[setChartVmQuotePriceType]();
        this[changeFootToTendency]();
    };

    [setChartVmQuotePriceType]() {
        if (this.chartVm.billTypeMedium.billType === 'CMB') {
            if (this.oldBillTypeValue !== 'CMB' || !this.chartVm.quotePriceType) {
                if (this.chartVm.qptItemSource.CMB instanceof Array) {
                    this.chartVm.quotePriceType = this.chartVm.qptItemSource.CMB.findItem(dataDefine.footDefine.quotePriceTypeDefaultValue.CMB);
                }
            }
        } else {
            if (this.oldBillTypeValue === 'CMB' || !this.chartVm.quotePriceType) {
                if (this.chartVm.qptItemSource.other instanceof Array) {
                    this.chartVm.quotePriceType = this.chartVm.qptItemSource.other.findItem(dataDefine.footDefine.quotePriceTypeDefaultValue.other);
                }
            }
        }

        this.oldBillTypeValue = angular.copy(this.chartVm.billTypeMedium.billType);
    };

    [changeFootToCurrent]() {

        if (!this.chartVm.billTypeMedium) return;

        this.babQuoteService.babInitMngFootCurrent({
            "quoteType": "SSC",
            "billMedium": this.chartVm.billTypeMedium.billMedium,
            "billType": this.chartVm.billTypeMedium.billType,
            "minorFlag": this.chartVm.billTypeMedium.minor,
        }).then(res => {
            if (res instanceof Array && dataDefine.footDefine.quotePriceTypeIndexMap) {
                res.forEach(e => { angular.merge(e, dataDefine.footDefine.quotePriceTypeIndexMap.get(e.quotePriceType)); });
                this.chartVm.currentItemSource = res;
            }
        }, res => { });
    };

    [changeFootToTendency]() {

        if (!this.chartVm.billTypeMedium) return;

        this.babQuoteService.babInitMngFootTendency({
            "quotePriceType": this.chartVm.quotePriceType.code,
            "billMedium": this.chartVm.billTypeMedium.billMedium,
            "billType": this.chartVm.billTypeMedium.billType,
            "quoteType": "SSC",
            "minorFlag": this.chartVm.billTypeMedium.minor,
        }).then(res => {
            this.chartVm.chartData = res;
        }, res => { });
    };

    $routerOnActivate(currentInstruction, previousInstruction) {
        if (currentInstruction) {
            if (currentInstruction.routeData && currentInstruction.routeData.data) {
                this.theme = currentInstruction.routeData.data.theme;
            }
        }
    };

    doSearch(content) {
        if (!content) {
            if (this.searchObj) {
                if (this.searchObj.memo) {
                    this.searchObj.memo = undefined;
                }
                if (this.searchObj.id) {
                    this.searchObj.id = undefined;
                }
            }
        }
        var def = this.$q.defer();
        if (!content) {
            def.reject();
            return def.promise;
        }

        this.babQuoteService.babSearchCompanies("SSC", content).then(res => {
            if (!res || !res.result) {
                def.reject();
            }
            def.resolve(res.result);
        }, res => {
            def.reject();
        })
        return def.promise;
    };

    onClickGridRefresh() {
        this.$scope.$broadcast('gridDataRefresh');
        this.websocketService.isReconnect = false;
        this.$scope.isReconnect = false;
    };
}

let listSscTicketHall = () => {
    return {
        template: require('./template/list_ssc_ticket_hall.html'),
        bindings: {
            theme: '@',
            $router: '<'
        },
        controller: ['$scope', 'babQuoteService', 'componentCommonService', 'commonService', 'userService', 'babSscConsts', 'configConsts', '$q', 'websocketService', 'gridColumnFactoryService', 'gridExcelImportService', listSscTicketHallCtrl]
    }
};

export default listSscTicketHall;