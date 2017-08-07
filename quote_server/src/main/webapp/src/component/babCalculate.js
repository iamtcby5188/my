const initView = Symbol('initView');

class babCalculateCtrl {

    constructor($scope,componentCommonService,httpService) {
        this.$scope = $scope;
        this.componentCommonService = componentCommonService;
        this.httpService=httpService;

        this[initView]();

        this.billAmountRege =  /^(\d+)$|^(\d+\.\d{1,2})$/;
        this.adjustDayRege = /^[0-9]$/;
        this.dropList =[
                    {
                        displayName: '‰', regexp: /^(\d+)$|^(\d+\.\d{1,2})$/, procFunc: e => {
                            // 月利率‰ * 12 = 年利率%
                            var result = (+e) * 1.20;
                            if (result) result = result.toFixed(3);
                            return result;
                        }
                    },
                    { displayName: '%', regexp: /^(\d+)$|^(\d+\.\d{1,3})$/, procFunc: e => e , isDefault: true}
                ]
        this.comCondition ={};
        this.condition1 = {};
        this.condition2 = {};

        this.calResp ={};
    };

    [initView]() {
        console.debug('babCalculateCtrl initView');
    };

    $routerOnActivate (currentInstruction, previousInstruction) {
        if (currentInstruction) {
            if (currentInstruction.routeData && currentInstruction.routeData.data) {
                this.theme = currentInstruction.routeData.data.theme;
            }
        }
    };

    getPrice(price){
        if(price && price.selectedItem && price.selectedItem.procFunc && price.inputString){
            return price.selectedItem.procFunc(price.inputString);
        }
    }

    openError(message){
            this.componentCommonService.openErrorDialog({
                title:"错误",
                theme:this.theme,
                message:message?message:""
            })
    }

    checkCalCondition(condition){
        if(condition.price && condition.price.inputString && condition.tradeDate){
            return 'ok';
        }
        else if(!(condition.price && condition.price.inputString) && !condition.tradeDate){
            return 'all-empty';
        }
        else{
            return 'one-empty';
        }
    }

    checkPrice(price){
        if(price && price.selectedItem && price.selectedItem.regexp && price.inputString){
            return price.selectedItem.regexp.test(price.inputString);
        }

        return false;
    }

    checkParam(){
        if(!this.comCondition.billAmount){
            this.openError("票面金额不能为空");
            return false;
        }

        if(!this.comCondition.dueDate){
            this.openError("到期日不能为空");
            return false;
        }

        if(!this.billAmountRege.test(this.comCondition.billAmount) || parseFloat(this.comCondition.billAmount) < 0.01 || parseFloat(this.comCondition.billAmount) > 10000000000.00){
            this.openError("票面金额错误,金额范围 0.01 ~ 10,000,000,000, 小数点后两位");
            return false;
        }

        if(this.condition1.adjustDay){
            if(!this.adjustDayRege.test(this.condition1.adjustDay)){
                this.openError("条件一调整天数输入错误,只能输入整数,有效范围0~9天");                 
                return false;
            }
        }

        if(this.checkCalCondition(this.condition1) !== 'ok'){
            this.openError("计算条件一错误，交易日、利率不能为空");
            return false;
        }else{
            var priceVaule = this.condition1.price.selectedItem.procFunc(this.condition1.price.inputString);
            if(!(priceVaule >= 0.001 && priceVaule <= 99.999)){
                this.openError("利率1填写超出有效范围,年化利率有效范围0.001~99.999");
                return false;
            }

            if(!this.checkPrice(this.condition1.price)){
                this.openError("利率1填写出错,%保留3位小数,‰保留2位小数");
                return false;
            }
        }

        if(this.checkCalCondition(this.condition2) === 'one-empty'){
            this.openError("计算条件二错误，交易日、利率必须一起填写");
            return false;
        }
        else if(this.checkCalCondition(this.condition2) === 'ok'){

            var priceVaule = this.condition2.price.selectedItem.procFunc(this.condition2.price.inputString);
            if(!(priceVaule >= 0.001 && priceVaule <= 99.999)){
                this.openError("利率2填写超出有效范围,年化利率有效范围0.001~99.999");
                return false;
            }

            if(!this.checkPrice(this.condition2.price)){
                this.openError("利率2填写出错，%保留3位小数,‰保留2位小数");
                return false;
            }
        }

        if(this.condition2.adjustDay){
            if(!this.adjustDayRege.test(this.condition2.adjustDay)){
                this.openError("条件二调整天数输入错误,只能输入整数,有效范围0~9天");                 
                return false;
            }
        }

        if(this.condition1.tradeDate && this.comCondition.dueDate && (this.comCondition.dueDate.getTime() - this.condition1.tradeDate.getTime()) < 0){
                this.openError("交易日1范围出错，到期日必须大于交易日");                 
                return false;
        }

        if(this.condition2.tradeDate && this.comCondition.dueDate && (this.comCondition.dueDate.getTime() - this.condition2.tradeDate.getTime()) < 0){
                this.openError("交易日2范围出错，到期日必须大于交易日");                 
                return false;
        }
    
        return true;
    }

    onClickCal(evt){
        if(!this.checkParam()){
            return;
        }
        var curDate = new Date();
        curDate.setHours(0);
        curDate.setMinutes(0);
        curDate.setSeconds(0);
        curDate.setMilliseconds(0);

        let param = {
            amount:this.comCondition.billAmount,
            dueDate:this.comCondition.dueDate?this.comCondition.dueDate.getTime():curDate.getTime(),
            firstDetailsRequest:{
                tradeDate:this.condition1.tradeDate?this.condition1.tradeDate.getTime():curDate.getTime(),
                adjustDays:this.condition1.adjustDay?this.condition1.adjustDay:0,
                price:this.getPrice(this.condition1.price),
            },
            secondDetailsRequest:{
                tradeDate:this.condition2.tradeDate?this.condition2.tradeDate.getTime():curDate.getTime(),
                adjustDays:this.condition2.adjustDay?this.condition2.adjustDay:0,
                price:this.getPrice(this.condition2.price),
            }
        };

        if(this.checkCalCondition(this.condition2) !== 'ok'){
            delete(param["secondDetailsRequest"]);
        }

        this.httpService.postService('/bab_quote/calculator/calculate', param)
            .then(e=>{
                    if(e && e.result && angular.isArray(e.result) && e.result.length === 1){
                        this.calResult = e.result[0];
                    }
                },e=>{
                    if(e && e.return_message && e.return_message && e.return_message.exceptionMessage){
                        this.openError("计算错误  " + e.return_message.exceptionMessage);
                    }
                    else{
                      this.openError("计算错误");                 
                    }
                });

    }

    onClickReset(evt){
        this.calResult = {};
        this.comCondition = {};
        this.condition1 = {};
        this.condition2 = {};
    }
};

let babCalculate = () => {
    return {
        template: require('./template/bab_calculate.html'),
        bindings: {
            theme: '@mdTheme'
        },
        controller: ['$scope','componentCommonService','httpService', babCalculateCtrl]
    }
};

export default babCalculate;