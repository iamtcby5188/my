/**
 * Created by jiannan.niu on 2016/12/30.
 */
import addPriceNpcCtrl from './controller/addPriceNpcCtrl';
import quotOperConfirmCtrl from './../../controller/quotOperConfirm';

const initView = Symbol('initView');
const changeFootToCurrent = Symbol('changeFootToCurrent');
const changeFootToTendency = Symbol('changeFootToTendency');
const onAuthFailed = Symbol('onAuthFailed');

const dataDefine = {
    searchNpcmngDataDefine: [
        {
            id: "npcMng1", typeName: "npcBillMedium", conditionName: '票据类型', component: "barNav", containerClass: "inline-block", bShow: true,
            default: e => e.code === 'PAP', attribute: { displayPath: 'name' }
        },
        {
            id: "npcMng2", typeName: "npcAddQuot", component: "mdButton", displayName: "新增报价", containerClass: "inline-block animation-target-addprice",
            containerAlien: "alien-right", bShow: true
        },
        { id: "npcMng3", typeName: "npcImport", component: "mdButton", displayName: "批量导入", containerClass: "inline-block import", containerAlien: "alien-right", bShow: true },
        {
            id: "npcMng5", typeName: "npcTradeModel", conditionName: '交易模式', component: "dropLabel", containerClass: "inline-block", containerAlien: "alien-right", bShow: true,
            default: e => e.code === 'BOT', attribute: { displayPath: 'name', label: '交易模式', vmType: "Object" }
        },
        {
            id: "npcMng6", typeName: "npcDateRange", vm: "date", component: "inputDatePickerRange", containerClass: "inline-block", containerAlien: "alien-right", bShow: true,
            attribute: { label: "发价日期", flex: { label: 30, start: 35, end: 35 } }
        }
    ],

    searchCriteriaDataDefineMatchFunc: (item, dataDefine) =>
        item && item.conditionName && dataDefine && dataDefine.conditionName && item.conditionName === dataDefine.conditionName,

    footDefine: {
        titles: [
            { displayName: '当日价格概览', value: 'current', isDefault: true },
            { displayName: '近期价格走势', value: 'tendency' }
        ],

        quotePriceTypeDefaultValue: {
            other: e => e.code === 'GG'
        },
    }

}

class manageNpcTicketHallCtrl {
    constructor($scope, babQuoteService, userService, componentCommonService, commonService, babNpcConsts, $q, gridColumnFactoryService, gridExcelImportService) {
        this.$scope = $scope;
        this.babQuoteService = babQuoteService;
        this.userService = userService;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;
        this.babNpcConsts = babNpcConsts;
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.gridExcelImportService = gridExcelImportService;
        this.$q = $q;
        this[initView]();
    };

    onSearchCriteriaChanged() {
        if (this.$scope.vm.searchPanelData instanceof Array) {

            var condition = this.$scope.vm.searchPanelData.findItem(e => e.conditionName === '票据类型');

            if (condition) {
                this.chartVm.billMedium = this.commonService.getPropertyX(condition, 'attribute.ngModel.code');

                if (!this.chartVm.quotePriceType) {
                    this.chartVm.quotePriceType = this.chartVm.qptItemSource.other.findItem(dataDefine.footDefine.quotePriceTypeDefaultValue.other);
                }
            }

            condition = this.$scope.vm.searchPanelData.findItem(e => e.vm === 'date');

            if (condition) {
                this.chartVm.startDate = this.commonService.getPropertyX(condition, 'attribute.ngModel.start');
                if (this.chartVm.startDate) this.chartVm.startDate = this.chartVm.startDate.getTime();
                this.chartVm.endDate = this.commonService.getPropertyX(condition, 'attribute.ngModel.end');
                if (this.chartVm.endDate) this.chartVm.endDate = this.chartVm.endDate.getTime();
            }

            condition = this.$scope.vm.searchPanelData.findItem(e => e.conditionName === '交易模式');

            if (condition) {
                this.chartVm.tradeType = this.commonService.getPropertyX(condition, 'attribute.ngModel.code');
            }
        }

        this[changeFootToCurrent]();
        this[changeFootToTendency]();
    };

    onSelectChartPage() {
        this.gridColumnFactoryService.resizeGrid('panel-title-container');
        this[changeFootToTendency]();
    };

    [changeFootToCurrent]() {

        if (!this.chartVm.tradeType) return;

        if (!this.chartVm.billMedium) return;

        this.babQuoteService.babInitMngFootCurrent({
            "quoteType": "NPC",
            "billMedium": this.chartVm.billMedium,
            "billType": "BKB",
            "tradeType": this.chartVm.tradeType
        }).then(res => {
            if (res instanceof Array && dataDefine.footDefine.quotePriceTypeIndexMap) {
                res.forEach(e => { angular.merge(e, dataDefine.footDefine.quotePriceTypeIndexMap.get(e.quotePriceType)); });
                this.chartVm.currentItemSource = res;
            }
        }, res => { });

    };

