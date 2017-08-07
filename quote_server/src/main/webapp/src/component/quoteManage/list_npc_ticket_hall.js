import addPriceNpcCtrl from './controller/addPriceNpcCtrl';

const initView = Symbol('initView');
const changeFootToCurrent = Symbol('changeFootToCurrent');
const changeFootToTendency = Symbol('changeFootToTendency');
const onAuthFailed = Symbol('onAuthFailed');

const dataDefine = {

    searchCriteriaDataDefine: [
        {
            id: "ssr1",
            conditionName: '票据类型',
            vm: "billMedium",
            component: 'inputButtonSelector',
            containerClass: "inline-block",
            default: e => e.code === 'PAP',
            attribute: { label: '票据类型', displayPath: 'name', vmType: "Object" }
        }],

    dropLabelData: [
        {
            id: "ssr2", conditionName: '交易模式', component: 'dropLabel', containerClass: "inline-block",
            default: e => e.code === 'BOT', attribute: { label: '交易模式', displayPath: 'name', vmType: "Object" }
        }],

    footDefine: {
        titles: [
            { displayName: '当日价格概览', value: 'current', isDefault: true },
            { displayName: '近期价格走势', value: 'tendency' }
        ],

        quotePriceTypeDefaultValue: {
            other: e => e.code === 'GG'
        },
    }
};


class listNpcTicketHallCtrl {

    constructor($scope, babQuoteService, componentCommonService, commonService, userService, babNpcConsts, configConsts, $q, websocketService, gridColumnFactoryService, gridExcelImportService) {

        this.$scope = $scope;

        this.babQuoteService = babQuoteService;
        this.websocketService = websocketService;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;
        this.userService = userService;
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.gridExcelImportService = gridExcelImportService;

        this.babNpcConsts = babNpcConsts;
        this.configConsts = configConsts;

        // this.theme = "ssAvalonUi";
        this.$q = $q;
        this[initView]();
    };

    onClickImport(event) {
        this.gridExcelImportService.openImportDialog(undefined, { type: 'npc' }, this.$scope.initData).then(e => {
            this.mdPanelRef = e;
        });;
    }

    addPrice(event) {
        dataDefine.viewModelDataDefine.addPriceVaildRule = this.babNpcConsts.viewModelDataDefine.initVaildRuleFunc(dataDefine.viewModelDataDefine.addPriceVaildRule);

        this.userService.getToken('bab.npc').then(res => {
            console.debug(`listNpcTicketHallCtrl initView. get token: ${res.result[0]}`);
            return this.$q.resolve(res.result[0]);
        }, res => this.$q.reject(res)).then(token => {
            // this.openAddQuote(dataDefine.addPriceDataDefine, event, res);

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
                            });
                        } else {
                            return this.$q.reject(false);
                        }
                    }, res => this.$q.reject(false));
                },
                onVmChanged: result => { }
            })
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

    onSearchCriteriaChanged() {
        this.chartVm.billMedium = this.commonService.getPropertyX(this, '$scope.vm.searchCriteria.billMedium.code');

        if (this.chartVm.qptItemSource && !this.chartVm.quotePriceType) {
            this.chartVm.quotePriceType = this.chartVm.qptItemSource.other.findItem(dataDefine.footDefine.quotePriceTypeDefaultValue.other);
        }

        this.chartVm.tradeType = this.commonService.getPropertyX(this, '$scope.vm.dueType.code');

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
        console.debug('listNpcTicketHallCtrl initView');

        dataDefine.addPriceDataDefine = angular.copy(this.babNpcConsts.addPriceDataDefine);
        dataDefine.viewModelDataDefine = angular.copy(this.babNpcConsts.viewModelDataDefine);

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
        this.babQuoteService.babInitData('NPC').then(res => {
            if (!res) return;

            this.$scope.initData = angular.copy(res);

            var initData = this.$scope.initData;

            // initSearchCriteriaDataDefine
            if (res.parameterList) {
                this.$scope.vm.searchCriteriaDefine = this.componentCommonService.createSearchCriteriaVm({
                    source: res.parameterList,
                    dataDefine: dataDefine.searchCriteriaDataDefine,
                    defineMatchFunc: (item, dataDefine) => item && item.conditionName && dataDefine && dataDefine.conditionName && item.conditionName === dataDefine.conditionName
                });

                this.$scope.vm.dueTypeItemSource = this.componentCommonService.createSearchCriteriaVm({
                    source: res.parameterList,
                    dataDefine: dataDefine.dropLabelData,
                    defineMatchFunc: (item, dataDefine) => item && item.conditionName && dataDefine && dataDefine.conditionName && item.conditionName === dataDefine.conditionName
                })[0].attribute.itemSource;

                this.inputFlex = 70;
                this.labelFlex = 25;
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
            console.error('listNpcTicketHallCtrl babQuoteService.babInitData.');
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

        this.labelFlex = "35";
        this.inputFlex = "65";
        this.path = "name";
    };

    $routerOnActivate(currentInstruction, previousInstruction) {

        if (currentInstruction) {
            if (currentInstruction.routeData && currentInstruction.routeData.data) {
                this.theme = currentInstruction.routeData.data.theme;
            }
        }

        var userInfo = this.userService.getUserInfo();

        if (userInfo) {
            var canActive = userInfo.info["bab.quote.npc.view"];

            if (!canActive || canActive !== "true") {
                this[onAuthFailed]();
            }
        }
    };

    [onAuthFailed]() {
        this.isAuthFailed = true;

        this.componentCommonService.openErrorDialog({
            title: "认证失败",
            message: "用户无全国转贴报价浏览权限！",
            theme: this.theme
        });

        this.$router.navigateByUrl('../login');
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

        this.babQuoteService.babSearchCompanies("NPC", content).then(res => {
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

let listNpcTicketHall = () => {
    return {
        template: require('./template/list_npc_ticket_hall.html'),
        bindings: {
            theme: '@',
            $router: '<'
        },
        controller: ['$scope', 'babQuoteService', 'componentCommonService', 'commonService', 'userService', 'babNpcConsts', 'configConsts', '$q', 'websocketService', 'gridColumnFactoryService', 'gridExcelImportService', listNpcTicketHallCtrl]
    }
};

export default listNpcTicketHall;