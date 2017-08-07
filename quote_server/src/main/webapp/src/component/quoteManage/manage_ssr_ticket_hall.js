/**
 * Created by jiannan.niu on 2016/12/30.
 * Updated By Wei.Lai on 2017/01/13
 */

import addPriceSsrCtrl from './controller/addPriceSsrCtrl';
import addPriceQuickSsrCtrl from './controller/addPriceQuickSsrCtrl';
import quotOperConfirmCtrl from './../../controller/quotOperConfirm';

const initView = Symbol('initView');
const checkViewModelPrices = Symbol('checkViewModelPrices');
const onAuthFailed = Symbol('onAuthFailed');
const dataDefine = {
    tabShow: {
        "DFT": ["ssrMng1", "ssrMng3", "ssrMng4", "ssrMng6"],
        "CAL": ["ssrMng1", "ssrMng6"],
        "DLD": ["ssrMng1", "ssrMng3", "ssrMng6", "ssrMng7"],
    },

    // id 不可更改
    searchSsrmngDataDefine: [
        {
            id: "ssrMng1", typeName: "ssrStatus", conditionName: '报价状态', component: "barNav", containerClass: "inline-block", flex: "20", bShow: true,
            default: e => e.code === 'DFT', attribute: { displayPath: 'name' }
        },
        { id: "ssrMng2", typeName: "ssrExport", component: "mdButton", displayName: "导出", containerClass: "inline-block", flex: "20", containerAlien: "alien-right", bShow: false },
        {
            id: "ssrMng3", typeName: "ssrAddQuot", component: "mdButton", displayName: "新增报价", containerClass: "inline-block animation-target-addprice",
            flex: "20", containerAlien: "alien-right", bShow: true
        },
        { id: "ssrMng4", typeName: "ssrImport", component: "mdButton", displayName: "批量导入", containerClass: "inline-block import", flex: "20", containerAlien: "alien-right", bShow: true },
        {
            id: "ssrMng5", typeName: "ssrDate", component: "inputDatePicker", containerClass: "inline-block", flex: "40", containerAlien: "alien-right", bShow: false,
            attribute: { label: "发布时间" }
        },
        {
            id: "ssrMng6", typeName: "ssrInputSearch", component: "inputSearch", containerClass: "inline-block", flex: "40", containerAlien: "alien-right", bShow: true,
            attribute: { displayPath: 'name' }
        },
        {
            id: "ssrMng7", typeName: "ssrDateRange", component: "inputDatePickerRange", containerClass: "inline-block", flex: "50", containerAlien: "alien-right", bShow: false,
            attribute: { label: "交易日期", flex: { label: 30, start: 35, end: 35 } }
        }
    ],

    searchCriteriaDataDefineMatchFunc: (item, dataDefine) =>
        item && item.conditionName && dataDefine && dataDefine.conditionName && item.conditionName === dataDefine.conditionName,
    searchCriteriaDataDefine: [
        {
            id: "ssr1", vm: 'billTypeMedium', conditionName: '票据类型', component: 'inputButtonSelector', containerClass: "inline-block",
            default: e => e.code === 'ELE_BKB', attribute:
            { label: '票据类型', displayPath: 'name', vmType: "Object" }
        }, {
            id: "ssr2", vm: 'direction', conditionName: '报价方向', component: 'inputButtonSelector', containerClass: "inline-block", attribute:
            { label: '报价方向', hasSelectAllButton: true, displayPath: 'name', vmType: "Object" }
        },
    ]
};

class manageSsrTicketHallCtrl {
    constructor($scope, $q, babQuoteService, userService, componentCommonService, commonService, babSsrConsts, gridColumnFactoryService, gridExcelImportService) {
        this.$scope = $scope;
        this.$q = $q;
        this.babQuoteService = babQuoteService;
        this.userService = userService;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;
        this.babSsrConsts = babSsrConsts;
        this.$q = $q;
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.gridExcelImportService = gridExcelImportService;
        this[initView]();
    };

    [initView]() {
        console.debug('manageSsrTicketHallCtrl initView');

        dataDefine.addPriceDataDefine = angular.copy(this.babSsrConsts.addPriceDataDefine);
        dataDefine.addPriceQuickDataDefine = angular.copy(this.babSsrConsts.addPriceQuickDataDefine);
        dataDefine.viewModelDataDefine = angular.copy(this.babSsrConsts.viewModelDataDefine);

        this.$scope.vm = {};
    };