    [changeFootToTendency]() {

        if (!this.chartVm.tradeType) return;

        if (!this.chartVm.billMedium) return;

        this.babQuoteService.babInitMngFootTendency({
            "quoteType": "NPC",
            "quotePriceType": this.chartVm.quotePriceType.code,
            "billType": "BKB",
            "billMedium": this.chartVm.billMedium,
            "tradeType": this.chartVm.tradeType
        }).then(res => {
            this.chartVm.chartData = res;
        }, res => { });

    };

    [initView]() {
        console.debug('manageNpcTicketHallCtrl initView');

        dataDefine.addPriceDataDefine = angular.copy(this.babNpcConsts.addPriceDataDefine);
        dataDefine.viewModelDataDefine = angular.copy(this.babNpcConsts.viewModelDataDefine);

        this.$scope.vm = {};

        this.chartVm = {};

        this.footTitles = dataDefine.footDefine.titles;
    };

    $onInit() {
        let userInfo = this.userService.getUserInfo();
        if (userInfo && userInfo.info && userInfo.info['bab.quote.batchimport']) {
            this.importAuth = userInfo.info['bab.quote.batchimport'] === 'true';
        }

        this.babQuoteService.babMngInitData("NPC").then(res => {
            this.$scope.initData = angular.copy(res);

            let searchNpcmngDataDefine = angular.copy(dataDefine.searchNpcmngDataDefine);
            searchNpcmngDataDefine.findItem(e=>e.id === 'npcMng3').bShow = this.importAuth;
            if (res.parameterList) {
                this.$scope.vm.searchPanelData = this.componentCommonService.createSearchCriteriaVm({
                    source: res.parameterList,
                    dataDefine: searchNpcmngDataDefine,
                    defineMatchFunc: dataDefine.searchCriteriaDataDefineMatchFunc
                });
            }

            if (res.parameterList && res.parameterList instanceof Array) {

                dataDefine.footDefine.quotePriceTypeIndexMap = new Map();

                // init foot chartVm
                [
                    {
                        targetMatcher: e => e.conditionName === "承兑行类别", sourceProp: 'other', targetProp: 'conditions',
                        default: dataDefine.footDefine.quotePriceTypeDefaultValue.other
                    }
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
            // this.componentCommonService.openErrorDialog({
            //     title: '错误',
            //     message: '初始化数据获取失败',
            //     theme: this.theme
            // });

            if (res && res.return_message && res.return_message.exceptionCode === "E8888") {
                this.componentCommonService.openErrorDialog({
                    title: res.return_message.exceptionName,
                    message: res.return_message.exceptionMessage,
                    theme: this.theme
                });

                // this.$router.navigateByUrl('../login?page=ManageNpcTicketHall');
            } else {
                this.componentCommonService.openErrorDialog({
                    title: '错误',
                    message: '初始化数据获取失败',
                    theme: this.theme
                });
            }
        });
    };

    click(evt) {
        console.debug(this.$scope.vm.ngModel);
    };

    onTabChanged(tabCode) {

    };

    onBtnClick(event, define) {
        switch (define.typeName) {
            case "npcAddQuot":


                this.userService.getToken('bab.npc').then(res => {
                    console.debug(`manageSsrTicketHallCtrl initView. get token: ${res.result[0]}`);
                    return this.$q.resolve(res.result[0]);
                }, res => this.$q.reject(res)).then(res => {
                    // this.openAddQuote(dataDefine.addPriceDataDefine, event, res);
                    this.addPrice(event, res);
                }, res => {
                    this.componentCommonService.openErrorDialog({
                        title: '错误',
                        message: '初始化报价表单失败',
                        theme: this.theme
                    });
                });

                break;
            case "npcImport":
                this.gridExcelImportService.openImportDialog(undefined, { type: 'npc' }, this.$scope.initData).then(e => {
                    this.mdPanelRef = e;
                });
                break;
            default: break;
        };
    };

    addPrice(event, token) {

        dataDefine.viewModelDataDefine.addPriceVaildRule = this.babNpcConsts.viewModelDataDefine.initVaildRuleFunc(dataDefine.viewModelDataDefine.addPriceVaildRule);
        this.componentCommonService.openAddPriceDialog(addPriceNpcCtrl, {
            itemSource: dataDefine.addPriceDataDefine,
            theme: this.theme,
            title: '报价',
            event: event,
            button: '.animation-target-addprice',
            onClosing: result => {

                if (result.prices) {
                    for (var prop in result.prices) {
                        if (!result.prices.hasOwnProperty(prop)) continue;

                        if (result.prices[prop] && result.prices[prop].inputValue) {
                            result.prices[prop].value = +result.prices[prop].inputValue;
                        }else{
                            result.prices[prop].value = "";
                        }
                    }
                }

                var defineArray = ["prices.ggPrice", "prices.csPrice", "prices.nsPrice", "prices.nxPrice", "prices.nhPrice", "prices.czPrice", "prices.wzPrice", "prices.cwPrice"];

                return this.babQuoteService.checkViewModelPrices(result, defineArray, this.theme).then(res => {
                    if (res) {
                        result.quoteToken = token;
                        return this.babQuoteService.babClosingAddpriceDialog('NPC', {
                            theme: this.theme,
                            result: result,
                            dataDefine: angular.copy(dataDefine.viewModelDataDefine)
                        }, "DFT").then(res => {
                            if (res) {
                                this.$scope.$broadcast("gridManageNpcTicketHall");
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
        }, answer => {
            console.debug(answer);
        });
    };

    onEditListItem(event, item) {

        return this.userService.getToken('bab.npcedit').then(res => {
            console.debug(`manageSsrTicketHallCtrl initView. get token: ${res.result[0]}`);
            return this.$q.resolve(res.result[0]);
        }, res => this.$q.reject(res)).then(token => {
            var dialogDataDefine = angular.copy(dataDefine.addPriceDataDefine);

            if (!(dialogDataDefine instanceof Array)) {
                return this.$q.reject(`dialogDataDefine is not array`);
            }

            this.componentCommonService.initEditPriceVm(dialogDataDefine, dataDefine.viewModelDataDefine, item);

            return this.componentCommonService.openAddPriceDialog(addPriceNpcCtrl, {
                itemSource: dialogDataDefine,
                theme: this.theme,
                title: '修改报价',
                event: event,
                button: '.animation-target-addprice',
                onClosing: result => {

                    if (result.prices) {
                        for (var prop in result.prices) {
                            if (!result.prices.hasOwnProperty(prop)) continue;

                            if (result.prices[prop] && result.prices[prop].inputValue) {
                                result.prices[prop].value = +result.prices[prop].inputValue;
                            }else{
                                result.prices[prop].value = "";
                            }
                        }
                    }

                    var defineArray = [
                        "prices.ggPrice", "prices.csPrice", "prices.nsPrice", "prices.nxPrice", "prices.nhPrice", "prices.czPrice", "prices.wzPrice", "prices.cwPrice"
                    ];

                    return this.babQuoteService.checkViewModelPrices(result, defineArray, this.theme).then(res => {
                        if (res) {
                            result.quoteToken = token;
                            return this.babQuoteService.babClosingEditpriceDialog('NPC', {
                                theme: this.theme,
                                result: result,
                                dataDefine: angular.copy(dataDefine.viewModelDataDefine),
                                originItem: item
                            }, "DFT").then(res => {
                                if (res) {
                                    this.$scope.$broadcast("gridManageNpcTicketHall");
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
            return this.$q.resolve(true);
        }, answer => {
            console.debug(answer);
            return this.$q.reject(false);
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
                def.resolve(this.babQuoteService.babSetStatus("NPC", { "ids": [item.id], "status": "DSB" }));
            },
            cancelCallback: e => {
                def.reject();
            }
        });

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
                def.resolve(this.babQuoteService.babSetStatus("NPC", { "ids": [item.id], "status": "DEL" }));
            },
            cancelCallback: e => {
                def.reject();
            }
        })

        return def.promise;
    };

    $routerOnActivate(currentInstruction, previousInstruction) {

        if (currentInstruction) {
            if (currentInstruction.routeData && currentInstruction.routeData.data) {
                this.theme = currentInstruction.routeData.data.theme;
            }
        }

        var userInfo = this.userService.getUserInfo();

        if (userInfo) {
            var canActive = userInfo.info["bab.quote.npc.management"];

            if (!canActive || canActive !== "true") {
                this[onAuthFailed]();
            }
        }
    };

    [onAuthFailed]() {
        this.isAuthFailed = true;

        // this.componentCommonService.openErrorDialog({
        //     title: "认证失败",
        //     message: "用户无转贴价格管理浏览权限！",
        //     theme: this.theme
        // });

        this.$router.navigateByUrl('../login');
    };
}

let manageNpcTicketHall = () => {
    return {
        template: require('./template/manage_npc_ticket_hall.html'),
        bindings: {
            theme: '@mdTheme',
            $router: '<'
        },
        controller: [
            '$scope', 'babQuoteService', 'userService', 'componentCommonService', 'commonService', 'babNpcConsts', '$q', 'gridColumnFactoryService', 'gridExcelImportService', manageNpcTicketHallCtrl
        ]
    }
};

export default manageNpcTicketHall;