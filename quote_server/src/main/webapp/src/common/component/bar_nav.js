const initView = Symbol('initView');

class  barNavCtrl {

    constructor($scope, componentCommonService) {
        this.$scope = $scope;

        this.componentCommonService = componentCommonService;

        this[initView]();

        if(this.itemSource && this.itemSource instanceof Array){
            this.itemSource.forEach(e =>{
                if(e.isDefault)
                    this.currentNavItem = e.code;
            });

            if(!this.currentNavItem) this.currentNavItem = this.itemSource[0].code;
            this.preNavItem = this.currentNavItem;
        }
    };

    [initView]() {
        console.debug('barNavCtrl initView');
    };

    $onInit(){
        this.ngModelCtrl.$viewValue = {code:this.currentNavItem};
        this.ngModelCtrl.$commitViewValue();
        if(this.vmChanged) this.vmChanged();
    };

    onClick(evt){
        if(this.preNavItem === this.currentNavItem) return;
        this.preNavItem = this.currentNavItem;

        this.ngModelCtrl.$viewValue = {code:this.currentNavItem};
        this.ngModelCtrl.$commitViewValue();
        if(this.vmChanged) this.vmChanged();
        if(this.tabChanged) this.tabChanged({tabCode:this.currentNavItem});
    }
};

let barNav = () => {
    return {
        template: require('./template/bar_nav.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            displayPath:'@',
            itemSource: '<',
            ngModel: '<',
            vmChanged:'&',
            tabChanged:'&',
        },
        controller: ['$scope', 'componentCommonService', barNavCtrl]
    }
};

export default barNav;