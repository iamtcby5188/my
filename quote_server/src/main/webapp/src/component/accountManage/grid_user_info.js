const initView = Symbol('initView');
class gridUserInfoCtrl {

    constructor($scope,uiGridConstants) {
        this.$scope = $scope;
        this.$scope.uiGridConstants  = uiGridConstants ;
        
        this.$scope.gridOptions ={
            //行高
            rowHeight: 30,
            //是否显示横向滚动条，只有两个值，默认uiGridConstants.scrollbars.ALWAYS
            enableHorizontalScrollbar: uiGridConstants.scrollbars.ALWAYS,
            enableVerticalScrollbar: uiGridConstants.scrollbars.ALWAYS,
            enableColumnMenus: false,
            enableFiltering: false,
            enableSorting: false,

            //是否允许调整列宽度
            enableColumnResizing: true,
            //是否允许调整列的顺序
            enableColumnMoving: true,
            //是否无限滚动，默认true
            enableInfiniteScroll: false,
            //滚动到距离底部10条时加载数据，默认20
            infiniteScrollRowsFromEnd: 20,
            //是否允许向下无限滚动，默认true
            infiniteScrollDown: false,
            //是否允许向上无限滚动，默认true
            infiniteScrollUp: false,
            onRegisterApi: function (gridApi) {

            },
            columnDefs: [
                {
                    name:"账户名",
                    field:'joiningUserDto.name',
                    width:"*",
                    visible:true
                },
                {
                    name:"与本账户关系",
                    cellTemplate:'<div class="ui-grid-cell-contents">{{row.entity.babJoiningTarget==="PARENT"? "父账户":"子账户"}}</div>',
                    width:110
                },
                {
                    name:"   ",
                    cellTemplate:'<div ng-if="row.entity.babJoiningTarget===\'CHILD\'" class="ui-grid-cell-contents" ng-click="grid.appScope.unJoinUser(row.entity)"><a class="pointer">解除关联</a></div>',
                    width:80
                },
                ],            
        };

        this.$scope.unJoinUser = e=>{
            if(this.unJoinUser){
                this.unJoinUser({contact:e.joiningUserDto});
            }
        }
        this[initView]();
    };

    [initView]() {
        console.debug('gridUserInfoCtrl initView');
    };

    $onChanges(evt){
        this.$scope.gridOptions.data = this.joninUser;
    }
};

let gridUserInfo = () => {
    return {
        template: require('./template/grid_user_info.html'),
        bindings: {
            theme: '@',
            joninUser:'<',
            unJoinUser:'&'
        },
        controller: ['$scope','uiGridConstants', gridUserInfoCtrl]
    }
};

export default gridUserInfo;