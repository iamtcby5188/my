const initView = Symbol('initView');

class panelTitleContainerCtrl {

    constructor(componentCommonService) {

        this.componentCommonService = componentCommonService;

        this[initView]();
    };

    onClickItem(event) {
        if (this.ngModelCtrl) {
            this.ngModelCtrl.$viewValue = this.currentNavItem;
            this.ngModelCtrl.$commitViewValue();
        }
    };

    [initView]() {
        console.debug('panelTitleContainerCtrl initView');
    };

    $onChanges(event) {
        if (event.titles) {

            if (event.titles.currentValue) {
                if (this.titles instanceof Array) {
                    this.itemSource = this.titles;

                    this.itemSource.forEach(e => {
                        if (e.isDefault) {
                            this.currentNavItem = e.value;

                            if (this.ngModelCtrl) {
                                this.ngModelCtrl.$viewValue = this.currentNavItem;
                                this.ngModelCtrl.$commitViewValue();
                            }

                        }
                    });
                } else {
                    this.itemSource = [{ displayName: this.titles, value: 'default' }];

                    this.currentNavItem = 'default';

                    if (this.ngModelCtrl) {
                        this.ngModelCtrl.$viewValue = this.currentNavItem;
                        this.ngModelCtrl.$commitViewValue();
                    }
                }

                this.componentCommonService.setDisplayNameForItems(this.itemSource, this.displayPath ? this.displayPath : undefined);
            } else {

            }

        }
    };


};

let panelTitleContainer = () => {
    return {
        template: require('./template/panel_title_container.html'),
        require: {
            ngModelCtrl: '?ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            displayPath: '@',

            ngModel: '<',

            titles: '<',

            type: '@'
        },
        controller: ['componentCommonService', panelTitleContainerCtrl],
        transclude: true
    }
};

export default panelTitleContainer;