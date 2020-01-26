(function () {
    "use strict";

    var app = angular.module('App');

    app.component('code.component', {
        templateUrl: '/app/components/code/code.component.html',
        controllerAs: 'ctrl',
        controller: function () {
            var ctrl = this;
            ctrl.state = 'waiting';
            
            ctrl.$onInit = function () {
                ctrl.state = 'ready';
            };
        }});
})();
