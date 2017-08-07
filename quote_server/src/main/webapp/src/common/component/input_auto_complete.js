const initView = Symbol('initView');

class inputAutoCompleteCtrl {

    constructor($scope, $timeout, $q, componentCommonService, commonService) {
        this.$scope = $scope;
        this.$timeout = $timeout;
        this.$q = $q;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;
        this.bBusy = false;

        this.searchTaskTimer = undefined;

        this[initView]();
    };

    [initView]() {
        console.debug('inputAutoComplete initView');
    };

    searchTextChange() {
        if (!this.searchText && this.searchFun) {
            this.searchFun({ content: '' });
            return;
        }
        if(this.lengthLimit && this.searchText.length > this.lengthLimit ){
            this.searchText = this.searchText.substr(0,this.lengthLimit);
            return;
        }
        if (this.bBusy) return;
        this.bBusy = true;

        if (this.searchTaskTimer) this.$timeout.cancel(this.searchTaskTimer);

        var defer = this.$q.defer();

        this.searchTaskTimer = this.$timeout(() => {
            this.bBusy = false;
            if(this.searchFun){
                if(this.isAsync === "true"){
                    this.searchFun({ content: this.searchText }).then(res=>{
                        this.componentCommonService.setDisplayNameForItems(res, this.displayPath);
                        defer.resolve(res);
                    });
                }
                else{
                    var ret = this.searchFun({ content: this.searchText });
                    if(ret && angular.isArray(ret)){
                        this.componentCommonService.setDisplayNameForItems(ret, this.displayPath);
                        defer.resolve(ret);
                    }
                }
            }
        }, 500, true);

        this.searchRet = defer.promise;
    };

    selectedItemChange() {
        this.curChangeTime = new Date().getTime();
        if (!this.selectedItem && this.searchText) return;
        this.ngModelCtrl.$viewValue = this.selectedItem ? this.selectedItem : {};
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    }

    enterEvent(evt) {
        var keycode = window.event ? evt.keyCode : evt.which;
        if (keycode == 13) {
            if(this.curChangeTime){
                var curEnter = new Date().getTime();
                if(curEnter - this.curChangeTime <= 500) return;

                this.curChangeTime = undefined;
            }

            this.ngModelCtrl.$viewValue = { memo: this.searchText };
            this.ngModelCtrl.$commitViewValue();
            if (this.vmChanged) this.vmChanged();
        }
    };

};     

let inputAutoComplete = () => {
    return {
        template: require('./template/input_auto_complete.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            timeEscape: "@",
            displayPath: "@",
            ngModel: '<',
            lengthLimit:'<',
            selectedItem: '<',
            placeholder: '@',
            searchFun: '&',
            isAsync:'@',
            vmChanged: '&',
        },
        controller: ['$scope', '$timeout', '$q', 'componentCommonService', 'commonService', inputAutoCompleteCtrl]
    }
};

export default inputAutoComplete;