/*
 *  Quoteboard menu for Angular 1.x
 *  @author shuang.wang@sumscope.com
 *  @version (2016-09-19)
 */


angular.module('avalon.ui').directive('ssMenuDirective', [

    // Note：当前展开的Menu做单例控制
    // Service？
    // Class？ 

    function() {
        return {
            restrict: 'C',
            replace: true,
            scope: {
                //传入的菜单数据
                data: '=menuData',
                //点击菜单，将数据传入controller
                menuClickData: '=menuClick',
                //禁止滚动的元素
                menuForbidScrolldom: '=menuForbidScrolldom',
                //是否不应用边界检测，默认为false
                menuForced: '=menuForced'
                
                // Note： &onClickMenuItem
            },
            //template: '<div class="ss-menu" ng-show="menuShow">' +
            //    '<ul class="ss-text-secondary" ng-class="{\'ss-menu-active\':mindex==$index,\'ss-menu-icon\':d.icon,\'ss-menu-children\':d.children}" ng-repeat="d in data" ng-mouseleave="menuOut()" ng-mouseover="menuOver(d.children,$index)" ng-click="d.event?d.event($event,d):menuClickFn($event,d)">{{d.title}}' +
            //    '<li ng-if="d.line" ng-class="{\'ss-menu-line\':d.line}"></li>' +
            //    '<div class="ss-menu ss-menu-first-children" ng-class="{\'ss-menu-showLeft\':menuShowLeft,\'ss-menu-showRight\':!menuShowLeft}" ng-if="showchildrenindex==$index">' +
            //    '<ul class="ss-text-secondary" ng-mouseover="childrenMenuOver($index)" ng-mouseleave="childrenMenuOut()" ' +
            //    'ng-class="{\'ss-menu-active\':childrenhoverindex==$index,\'ss-menu-icon\':dc.icon}" ng-repeat="dc in d.children track by $index" ng-click="dc.event?dc.event($event,dc):menuClickFn($event,dc)">{{dc.title}}' +
            //    '<li ng-if="dc.line" ng-class="{\'ss-menu-line\':dc.line}"></li>' +
            //    '</ul>' +
            //    '</div>' +
            //    '</ul> ' +
            //    '</div>',
            // Note：可以通过templateUrl来指定模板，便于编辑
            templateUrl: 'template/menu.html',
            link: function(scope, ele, attr) {
                /*
                     * 第一层菜单的索引，主要处理鼠标悬浮
                     * scope.mindex = -1;
                     * 第一层菜单的显示子菜单索引，主要处理显示子菜单
                     * scope.showchildrenindex = -1;
                     * 第一层菜单的子菜单索引，主要用于处理子菜单悬浮
                     * scope.childrenhoverindex = -1;
                     * */
                //存储禁止的事件
                var forbidScrollEvents = [];
                // var menuContextmenuDom = document.getElementsByClassName('ss-menu-contextmenu')[0];
                var menuContextmenuDom = ele.parent('.ss-menu-contextmenu');
                // document.getElementsByClassName('ss-menu-contextmenu')[0];
                var menuContextmenu = angular.element(menuContextmenuDom);

                if (menuContextmenu)
                    menuContextmenu.on('contextmenu', function(e) {
                        scope.$apply(function() {
                            e.preventDefault();
                            //显示菜单
                            scope.menuShow = true;
                            scope.mindex = -1;
                            scope.showchildrenindex = -1;
                            scope.childrenhoverindex = -1;
                            scope.clientX = e.clientX;
                            ele.css({
                                'left': calcX(menuContextmenu, ele, e.clientX, 0).width + 'px',
                                'top': calcY(menuContextmenu, ele, e.clientY, $('.ss-menu ul').height() * scope.data.length).height + 'px'
                            });
                        });
                        if (scope.menuForbidScrolldom)
                            if (scope.menuForbidScrolldom[scope.menuForbidScrolldom.length - 1] != "true") {
                                $.each(scope.menuForbidScrolldom, function(k, e) {
                                    if ($(e))
                                        $(e).off('scroll').on('scroll', function() {
                                            $scope.$apply(function() {
                                                scope.menuShow = false;
                                            });
                                        });
                                });
                            } else {
                                $.each(scope.menuForbidScrolldom, function(k, e) {
                                    forbidScrollEvents.push($(e).scroll);
                                    $(e).off('scroll');
                                    var eScrollTop = $(e).scrollTop();
                                    $(e).on('scroll', function() {
                                        $(this).scrollTop(eScrollTop);
                                        console.log($(this).scrollTop());
                                    });
                                });
                            }
                    });

                $(document).on('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    // Note：没有双绑的情况下$apply也没问题，推荐使用safeApply
                    scope.$apply(function() {
                        if (scope.menuShow)
                            scope.menuShow = false;
                    });
                    //去掉禁止滚动事件
                    if (scope.menuForbidScrolldom && scope.menuForbidScrolldom.length > 0) {
                        $.each(scope.menuForbidScrolldom, function(k, e) {
                            if ($(e))
                                $(e).off('scroll');
                        });
                    }
                });

                //鼠标悬浮
                scope.menuOver = function(hasChildren, index) {
                    scope.mindex = index;
                    scope.showchildrenindex = hasChildren ? index : -1;

                    if (hasChildren) {
                        if (!calcX(menuContextmenu, ele, scope.clientX, 1).istrue) {
                            scope.menuShowLeft = false;
                        } else {
                            scope.menuShowLeft = true;
                        }
                    }
                };
                scope.childrenMenuOver = function(index) {
                    scope.childrenhoverindex = index;
                };
                scope.childrenMenuOut = function(index) {
                    scope.childrenhoverindex = -1;
                };

                //菜单点击事件
                scope.menuClickFn = function($event, obj) {
                    scope.menuClickData = obj;
                    $event.stopPropagation();
                };

                /*
                     * 计算菜单到右边的距离
                     * parentele 容器dom
                     * element 菜单dom
                     * x e.clientX
                     * times 0代表一级菜单,1代表二级菜单，2代表三级菜单，以此类推
                     * return obj
                     * */
                var calcX = function(parentele, element, x, times) {
                    var obj = {
                        width: 0,
                        istrue: true
                    };
                    //判断父级是否有横向滚动条，如果有，则改变可视区域为父级的宽
                    if ($(parentele).parent().width() < $(parentele).width() && ($(parentele).parent().css('overflow') == 'auto' || $(parentele).parent().css('overflow') == 'scroll')) {
                        parentele = $(parentele).parent();
                    }
                    if (!(scope.menuForced && scope.menuForced.x)) {
                        if ($(parentele).width() - (x - $(parentele).offset().left) - $(element).width() * times < $(element).width()) {
                            obj.width = x - $(element).width();
                            obj.istrue = false;
                        } else {
                            obj.width = x;
                        }
                    } else {
                        obj.width = x;
                    }
                    return obj;
                };

                /*
                     * 计算菜单到下边的距离
                     * parentele 容器dom
                     * element 菜单dom
                     * y e.clientY
                     * eleheight菜单的高度，指令中，用的单个菜单的高度*data.length+菜单.paddingTop+菜单.paddingBottom
                     * return obj
                     * */
                var calcY = function(parentele, element, y, eleheight) {
                    //判断父级是否有纵向滚动条，如果有，则改变可视区域为父级的高
                    if ($(parentele).parent().height() < $(parentele).height() && ($(parentele).parent().css('overflow') == 'auto' || $(parentele).parent().css('overflow') == 'scroll')) {
                        parentele = $(parentele).parent();
                    }
                    var obj = {
                        height: 0,
                        istrue: true
                    };
                    if (!(scope.menuForced && scope.menuForced.y)) {
                        if ($(parentele).height() - (y - ($(parentele).offset().top - $(document).scrollTop())) < (eleheight + parseInt($(element).css('paddingTop')) + parseInt($(element).css('paddingBottom')))) {
                            obj.height = y - eleheight;
                            obj.istrue = false;
                        } else
                            obj.height = y;
                    } else {
                        obj.height = y;
                    }
                    return obj;
                }

            }
        }
    }
]);