    onTabChanged(tabCode) {
        if (this.$scope.vm.currentNavItem === tabCode) return;

        this.$scope.vm.currentNavItem = tabCode;
        var showArray = dataDefine.tabShow[tabCode];

        // for (var w = this.$scope.vm.searchPanelData.length - 1; w >= 0; --w) {
        //     this.$scope.vm.searchPanelData[w].bShow = false;
        // }
        // for (var j = showArray.length - 1; j >= 0; --j) {
        //     for (var q = this.$scope.vm.searchPanelData.length - 1; q >= 0; --q) {
        //         if (this.$scope.vm.searchPanelData[q].id === showArray[j]) {
        //             this.$scope.vm.searchPanelData[q].bShow = true;
        //             break;
        //         }
        //     }
        // }

        if (this.$scope.vm.searchPanelData instanceof Array) {
            this.$scope.vm.searchPanelData.forEach(e => {
                e.bShow = dataDefine.tabShow[tabCode].findItem(e1 => e1 === e.id) !== undefined;
            });
        }
    };

    onBtnClick(event, define) {
        switch (define.typeName) {
            case "ssrAddQuot":

                this.userService.getToken('bab.ssr').then(res => {
                    console.debug(`manageSsrTicketHallCtrl initView. get token: ${res.result[0]}`);
                    return this.$q.resolve(res.result[0]);
                }, res => this.$q.reject(res)).then(res => {
                    // this.openAddQuote(dataDefine.addPriceDataDefine, event);
                    this.openAddQuote(dataDefine.addPriceDataDefine, event, res);
                }, res => {
                    this.componentCommonService.openErrorDialog({
                        title: '错误',
                        message: '初始化报价表单失败',
                        theme: this.theme
                    });
                });

                break;
            case "ssrImport":
                this.gridExcelImportService.openImportDialog(undefined, { type: 'ssr' }, this.$scope.initData).then(e => {
                    this.mdPanelRef = e;
                });
                break;
            default: break;
        };
    };

    onInputSearch(content) {
        if (!content) {
            this.$scope.vm.ngModel.searchPanelModel.forEach(e => {
                if (e.$parentType === 'ssrInputSearch')
                    e.memo = undefined;
            });
        }
        else {
            var bFind = false;
            this.$scope.vm.ngModel.searchPanelModel.forEach(e => {
                if (e.$parentType === 'ssrInputSearch') {
                    e.memo = content;
                    bFind = true;
                }
            });

            if (!bFind) {
                this.$scope.vm.ngModel.searchPanelModel.push({ $parentType: "ssrInputSearch", memo: content });
            }
        }

        return [];
    };

