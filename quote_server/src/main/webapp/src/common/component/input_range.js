const initView = Symbol('initView');
const THEME_NAME = "ssAvalonUi";

class inputRangeCtrl {

    constructor($scope, componentCommonService, commonService) {
        this.$scope = $scope;
        this.commonService = commonService;
        this.componentCommonService = componentCommonService;
        this[initView]();
        if(this.itemSource && this.itemSource.dropList && (this.itemSource.dropList instanceof Array)){
            this.itemSource.dropList.forEach(e =>{if(e.bDefault){
                if(!this.rangeValue) this.rangeValue = {};
                this.rangeValue.dropSel = e;
            }});

            if(!this.rangeValue) 
            {
                this.rangeValue = {};
                this.rangeValue.dropSel = this.itemSource.dropList[0];
            }
            this.rangeValue.$parentCriteriaId = this.itemSource.$parentCriteriaId;
        }        
    };

    [initView]() {
        console.debug('inputRangeCtrl initView');
    };

    onClickFilter(){

        if(this.rangeValue.dropSel.procFuncStart && !this.rangeValue.dropSel.procFuncStart(this.rangeValue.start)){
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: this.rangeValue.dropSel.errorTipStart("开始"),
                theme: THEME_NAME
            });

            return;
        }

        
        if(this.rangeValue.dropSel.procFuncEnd && !this.rangeValue.dropSel.procFuncEnd(this.rangeValue.end)){
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: this.rangeValue.dropSel.errorTipEnd("结束"),
                theme: THEME_NAME
            });

            return;
        }

        if(this.rangeValue.start && this.rangeValue.end){
            if(parseInt(this.rangeValue.start) > parseInt(this.rangeValue.end)){
                this.componentCommonService.openErrorDialog({
                    title: '错误',
                    message: "您输入的范围无效,开始值必须小于结束值",
                    theme: THEME_NAME
                });

                return;
            }
        }

        this.ngModelCtrl.$viewValue = angular.copy(this.rangeValue);
        this.ngModelCtrl.$commitViewValue();
        if(this.vmChanged) this.vmChanged();
    }

    $onChanges(event){
        if(!event) return;
        if(event.ngModel){
            if(event.ngModel.currentValue){
                this.rangeValue = event.ngModel.currentValue;
            }
            else{
                if(this.rangeValue && this.rangeValue.start) this.rangeValue.start = undefined;
                if(this.rangeValue && this.rangeValue.end) this.rangeValue.end = undefined;
                if(this.itemSource && this.itemSource.dropList && (this.itemSource.dropList instanceof Array)){
                    this.itemSource.dropList.forEach(e =>{if(e.bDefault){
                        if(!this.rangeValue) this.rangeValue = {};
                        this.rangeValue.dropSel = e;
                    }});

                    if(!this.rangeValue) 
                    {
                        this.rangeValue = {};
                        this.rangeValue.dropSel = this.itemSource.dropList[0];
                    }
                    this.rangeValue.$parentCriteriaId = this.itemSource.$parentCriteriaId;
                }    
            }
        }
    };

    showLabel(){
        return !(this.label === undefined);
    }
};

let inputRange = () => {
    return {
        template: require('./template/input_range.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            label:'@',
            displayPath:'@',
            itemSource:'<',
            ctrlFlex:'<',
            ngModel:'<',
            vmChanged:'&'
        },
        controller: ['$scope', 'componentCommonService', 'commonService', inputRangeCtrl]
    }
};

export default inputRange;