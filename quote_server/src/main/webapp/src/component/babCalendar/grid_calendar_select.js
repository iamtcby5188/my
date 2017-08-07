/**
 * Created by jiannan.niu on 2017/2/3.
 */
const initView = Symbol('initView');

class gridCalendarSelectCtrl {

    constructor ($scope, configConsts, httpService) {
        this.$scope = $scope;
        this.configConsts = configConsts;
        this.httpService = httpService;
        this[initView]();
    };

    [initView] () {
        console.debug('gridCalendarSelectCtrl initView');


    };

    $onChanges (event) {

    }

    onClickDate (event, item) {
        if (!event || !item || !item.date || item.holidayReason) return;
        this.$scope.$emit('clickDate', item);
    }
}


let gridCalendarSelect = () => {
    return {
        template: require('./template/grid_calendar_select.html'),
        bindings: {
            theme: '@',
            type: '@',
            vmChanged: '<',
            startDate: '<',
            endDate: '<'
        },
        controller: ['$scope', 'configConsts', 'httpService', gridCalendarSelectCtrl]
    }
};

export default gridCalendarSelect;