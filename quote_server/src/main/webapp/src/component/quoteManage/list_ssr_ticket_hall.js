import addPriceSsrCtrl from './controller/addPriceSsrCtrl';
import addPriceQuickSsrCtrl from './controller/addPriceQuickSsrCtrl';

const initView = Symbol('initView');

const dataDefine = {

    searchCriteriaDataDefineMatchFunc: (item, dataDefine) => {
        if (item && item.conditionName && dataDefine && dataDefine.conditionName) {

            if (dataDefine.conditionName === "承兑行承兑方类别" && (item.conditionName === "承兑行类别" || item.conditionName === "承兑方类别")) { return true; }

            return item.conditionName === dataDefine.conditionName;
        } else {
            return false;
        }
    },

    searchCriteriaDataDefine: [
        {
            id: "ssr1", vm: 'billTypeMedium', conditionName: '票据类型', component: 'inputButtonSelector', containerClass: "inline-block",
            default: e => e.code === 'ELE_BKB', attribute: { label: '票据类型', displayPath: 'name', vmType: "Object" }
        }, {
            id: "ssr2", vm: 'direction', conditionName: '报价方向', component: 'inputButtonSelector', containerClass: "inline-block", attribute:
            { label: '报价方向', hasSelectAllButton: true, displayPath: 'name', vmType: "Object" }
        },
        // { id: "ssr3", conditionName: '承兑行类别', component: 'inputButtonSelector', attribute: { label: '承兑行类别', hasSelectAllButton: true, displayPath: 'name' } },
        { id: "ssr3", vm: 'qptOrAh', conditionName: '承兑行承兑方类别', component: 'searchcriteriaAcceptinghouseQuotepricetype', attribute: { vmType: 'quotePriceType' } },
        {
            id: "ssr4", vm: 'amountList', conditionName: '票面金额', component: 'inputButtonSelector',
            attribute: { label: '票面金额', hasSelectAllButton: true, displayPath: 'name' }
        },
        {
            id: "ssr5", vm: 'dueDateWrapperList', conditionName: '剩余期限', component: 'inputSelectorRange',
            attribute: {
                label: '剩余期限', hasSelectAllButton: true, displayPath: 'name',
                itemSource: {
                    rangeValue: {
                        dropList: [
                            {
                                code: "day", displayName: "天",
                                procFuncEnd: e => {
                                    if (!e || !angular.isNumber(+e) || +e > 390 || isNaN(+e)) return false;
                                    return true;
                                },
                                errorTipEnd: e => { return e + "期限输入错误,期限必须小于等于390天" },
                                procFuncStart: e => {
                                    if (!e || !angular.isNumber(+e) || +e < 1 || +e === 0 || isNaN(+e)) return false;
                                    return true;
                                },
                                errorTipStart: e => { return e + "期限输入错误,期限必须大于等于1天" }
                            },
                            {
                                code: "mon", displayName: "月", bDefault: true,
                                procFuncEnd: e => {
                                    if (!e || !angular.isNumber(+e) || +e > 13 || isNaN(+e)) return false;
                                    return true;
                                },
                                errorTipEnd: e => { return e + "期限输入错误,期限必须小于13个月" },
                                procFuncStart: e => {
                                    if (!e || !angular.isNumber(+e) || (+e < 1 && +e !== 0) || isNaN(+e)) return false;
                                    return true;
                                },
                                errorTipStart: e => { return e + "期限输入错误,期限必须大于等于0个月" }
                            },
                        ],
                    }
                },
                selectorFlex: { flex: 25 },
                rangeFlex: {
                    flex: 40,
                    ctrl: { label: 7, start: 10, end: 10, button: 10, drop: 10 }
                }
            }
        },
        { id: "ssr6", vm: 'provinceCodes', conditionName: '交易地点', component: 'inputDropdownChips', attribute: { label: '交易地点', displayPath: 'name' } },
    ]
};

class listSsrTicketHallCtrl {

