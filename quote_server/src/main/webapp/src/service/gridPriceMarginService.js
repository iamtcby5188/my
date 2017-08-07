
class gridPriceMarginService {
    constructor(httpService, uiGridConstants, configConsts, userService) {
        this.httpService = httpService;
        this.uiGridConstants = uiGridConstants;
        this.configConsts = configConsts;
        this.userService = userService;

        this.priceGridOption = {
            rowHeight: 40,
            enableColumnMenus: false,
            enableFiltering: false,
            enableSorting: false,
            enableColumnResizing: false,
            enableInfiniteScroll: false,
            infiniteScrollDown: false,
            infiniteScrollUp: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            columnDefs: [
                {
                    name: '承兑行类别',
                    width: '*',
                    minWidth: 90,
                    cellTemplate: '<div class="ui-grid-cell-contents">{{grid.appScope.gridDataDefineService.BABQuotePriceType[row.entity.quotePriceType]}}</div>'
                },
                {
                    name: '最高（%）',
                    width: '*',
                    cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity.priceMax?(row.entity.priceMax | number:2):"--"}}</div>'
                },
                {
                    name: '最低（%）',
                    width: "*",
                    cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity.priceMin?(row.entity.priceMin | number:2):"--"}}</div>'
                }
            ]
        };

        this.shiborGridOption = {
            rowHeight: 40,
            enableColumnMenus: false,
            enableFiltering: false,
            enableSorting: false,
            enableColumnResizing: false,
            enableInfiniteScroll: false,
            infiniteScrollDown: false,
            infiniteScrollUp: false,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            columnDefs: [
                {
                    name: '期限',
                    width: '*',
                    cellTemplate: '<div class="ui-grid-cell-contents">{{grid.appScope.gridDataDefineService.Shibor[row.entity.period]}}</div>'
                },
                {
                    name: '价格（%）',
                    width: '*',
                    cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity.price?(row.entity.price|number :4):"--"}}</div>'
                },
                {
                    name: '涨跌(bp)',
                    width: "*",
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-class="{\'red-text\':row.entity.margin > 0,\'green-text\':row.entity.margin < 0,\'gray-text\':row.entity.margin === 0}">{{row.entity.margin}}</div>'
                }
            ]
        };
    };

    getPriceGridOption() {
        return angular.copy(this.priceGridOption);
    };

    getShiborGridOption() {
        return angular.copy(this.shiborGridOption);
    };

    initPriceMarginAnalysis(code) {
        let headers = this.userService.getAuthHeaders();    
        if (!headers || !headers.token) return this.$q.resolve({ result: undefined });       
        return this.httpService.postService(this.configConsts.price_margin_analysis_init, { billMedium: code }, headers);
    };

    updatePriceMarginAnalysis(code) {
        let headers = this.userService.getAuthHeaders();

        return this.httpService.postService(this.configConsts.price_margin_analysis_price, { billMedium: code }, headers);
    };

    updateTrendsHistory(dto) {

        var fn = dto => {
            let headers = this.userService.getAuthHeaders();

            return this.httpService.postService(this.configConsts.chart_get_price_trends_history, dto, headers);
        }

        return this.httpService.unique('gridPriceMarginService.updateTrendsHistory', dto, fn);
    };

}
export default ['httpService', 'uiGridConstants', 'configConsts', 'userService', gridPriceMarginService];