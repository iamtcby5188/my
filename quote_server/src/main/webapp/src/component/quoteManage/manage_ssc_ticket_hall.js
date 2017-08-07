/**
 * Created by yu.bian on 2017/1/10.
 */
import addPriceSscCtrl from './controller/addPriceSscCtrl';
import quotOperConfirmCtrl from './../../controller/quotOperConfirm';

const initView = Symbol('initView');
const checkViewModelPrices = Symbol('checkViewModelPrices');
const setChartVmQuotePriceType = Symbol('setChartVmQuotePriceType');
const changeFootToCurrent = Symbol('changeFootToCurrent');
const changeFootToTendency = Symbol('changeFootToTendency');
const onAuthFailed = Symbol('onAuthFailed');
const dataDefine = {
    // id 不可更改
    searchSscmngDataDefine: [
        {
            id: "sscMng1", typeName: "sscBillType", conditionName: '票据类型', component: "barNav", containerClass: "inline-block", flex: "20", bShow: true,
            default: e => e.code === 'PAP_BKB', attribute: { displayPath: 'name' }
        },
        {
            id: "sscMng2", typeName: "sscAddQuot", component: "mdButton", displayName: "新增报价", containerClass: "inline-block animation-target-addprice",
            flex: "20", containerAlien: "alien-right", bShow: true,
        },
        { id: "sscMng3", typeName: "sscImport", component: "mdButton", displayName: "批量导入", containerClass: "inline-block import", flex: "20", containerAlien: "alien-right", bShow: true },
        {
            id: "sscMng6", typeName: "sscDate", vm: "date", component: "inputDatePickerRange", containerClass: "inline-block", flex: "50", containerAlien: "alien-right", bShow: true,
            attribute: { label: "发价日期", flex: { label: 30, start: 35, end: 35 } }
        }
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

class manageSscTicketHallCtrl {
    constructor($scope, babQuoteService, userService, componentCommonService, commonService, babSscConsts, $q, gridColumnFactoryService, gridExcelImportService) {
        this.$scope = $scope;
        this.babQuoteService = babQuoteService;
        this.userService = userService;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;
        this.babSscConsts = babSscConsts;
        this.$q = $q;
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.gridExcelImportService = gridExcelImportService;
        this[initView]();
    }

    [initView]() {
        console.debug('manageSscTicketHallCtrl initView');

        dataDefine.addPriceDataDefine = angular.copy(this.babSscConsts.addPriceDataDefine);
        dataDefine.viewModelDataDefine = angular.copy(this.babSscConsts.viewModelDataDefine);

        this.$scope.vm = {};

        this.chartVm = {};

        this.footTitles = dataDefine.footDefine.titles;
    };

    click(evt) {
    };

    onTabChanged(tabCode) {

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

    onSearchCriteriaChanged() {
        if (this.$scope.vm.searchPanelData instanceof Array) {

            var condition = this.$scope.vm.searchPanelData.findItem(e => e.conditionName === '票据类型');

            if (condition) {
                this.chartVm.billTypeMedium = dataDefine.footDefine.billTypeMediumMap.get(this.commonService.getPropertyX(condition, 'attribute.ngModel.code'));

                if (this.chartVm.billTypeMedium) {
                    this[setChartVmQuotePriceType]();
                }
            }

            condition = this.$scope.vm.searchPanelData.findItem(e => e.vm === 'date');

            if (condition) {
                this.chartVm.startDate = this.commonService.getPropertyX(condition, 'attribute.ngModel.start');
                if (this.chartVm.startDate) this.chartVm.startDate = this.chartVm.startDate.getTime();
                this.chartVm.endDate = this.commonService.getPropertyX(condition, 'attribute.ngModel.end');
                if (this.chartVm.endDate) this.chartVm.endDate = this.chartVm.endDate.getTime();
            }
        }

        this[changeFootToCurrent]();
        this[changeFootToTendency]();
    };

    onBtnClick(event, define) {

        switch (define.typeName) {
            case "sscAddQuot":

                this.userService.getToken('bab.ssc').then(res => {
                    console.debug(`manageSscTicketHallCtrl initView. get token: ${res.result[0]}`);
                    return this.$q.resolve(res.result[0]);
                }, res => this.$q.reject(res)).then(res => {
                    // this.addPrice(dataDefine.addPriceDataDefine, event);
                    this.addPrice(event, res);
                }, res => {
                    this.componentCommonService.openErrorDialog({
                        title: '错误',
                        message: '初始化报价表单失败',
                        theme: this.theme
                    });
                });

                break;
            case "sscImport":
                this.gridExcelImportService.openImportDialog(undefined, { type: 'ssc' }, this.$scope.initData).then(e => {
                    this.mdPanelRef = e;
                });;
                break;
            default: break;
        };
    };

    addPrice(event, token) {

        dataDefine.viewModelDataDefine.addPriceVaildRule = this.babSscConsts.viewModelDataDefine.initVaildRuleFunc(dataDefine.viewModelDataDefine.addPriceVaildRule);
        this.componentCommonService.openAddPriceDialog(addPriceSscCtrl, {
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
                        }, "DFT").then(res => {
                            if (res) {
                                this.$scope.$broadcast("gridManageSscTicketHall");
                                return this.$q.resolve(true);
                            } else {
                                return this.$q.reject(false);
                            }
                        }, res => this.$q.reject(false));
                    } else {
                        return this.$q.reject(false);
                    }
                }, res => this.$q.reject(false));

            },
            onVmChanged: result => { }
        }).then(ref => {
            this.addPricePanelRef = ref;
            return this.$q.resolve(true);
        }, answer => {
            console.debug(answer);
            return this.$q.reject(false);
        });
    };

    onEditListItem(event, item) {

        return this.userService.getToken('bab.sscedit').then(res => {
            console.debug(`manageSscTicketHallCtrl initView. get token: ${res.result[0]}`);
            return this.$q.resolve(res.result[0]);
        }, res => this.$q.reject(res)).then(token => {
            // this.addPrice(dataDefine.addPriceDataDefine, event);
            // this.addPrice(event, res);

            var dialogDataDefine = angular.copy(dataDefine.addPriceDataDefine);

            if (!(dialogDataDefine instanceof Array)) {
                return this.$q.reject(`dialogDataDefine is not array`);
            }

            this.componentCommonService.initEditPriceVm(dialogDataDefine, dataDefine.viewModelDataDefine, item);

            return this.componentCommonService.openAddPriceDialog(addPriceSscCtrl, {
                itemSource: dialogDataDefine,
                theme: this.theme,
                title: '修改报价',
                event: event,
                button: '.animation-target-addprice',
                onClosing: result => {
                    this.babQuoteService.babConvertPriceForSscVm(result, dataDefine.priceVmDefine);
                    var defineArray = undefined;

                    if (result.billTypeMedium && result.billTypeMedium.billType === 'CMB') {
                        defineArray = ["prices.ybhPrice", "prices.wbhPrice"];
                    } else {
                        defineArray = [
                            "prices.ggPrice", "prices.csPrice", "prices.nsPrice", "prices.nxPrice", "prices.nhPrice", "prices.czPrice", "prices.wzPrice", "prices.cwPrice"
                        ];
                    }

                    return this.babQuoteService.checkViewModelPrices(result, defineArray, this.theme).then(res => {
                        if (res) {
                            result.quoteToken = token;
                            return this.babQuoteService.babClosingEditpriceDialog('SSC', {
                                theme: this.theme,
                                result: result,
                                dataDefine: dataDefine.viewModelDataDefine,
                                originItem: item
                            }).then(res => {
                                if (res) {
                                    this.$scope.$broadcast("gridManageSscTicketHall");
                                    return this.$q.resolve(true);
                                } else {
                                    return this.$q.reject(false);
                                }
                            }, res => this.$q.reject(false));
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
        }).then(ref => {
            this.addPricePanelRef = ref;
        }, answer => {
            console.debug(answer);
        });
    };

    onPublishListItem(event, item) {
        var def = this.$q.defer();
        var curTime = new Date().getTime();
        if (curTime < item.effectiveDate) {
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: '不允许发布非当天报价',
                theme: this.theme
            });
            def.reject();
            return def.promise;
        }
        this.componentCommonService.openConfirmDialog(quotOperConfirmCtrl, {
            title: "发布确认",
            message: "请再次确认发布该笔价格，确认后价格将实时推送至全市场？",
            quotContent: "",
            theme: this.theme,
            okCallback: e => {
                def.resolve(this.babQuoteService.babSetStatus("SSC", { "ids": [item.id], "status": "DSB" }));
            },
            cancelCallback: e => {
                def.reject();
            }
        })

        return def.promise;
    };

    onDelListItem(event, item) {
        var def = this.$q.defer();
        this.componentCommonService.openConfirmDialog(quotOperConfirmCtrl, {
            title: "删除确认",
            message: "请再次确认是否删除该笔价格？",
            quotContent: "",
            theme: this.theme,
            okCallback: e => {
                def.resolve(this.babQuoteService.babSetStatus("SSC", { "ids": [item.id], "status": "DEL" }));
            },
            cancelCallback: e => {
                def.reject();
            }
        })

        return def.promise;
    };

    onSelectChartPage() {
        this.gridColumnFactoryService.resizeGrid('panel-title-container');
        this[setChartVmQuotePriceType]();
        this[changeFootToTendency]();
    };

    [changeFootToCurrent]() {

        if (!this.chartVm.billTypeMedium) return;

        // if (!this.chartVm.quotePriceType) this.chartVm.quotePriceType = 'GG';

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
        }, res => {
            // debugger;
        });
    };

    [changeFootToTendency]() {

        if (!this.chartVm.quotePriceType) return;

        this.babQuoteService.babInitMngFootTendency({
            "quotePriceType": this.chartVm.quotePriceType.code,
            "billMedium": this.chartVm.billTypeMedium.billMedium,
            "billType": this.chartVm.billTypeMedium.billType,
            "quoteType": "SSC",
            "minorFlag": this.chartVm.billTypeMedium.minor,
            // "startDate": this.chartVm.startDate,
            // "endDate": this.chartVm.endDate
        }).then(res => {
            this.chartVm.chartData = res;
        }, res => {
            // debugger;
        });;
    };

    $onInit() {
        let userInfo = this.userService.getUserInfo();
        if (userInfo && userInfo.info && userInfo.info['bab.quote.batchimport']) {
            this.importAuth = userInfo.info['bab.quote.batchimport'] === 'true';
        }

        this.babQuoteService.babMngInitData("SSC").then(res => {
            this.$scope.initData = angular.copy(res);

            let searchSscmngDataDefine = angular.copy(dataDefine.searchSscmngDataDefine);
            searchSscmngDataDefine.findItem(e=>e.id === 'sscMng3').bShow = this.importAuth;
            if (res.parameterList) {
                this.$scope.vm.searchPanelData = this.componentCommonService.createSearchCriteriaVm({
                    source: res.parameterList,
                    dataDefine: searchSscmngDataDefine,
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

                // init Addprice
                [
                    { targetMatcher: e => e.conditionName === "交易地点", sourceMatcher: e => e.vm === "quoteProvinces", sourceProp: 'conditions' }
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
            console.error('manageSscTicketHallCtrl babQuoteService.babInitData.');

            if (res && res.return_message && res.return_message.exceptionCode === "E8888") {
                this.componentCommonService.openErrorDialog({
                    title: res.return_message.exceptionName,
                    message: res.return_message.exceptionMessage,
                    theme: this.theme
                });

                // this.$router.navigateByUrl('../login?page=ManageSscTicketHall');
            } else {
                this.componentCommonService.openErrorDialog({
                    title: '错误',
                    message: '初始化数据获取失败',
                    theme: this.theme
                });
            }
        });
    };

    $routerOnActivate(currentInstruction, previousInstruction) {

        if (currentInstruction) {
            if (currentInstruction.routeData && currentInstruction.routeData.data) {
                this.theme = currentInstruction.routeData.data.theme;
            }
        }

        var userInfo = this.userService.getUserInfo();

        if (userInfo) {
            var canActive = userInfo.info["bab.quote.ssr.management"];

            if (!canActive || canActive !== "true") {
                this[onAuthFailed]();
            }
        }
    };

    [onAuthFailed]() {
        this.isAuthFailed = true;

        // this.componentCommonService.openErrorDialog({
        //     title: "认证失败",
        //     message: "用户无直贴价格管理浏览权限！",
        //     theme: this.theme
        // });

        this.$router.navigateByUrl('../login');
    };
}

let manageSscTicketHall = () => {
    return {
        template: require('./template/manage_ssc_ticket_hall.html'),
        bindings: {
            theme: '@mdTheme',
            $router: '<'
        },
        controller: ['$scope', 'babQuoteService', 'userService', 'componentCommonService', 'commonService', 'babSscConsts', '$q', 'gridColumnFactoryService', 'gridExcelImportService', manageSscTicketHallCtrl]
    }
};

export default manageSscTicketHall;