    openAddQuote(dialogDataDefine, event, token) {

        let viewModelDataDefine = angular.copy(dataDefine.viewModelDataDefine);

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
                        }, "DSB").then(res => {
                            if (res) {
                                this.$scope.$broadcast("gridManageSsrTicketHallCtrl");
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

    onEditListItem(event, item) {

        return this.userService.getToken('bab.ssredit').then(res => {
            console.debug(`manageSsrTicketHallCtrl initView. get token: ${res.result[0]}`);
            return this.$q.resolve(res.result[0]);
        }, res => this.$q.reject(res)).then(token => {

            var dialogDataDefine = angular.copy(dataDefine.addPriceDataDefine);

            if (!(dialogDataDefine instanceof Array)) {
                return this.$q.reject(`dialogDataDefine is not array`);
            }

            // 移除 普通报价 快速报价 选择
            dialogDataDefine = dialogDataDefine.findWhere(e => e.vm !== 'normalOrQuickBoard');

            this.componentCommonService.initEditPriceVm(dialogDataDefine, dataDefine.viewModelDataDefine, item);
            if (!item.dueDate) {
                let dueDate = dialogDataDefine.findItem(e => e.vm === 'dueDate');
                delete dueDate.attribute.ngModel;
            }

            return this.componentCommonService.openAddPriceDialog(addPriceSsrCtrl, {
                itemSource: dialogDataDefine,
                theme: this.theme,
                title: '修改报价',
                event: event,
                button: '.animation-target-addprice',
                onClosing: result => {
                    var promise = this.babQuoteService.checkPriceVaildValueFunc(result, 'price', this.theme) || this.$q.resolve(true);
                    return promise.then(res => {
                        if (res) {
                            result.quoteToken = token;
                            return this.babQuoteService.babClosingEditpriceDialog('SSR', {
                                theme: this.theme,
                                result: result,
                                dataDefine: dataDefine.viewModelDataDefine,
                                originItem: item
                            }).then(res => {
                                if (res) {
                                    this.$scope.$broadcast("gridManageSsrTicketHallCtrl");
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
            return this.$q.reject(res);
        }).then(ref => {
            this.addPricePanelRef = ref;
            return this.$q.resolve(true);
        }, answer => {
            console.debug(answer);
            return this.$q.reject(false);
        });
    };

    onPublishListItem(event, item) {
        return this.babQuoteService.babSetStatus("SSR", { "ids": [item.id], "status": "DLD" });
    };

    onDelListItem(event, item) {
        var status = "CAL";
        this.$scope.vm.ngModel.searchPanelModel.forEach(e => {
            if (e.$parentType === 'ssrStatus') {
                status = e.code === 'DFT' ? "CAL" : "DEL";
            }
        });

        var def = this.$q.defer();
        this.componentCommonService.openConfirmDialog(quotOperConfirmCtrl, {
            title: status === "CAL" ? "撤销报价" : "删除报价",
            message: status === "CAL" ? "请确认是否撤销该笔报价？" : "请确认是否删除该笔报价？",
            quotContent: this.gridColumnFactoryService.convertQuotString(item),
            theme: this.theme,
            okCallback: e => {
                def.resolve(this.babQuoteService.babSetStatus("SSR", { "ids": [item.id], "status": status }));
            },
            cancelCallback: e => {
                def.reject();
            }
        })

        return def.promise;
    };

    $onInit() {
        let userInfo = this.userService.getUserInfo();
        if (userInfo && userInfo.info && userInfo.info['bab.quote.batchimport']) {
            this.importAuth = userInfo.info['bab.quote.batchimport'] === 'true';
        }

        this.babQuoteService.babMngInitData("SSR").then(res => {
            this.$scope.initData = angular.copy(res);

            if (res.parameterList) {
                this.$scope.vm.searchCriteriaDefine = this.componentCommonService.createSearchCriteriaVm({
                    source: res.parameterList,
                    dataDefine: dataDefine.searchCriteriaDataDefine,
                    defineMatchFunc: dataDefine.searchCriteriaDataDefineMatchFunc
                });

                let searchSsrmngDataDefine = angular.copy(dataDefine.searchSsrmngDataDefine);
                searchSsrmngDataDefine.findItem(e => e.id === 'ssrMng4').bShow = this.importAuth;

                this.$scope.vm.searchPanelData = this.componentCommonService.createSearchCriteriaVm({
                    source: res.parameterList,
                    dataDefine: searchSsrmngDataDefine,
                    defineMatchFunc: dataDefine.searchCriteriaDataDefineMatchFunc
                });
                // console.debug(this.$scope.vm.searchCriteriaDefine);
            }

            if (res.parameterList && res.parameterList instanceof Array) {
                [
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
                    define.attribute.defaultValue = define.attribute.itemSource[0];
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
            console.error('manageSsrTicketHallCtrl babQuoteService.babInitData.');
            // this.componentCommonService.openErrorDialog({
            //     title: '错误',
            //     message: res && res.return_message ? `${res.return_message.exceptionName} ${res.return_message.exceptionMessage}` : '初始化数据获取失败',
            //     theme: this.theme
            // });

            if (res && res.return_message && res.return_message.exceptionCode === "E8888") {
                this.componentCommonService.openErrorDialog({
                    title: res.return_message.exceptionName,
                    message: res.return_message.exceptionMessage,
                    theme: this.theme
                });

                // this.$router.navigateByUrl('../login?page=ManageSsrTicketHall');
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
        //     message: "用户无直贴业务管理浏览权限！",
        //     theme: this.theme
        // });

        this.$router.navigateByUrl('../login');
    };
}

let manageSsrTicketHall = () => {
    return {
        template: require('./template/manage_ssr_ticket_hall.html'),
        bindings: {
            theme: '@mdTheme',
            $router: '<'
        },
        controller: ['$scope', '$q', 'babQuoteService', 'userService', 'componentCommonService', 'commonService', 'babSsrConsts', 'gridColumnFactoryService', 'gridExcelImportService', manageSsrTicketHallCtrl]
    }
};

export default manageSsrTicketHall;