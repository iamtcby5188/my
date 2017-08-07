const initView = Symbol('initView');
import showImgCtrl from './../../controller/showImgCtrl';

class inputFileSelectCtrl {

    constructor($scope,componentCommonService,commonService) {
        this.$scope = $scope;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;
        this[initView]();
        this.$scope.thumb=undefined;
        this.$scope.bShowImg = false;
        this.$scope.bShowTip = false;
        this.$scope.bShowTipErrType = false;

        $scope.handleFiles =  (files)=> {
            this.$scope.thumb = undefined;
            this.$scope.bShowImg = false;
            this.$scope.bShowTip = false;
            this.$scope.bShowTipErrType = false;
            var reader = new FileReader();
            var pic = files[0];
            if(!pic) return;
            if (!(/[.](jpg|png)$/.test(pic.name))) {
                this.commonService.safeApply(this.$scope, ()=>{
                    this.$scope.bShowTipErrType = true;
                    this.$scope.thumb = undefined;
                    this.$scope.bShowImg = false;
                    this.$scope.bShowTip = false;
                })

                this.ngModelCtrl.$viewValue = this.$scope.thumb;
                this.ngModelCtrl.$commitViewValue();
                if (this.vmChanged) this.vmChanged();
                
                $("#file_1").val("请选择图片");

                return;
            }

            reader.readAsDataURL(pic);
            if(pic.size < 200 * 1024){
                var img = new Image();
                
                reader.onload = ()=>{
                    this.commonService.safeApply(this.$scope, ()=>{
                            this.$scope.thumb = reader.result;
                            img.src = reader.result;
                            if(img.height > 300 && img.width < 300){
                                this.imgHeight = 300;
                                this.imgWidth = parseInt(300 / img.height * img.width);
                            }
                            else if(img.width > 300){
                                this.imgWidth = 300;
                                this.imgHeight = parseInt(300/img.width*img.height);
                            }
                            else{
                                this.imgWidth = img.width;
                                this.imgHeight = img.height;
                            }

                            $("#img").width(this.imgWidth);
                            $("#img").height(this.imgHeight);
                            
                            this.$scope.bShowImg = true;
                            this.$scope.bShowTip = false;

                            $("#file_1").val(pic.name);
                            this.ngModelCtrl.$viewValue = this.$scope.thumb;
                            this.ngModelCtrl.$commitViewValue();
                            if (this.vmChanged) this.vmChanged();
                    })
                }
            }
            else{
                this.commonService.safeApply(this.$scope, ()=>{
                        this.$scope.bShowTip = true;
                        this.ngModelCtrl.$viewValue = this.$scope.thumb;
                        this.ngModelCtrl.$commitViewValue();
                        if (this.vmChanged) this.vmChanged();
                        $("#file_1").val("请选择图片");
                    })
            }
        };
    };

    [initView]() {
        console.debug('inputFileSelectCtrl initView');
    };

    onClickUpload(){
        // if(this.onUploadPic)
        //     this.onUploadPic({picBase64:this.$scope.thumb})

        this.ngModelCtrl.$viewValue = this.$scope.thumb;
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    }

    $onChanges(event){
        if(event && event.ngModel){
            if(event.ngModel.previousValue !== undefined && event.ngModel.currentValue === undefined){
                this.$scope.bShowTip = false;
                this.$scope.bShowImg = false;
                this.$scope.bShowTipErrType = false;
                this.$scope.thumb = undefined;
                $("#file_1").val("请选择图片");
                $("#file").val("");
            }
        }
    }
    onShowOrg(){
        this.componentCommonService.openShowImg(showImgCtrl,{
            theme:this.theme,
            title:"查看原图",
            thumb:this.$scope.thumb
        });
    }
};

let inputFileSelect = () => {
    return {
        template: require('./template/input_file_select.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            label: '@',
            isRequired:'@',
            labelFlex:'<',
            inputFlex:'<',
            ngModel: '<',
            isShowThumb:'@',
            vmChanged:'&'
        },
        controller: ['$scope','componentCommonService', 'commonService',inputFileSelectCtrl],
    }
};

export default inputFileSelect;