    constructor($scope, $q, babQuoteService, componentCommonService, commonService, userService, babSsrConsts) {
        this.$scope = $scope;
        this.$q = $q;

        this.babQuoteService = babQuoteService;

        this.componentCommonService = componentCommonService;
        this.commonService = commonService;
        this.userService = userService;

        this.babSsrConsts = babSsrConsts;
        this[initView]();
    };

    onClickAddPrice(event) {
        this.userService.getToken('bab.ssr').then(res => {
            console.debug(`listSsrTicketHallCtrl initView. get token: ${res.result[0]}`);
            return this.$q.resolve(res.result[0]);
        }, res => this.$q.reject(res)).then(res => {
            this.openAddQuote(dataDefine.addPriceDataDefine, event, res);
        }, res => {
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: '初始化报价表单失败',
                theme: this.theme
            });
        });
    };

    openAddQuote(dialogDataDefine, event, token) {

        var viewModelDataDefine = angular.copy(dataDefine.viewModelDataDefine);

        viewModelDataDefine.addPriceVaildRule = this.babSsrConsts.initVaildRuleFunc(viewModelDataDefine.addPriceVaildRule, dialogDataDefine);

        this.componentCommonService.openAddPriceDialog(addPriceSsrCtrl, {
            itemSource: dialogDataDefine,
            theme: this.theme,
            title: '报价',
            event: event,
            button: '.animation-target-addprice',
            onClosing: result => {

                var promise = this.babQuoteService.checkPriceVaildValueFunc(result, 'price', this.theme) || this.$q.resolve(true);

                return promise.then(res => {
                    if (res) {
                        result.quoteToken = token;
                        return this.babQuoteService.babClosingAddpriceDialog('SSR', {
                            theme: this.theme,
                            result: result,
                            dataDefine: angular.copy(viewModelDataDefine)
                        }, "DSB");
                    } else {
                        return this.$q.reject(false);
                    }
                }, res => this.$q.reject(false));
            },
            onVmChanged: result => {
                if (!result || result.normalOrQuickBoard === undefined) return;

                if (result.normalOrQuickBoard === true) {
                    this.addPricePanelRef && this.addPricePanelRef.close().then(() => {
                        this.addPricePanelRef.destroy();
                        this.openAddQuote(dataDefine.addPriceDataDefine, event, token);
                    });

                } else if (result.normalOrQuickBoard === false) {
                    this.addPricePanelRef && this.addPricePanelRef.close().then(() => {
                        this.addPricePanelRef.destroy();
                        this.openAddQuote(dataDefine.addPriceQuickDataDefine, event, token);
                    });
                }
            }
        }).then(ref => {
            this.addPricePanelRef = ref;
        }, answer => {
            console.debug(answer);
        });
    };

    // onClickTest(event) {
    //     console.debug(this.$scope.vm.searchCriteria);
    //     console.debug(this.componentCommonService.getSearchCriteriaListByVm(this.$scope.vm.searchCriteria, dataDefine.searchCriteriaDataDefine));
    // };
    //
    // onClickClear(event) {
    //     this.$scope.vm.searchCriteria = undefined;
    // };
    //
    // onClickSave(event) {
    //     this.$scope.vm.searchCriteriaBak = angular.copy(this.$scope.vm.searchCriteria);
    // };
    //
    // onClickLoad(event) {
    //     this.$scope.vm.searchCriteria = angular.copy(this.$scope.vm.searchCriteriaBak);
    // };

    uploadPic(picBase64) {
        this.picInfo = picBase64;
    }
    [initView]() {
        console.debug('listSsrTicketHallCtrl initView');

        dataDefine.addPriceDataDefine = angular.copy(this.babSsrConsts.addPriceDataDefine);
        dataDefine.addPriceQuickDataDefine = angular.copy(this.babSsrConsts.addPriceQuickDataDefine);
        dataDefine.viewModelDataDefine = angular.copy(this.babSsrConsts.viewModelDataDefine);

        this.$scope.vm = {};

        this.$scope.$on('gridTicketHallSsr', (evt, param) => {

            this.$scope.vm.searchCriteriaDefine.forEach(e=>{
                if(e.vm !== "billTypeMedium"){
                    if(e.attribute && e.attribute.ngModel)
                        e.attribute.ngModel = undefined;
                }
            });

            var medium = this.$scope.vm.searchCriteria.billTypeMedium;
            this.$scope.vm.searchCriteria = {};
            this.$scope.vm.searchCriteria.billTypeMedium = medium;
        });
    };

    $onInit() {

        this.$viewBusy = true;

        this.babQuoteService.babInitData('SSR').then(res => {
            if (!res) return;

            this.$scope.initData = angular.copy(res);

            var initData = this.$scope.initData;

            // initSearchCriteriaDataDefine
            if (res.parameterList instanceof Array) {
                this.$scope.vm.searchCriteriaDefine = this.componentCommonService.createSearchCriteriaVm({
                    source: res.parameterList,
                    dataDefine: dataDefine.searchCriteriaDataDefine,
                    defineMatchFunc: dataDefine.searchCriteriaDataDefineMatchFunc
                });

                [
                    // { targetMatcher: e => e.conditionName === "承兑行类别", sourceMatcher: e => e.vm === "quotePriceType", sourceProp: 'conditions' },
                    { targetMatcher: e => e.conditionName === "承兑行类别", sourceMatcher: e => e.vm === "qptOrAh", sourceProp: 'conditions' },
                    { targetMatcher: e => e.conditionName === "交易地点", sourceMatcher: e => e.vm === "quoteProvinces", sourceProp: 'conditions' }
                ].forEach((item, index) => {
                    var target = res.parameterList.findItem(item.targetMatcher);
                    if (target) {
                        var define = dataDefine.addPriceDataDefine.findItem(item.sourceMatcher);

                        if (define) {
                            define.attribute.itemSource = angular.copy(this.commonService.getPropertyX(target, item.sourceProp));
                            define.attribute.itemSource.forEach(e => { e.isDefault = define.default && typeof define.default === "function" && define.default(e); })
                        }

                        define = dataDefine.addPriceQuickDataDefine.findItem(item.sourceMatcher);

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

                    if (define.attribute.itemSource[0]) {
                        define.attribute.defaultValue = define.attribute.itemSource[0];
                    } else {
                        define.attribute.defaultValue = {
                            joiningCompanyDto: userInfo.company,
                            joiningDisplayMode: "CTR",
                            joiningUserDto: userInfo.user
                        };
                        if (!define.attribute.itemSource) define.attribute.itemSource = [];
                        define.attribute.itemSource.push(define.attribute.defaultValue);
                    }
                }

                // 快速报价联系人设为默认
                define = dataDefine.addPriceQuickDataDefine.findItem(e => e.vm === "joininguser");
                if (define) {
                    define.attribute.defaultValue = {
                        joiningCompanyDto: userInfo.company,
                        joiningDisplayMode: "CTR",
                        joiningUserDto: userInfo.user
                    }
                    if (!define.attribute.itemSource) define.attribute.itemSource = [];
                    define.attribute.itemSource.push(define.attribute.defaultValue);
                }
            }

        }, res => {

            console.error('listSsrTicketHallCtrl babQuoteService.babInitData.');

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

    $routerOnActivate(currentInstruction, previousInstruction) {
        if (currentInstruction) {
            if (currentInstruction.routeData && currentInstruction.routeData.data) {
                this.theme = currentInstruction.routeData.data.theme;
            }
        }
    };

};

let listSsrTicketHall = () => {
    return {
        template: require('./template/list_ssr_ticket_hall.html'),
        bindings: {
            theme: '@mdTheme',
            $router: '<'
        },
        controller: ['$scope', '$q', 'babQuoteService', 'componentCommonService', 'commonService', 'userService', 'babSsrConsts', listSsrTicketHallCtrl]
    }
};

export default listSsrTicketHall;