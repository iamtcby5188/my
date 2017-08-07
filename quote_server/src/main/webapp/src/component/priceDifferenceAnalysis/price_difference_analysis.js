const initView = Symbol('initView');
const getInitData = Symbol('getInitData');
const THEME_NAME = "ssAvalonUi";
const onAuthFailed = Symbol('onAuthFailed');

const dataDefine = {
    barItemDefine: [
        { code: "PAP", name: "纸票" },
        { code: "ELE", name: "电票" }
    ]
};

class priceDifferenceAnalysisCtrl {

    constructor($scope, gridPriceMarginService, priceDifferenceService, componentCommonService, userService) {
        this.$scope = $scope;
        this.gridPriceMarginService = gridPriceMarginService;
        this.priceDifferenceService = priceDifferenceService;
        this.theme = THEME_NAME;
        this.componentCommonService = componentCommonService;
        this.barItem = dataDefine.barItemDefine;
        this.userService = userService;
        this.chartVm = {};

        this[initView]();
    };

    [initView]() {
        console.debug('priceDifferenceAnalysisCtrl initView');
    };

    [getInitData](billMedium) {
        this.gridPriceMarginService.initPriceMarginAnalysis(billMedium).then(e => {
            if (e && e.result && angular.isArray(e.result) && e.result.length === 1) {
                this.priceMarginResp = angular.copy(e.result[0]);

                if (this.priceMarginResp) {
                    this.chartVm.priceHistory = angular.copy(e.result[0].priceHistory);
                    this.chartVm.netVolumeHistory = angular.copy(e.result[0].netVolumeHistory);
                };

                this.userService.registLoginExpireHandler(() => {
                    clearInterval(this.refreshTimer);
                });

                if (this.refreshTimer) clearInterval(this.refreshTimer);

                this.refreshTimer = setInterval(() => {
                    // 直贴价格 转贴价格
                    this.gridPriceMarginService.updatePriceMarginAnalysis(this.tabCodeSel).then(e => {
                        if (e && e.result && angular.isArray(e.result) && e.result.length === 1) {
                            this.priceMarginResp = e.result[0];
                            // this.priceMarginResp.currentPriceTrendsNPC = e.result[0].currentPriceTrendsNPC;
                            // this.priceMarginResp.currentPriceTrendsSSC = e.result[0].currentPriceTrendsSSC;
                            // this.priceMarginResp.priceMargin = e.result[0].priceMargin;
                            // console.log(this.priceMarginResp.priceMargin);
                        }
                    }, e => {
                        console.error('priceDifferenceAnalysisCtrl.$onInit: this.gridPriceMarginService.updatePriceMarginAnalysis');
                    });
                }, 5 * 60 * 1000);
            }
        }, res => {
            // console.error('priceDifferenceAnalysisCtrl.$onInit: this.gridPriceMarginService.initPriceMarginAnalysis');

            if (res && res.return_message && res.return_message.exceptionCode === "E8888") {
                this.componentCommonService.openErrorDialog({
                    title: res.return_message.exceptionName,
                    message: res.return_message.exceptionMessage,
                    theme: this.theme
                });
            } else {
                this.componentCommonService.openErrorDialog({
                    title: '错误',
                    message: '初始化数据获取失败',
                    theme: this.theme
                });
            }
        });
    };

    $onInit() {
        //此页面需要滚动条，不能影响其他页面
        jQuery('app>div').addClass('overflow-y-auto');
        // 直贴价格 转贴价格
        this.tabCodeSel = "PAP";

        // this[getInitData]("PAP");
    }

    onTabChanged(tabCode) {
        this.tabCodeSel = tabCode;
        this[getInitData](tabCode);
    };

    $onDestroy() {
        if (this.refreshTimer) clearInterval(this.refreshTimer);
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

            this[getInitData]("PAP");
        } else {
            this.userService.getUserInfoAsync(userInfo => {
                if (userInfo) {
                    var canActive = userInfo.info["bab.quote.npc.management"];

                    if (!canActive || canActive !== "true") {
                        this[onAuthFailed]();
                    }

                    this[getInitData]("PAP");
                }

            }, res => {
                this[onAuthFailed]();
            });
        }  
    };


    [onAuthFailed]() {
        this.isAuthFailed = true;

        this.$router.navigateByUrl('../login');
    };
};

let priceDifferenceAnalysis = () => {
    return {
        template: require('./template/price_difference_analysis.html'),
        bindings: {
            theme: '@mdTheme',
            $router: '<'
        },
        controller: ['$scope', 'gridPriceMarginService', 'priceDifferenceService', 'componentCommonService', 'userService', priceDifferenceAnalysisCtrl]
    }
};

export default priceDifferenceAnalysis;