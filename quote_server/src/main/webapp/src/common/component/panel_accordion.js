const initView = Symbol('initView');

class panelAccordionCtrl {

    constructor($scope, $window) {
        this.$scope = $scope;
        this.$scope.window = $window;
        this.isPanelOpen = true || this.isPanelOpen;

        this[initView]();
    };

    toggleOpenClose(event) {
        this.isPanelOpen = !this.isPanelOpen;
        window.onresize();
    };

    [initView]() {
        console.debug('panelAccordionCtrl initView');
    };
};

let panelAccordion = () => {
    return {
        template: require('./template/panel_accordion.html'),
        bindings: {
            theme: '@'
        },
        controller: ['$scope', '$window', panelAccordionCtrl],
        transclude: true
    }
};

export default panelAccordion;