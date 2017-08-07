import 'Cssjs';
import 'ArrayUtils';
import 'StringUtils';

// import '../service/iconSpritesmithService.js';

// debugger;

class iconSpritesmithCtrl {

    constructor($scope, $http, iconSpritesmithService, configConsts) {

        this.$scope = $scope;
        this.$http = $http;

        this.blackList = [".ss-icon[disabled]", ".ss-icon:hover", ".ss-icon:active", ".ss-icon-highlight-bg"];

        $scope.timeStamp = `2016‎ 年‎ 10‎ 月‎ 13‎ 日 ‏‎ 15:35:49 (loaded service ${iconSpritesmithService.serviceName})`;

        $scope.configConsts = configConsts;

        this.initView();
    };

    initView() {
        var iconCss = 'mock/avalon-ui-icon.css';

        this.$http.get(iconCss).then(res => {
            var parser = new cssjs();

            this.$scope.parsedCss = parser.parseCSS(res.data).findWhere(e => {

                // 黑名单中的类不显示
                if (this.blackList.findItem(e1 => e1 === e.selector)) {
                    return false;
                }

                return e.selector && e.selector.startWith(".ss-icon");
            }).map(item => {

                // 移除css类名第一位的‘.’
                item.className = item.selector.substr(1, item.selector.length);

                return item;
            });
        }, res => {
            console.log("Parse icon css failed.");
        });
    };

};

// export default iconSpritesmithCtrl;
export default ['$scope', '$http', 'iconSpritesmithService', 'configConsts', iconSpritesmithCtrl];