(function (angular, React) {
    'use strict';
    
    // React 为可选组件
    if (!React) {
        console.warn('AvalonUI: React and React-DOM not loaded.');
        return;
    }

    // 定义React控件
    var component = React.createClass({
        propTypes: {
            fname: React.PropTypes.string.isRequired,
            lname: React.PropTypes.string.isRequired
        },
        render: function () {
            return React.DOM.span(null, 'The name of Super Man is ' + this.props.fname + ' ' + this.props.lname);
        }
    });
    
    // 使用Directive注册
    angular.module('avalon.ui').directive("reactHelloComponentDirective", ['reactDirective', function (reactDirective) {
            return reactDirective(component);
        }]);
    
    // 使用Value注册
    angular.module('avalon.ui').value('reactHelloComponentValue', component);

    // 使用Service注册
    angular.module('avalon.ui').service('reactHelloComponentService', [function() {
        return component;
    }]);

})(window.angular, window.React);