(function (angular, React) {
    'use strict';

    // React 为可选组件
    if (!React) {
        console.warn('AvalonUI: React and React-DOM not loaded.');
        return;
    }

    // 定义JSX React控件
    var component = React.createClass({
        propTypes: {
            fname: React.PropTypes.string.isRequired,
            lname: React.PropTypes.string.isRequired
        },
        render: function () {
            return <span>The name of Super Man is {this.props.fname} {this.props.lname}</span>;
        }
    });

    // 使用Directive注册
    angular.module('avalon.ui').directive("reactHelloJsxComponentDirective", ['reactDirective', reactDirective => reactDirective(component)]);

})(window.angular, window.React);